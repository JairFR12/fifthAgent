package Agentes;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DataSetAgentPolinomial extends Agent {
    private AID classificationAgent;
    private String analysisTechnique;
    private String optimizationTechnique;
    private double[] x = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] y = {1, 4, 9, 16, 25, 36, 49, 64, 81, 100}; // Relación cuadrática

    protected void setup() {
        System.out.println("Agent with Dataset " + getLocalName() + " started.");

        SequentialBehaviour sb = new SequentialBehaviour();

        System.out.println("Buscando Clasificación...");
        sb.addSubBehaviour(new TickerBehaviour(this, 5000) {
            protected void onTick() {
                if (classificationAgent == null) {
                    classificationAgent = buscarAgente("classification-service");
                    if (classificationAgent != null) {
                        System.out.println("Classification Agent found: " + classificationAgent.getName());
                        enviarDataset(classificationAgent, x, y);
                    } else {
                        System.out.println("Classification Agent not found.");
                    }
                }
            }
        });



        //System.out.println("Se ejecuta Esto?");
        sb.addSubBehaviour(new RecibirClasificacion());

        //Tick de la optimización
        sb.addSubBehaviour(new TickerBehaviour(this, 5000) {

            protected void onTick() {
                System.out.println("SegundoTicket");
                if (analysisTechnique != null && optimizationTechnique != null) {
                    AID analysisAgent = buscarAgente(analysisTechnique);
                    if (analysisAgent != null) {
                        System.out.println("Analysis Agent found: " + analysisAgent.getName());
                        enviarDatosAlAgente(analysisAgent, optimizationTechnique, x, y);
                        analysisTechnique = null;
                        optimizationTechnique = null;
                        System.out.println("OnTickTechOpti");
                    } else {
                        System.out.println("Analysis Agent for " + analysisTechnique + " not found.");
                    }

                    AID optimizationAgent = buscarAgente(optimizationTechnique);
                    if (optimizationAgent != null) {
                        System.out.println("Optimization Agent found: " + optimizationAgent.getName());
                    } else {
                        System.out.println("Optimization Agent for " + optimizationTechnique + " not found.");
                    }
                }
            }
        });

        addBehaviour(sb);
    }

    protected void takeDown() {
        System.out.println("DatasetAgent " + getAID().getName() + " terminating.");
    }

    private AID buscarAgente(String serviceType) {
        AID agente = null;
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(this, template);
            if (result.length > 0) {
                agente = result[0].getName();
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return agente;
    }

    private void enviarDataset(AID receiver, double[] x, double[] y) {
        System.out.println("Enviando DataSet...");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);
        msg.setConversationId("dataset-classification");
        msg.setReplyWith("request" + System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();
        sb.append("x:");
        for (double val : x) sb.append(val).append(",");
        sb.append(";y:");
        for (double val : y) sb.append(val).append(",");
        msg.setContent(sb.toString());

        send(msg);
    }


    //Esta función sin uso aparente
    private void enviarDatosAlAgente(AID agentAID, String optimizationTechnique, double[] x, double[] y) {
        System.out.println("EnviarDatosAlAgente");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(agentAID);
        msg.setConversationId("analysis-optimization");
        System.out.println("Hola desde DataSetAgemt");

        StringBuilder sb = new StringBuilder();
        sb.append("x:");
        for (double val : x) sb.append(val).append(",");
        sb.append(";y:");
        for (double val : y) sb.append(val).append(",");
        sb.append(";optim:").append(optimizationTechnique);
        msg.setContent(sb.toString());

        send(msg);
    }

    private class RecibirClasificacion extends Behaviour {

        private MessageTemplate mt;
        private boolean done = false;

        public void action() {
            System.out.println("RecibirClasificación");
            mt = MessageTemplate.MatchConversationId("dataset-classification");
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.INFORM) {
                    String response = msg.getContent();

                    System.out.println("holaaaaaaa");
                    System.out.println("Classification response received: " + response);
                    String[] parts = response.split(",");
                    analysisTechnique = parts[0];
                    optimizationTechnique = parts[1];
                }
                done = true;
            } else {
                block();
            }
        }

        public boolean done() {
            return done;
        }
    }
}
