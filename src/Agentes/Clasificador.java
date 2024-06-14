package Agentes;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Clasificador extends Agent {

    protected void setup() {
        System.out.println("Agente de Clasificación de Problemas " + getLocalName() + " iniciado.");

        // Registrar el servicio de clasificación de problemas en las Páginas Amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("classification-service");
        sd.setName("JADE-classification");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Añadir comportamientos
        addBehaviour(new ReceiveModelVariables());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Agente de Clasificación de Problemas " + getAID().getName() + " terminado.");
    }

    private class ReceiveModelVariables extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String variables = msg.getContent();
                System.out.println("Variables del modelo recibidas: ");
                String[] parts = variables.split(";");
                String[] xValues = parts[0].replace("x:", "").split(",");
                String[] yValues = parts[1].replace("y:", "").split(",");

                System.out.println("X Values:");
                for (int i = 0; i < xValues.length; i++) {
                    System.out.print(xValues[i]);
                }

                System.out.println("");
                System.out.println("Y Values:");
                for (int i = 0; i < yValues.length; i++) {
                    System.out.print(yValues[i]);
                }
                System.out.println("");

                List<Double> xList = Arrays.stream(xValues).map(Double::parseDouble).collect(Collectors.toList());
                List<Double> yList = Arrays.stream(yValues).map(Double::parseDouble).collect(Collectors.toList());

                String tecnicaAnalisis = determinarTecnicaAnalisis(xList, yList);
                String tecnicaOptimizacion = determinarTecnicaOptimizacion(xList, yList);

                // Enviar datos al agente determinado
                enviarDatosAlAgente(tecnicaAnalisis, tecnicaOptimizacion, xList, yList);

                // Enviar respuesta al agente solicitante
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(tecnicaAnalisis + "," + tecnicaOptimizacion);
                myAgent.send(reply);
               // System.out.println("EnviandoRespuesta");
            } else {
                block();
            }
        }

        private String determinarTecnicaAnalisis(List<Double> x, List<Double> y) {
            System.out.println("Técnica de análisis predictivo: ");
            double correlacion = calcularCorrelacion(x, y);

            if (x.size() == 1) {  // Solo una variable independiente
                System.out.println("Por Regresión Lineal");
                return "regresion-lineal";
            } else if (x.size() > 1 && correlacion > 0.8) {
                System.out.println("Por Regresión Multiple");
                return "regresion-multiple";
            } else {
                System.out.println("Por Regresión Polinomial");
                return "regresion-polinomial";
            }
        }

        private double calcularCorrelacion(List<Double> x, List<Double> y) {
            int n = x.size();
            double sumX = x.stream().mapToDouble(Double::doubleValue).sum();
            double sumY = y.stream().mapToDouble(Double::doubleValue).sum();
            double sumXY = 0;
            double sumX2 = 0;
            double sumY2 = 0;
            for (int i = 0; i < n; i++) {
                sumXY += x.get(i) * y.get(i);
                sumX2 += Math.pow(x.get(i), 2);
                sumY2 += Math.pow(y.get(i), 2);
            }
            return (n * sumXY - sumX * sumY) / Math.sqrt((n * sumX2 - Math.pow(sumX, 2)) * (n * sumY2 - Math.pow(sumY, 2)));
        }

        private String determinarTecnicaOptimizacion(List<Double> x, List<Double> y) {
            System.out.println("Técnica de Optimización");
            int numVariables = x.size();

            if (numVariables < 50) {
                System.out.println("Por algoritmo genético");
                return "algoritmo-genetico";
            } else {
                System.out.println("Particle Swarm");
                return "pso";
            }
        }


        //Enviar Datos a DataSetAgent
        private void enviarDatosAlAgente(String tecnicaAnalisis, String tecnicaOptimizacion, List<Double> x, List<Double> y) {
            AID analysisAgentAID = buscarAgente(tecnicaAnalisis);
            System.out.println("Buscando el Agente correspondiente: ");
            if (analysisAgentAID == null) {
                System.out.println("Agente para " + tecnicaAnalisis + " no encontrado.");
                return;
            }

            AID optimizationAgentAID = buscarAgente(tecnicaOptimizacion);
            if (optimizationAgentAID == null) {
                System.out.println("Agente para " + tecnicaOptimizacion + " no encontrado.");
                return;
            }

            ACLMessage analysisMsg = new ACLMessage(ACLMessage.REQUEST);
            analysisMsg.addReceiver(analysisAgentAID);
            analysisMsg.setConversationId("analysis");

            ACLMessage optimizationMsg = new ACLMessage(ACLMessage.REQUEST);
            optimizationMsg.addReceiver(optimizationAgentAID);
            optimizationMsg.setConversationId("optimization");

            // Convertir los datos a formato de cadena
            StringBuilder sb = new StringBuilder();
            sb.append("x:");
            for (Double val : x) sb.append(val).append(",");
            sb.append(";y:");
            for (Double val : y) sb.append(val).append(",");

            analysisMsg.setContent(sb.toString());
            optimizationMsg.setContent(sb.toString());

            System.out.println("Enviando datos al agente de análisis");
            send(analysisMsg);

            // Añadir técnica de optimización en el mensaje para el agente de optimización
            sb.append(";optim:").append(tecnicaOptimizacion);
            optimizationMsg.setContent(sb.toString());

            System.out.println("Enviando datos al agente de optimización");
            send(optimizationMsg);
        }


        private AID buscarAgente(String serviceType) {
           // System.out.println("Buscando el Agente correspondiente: ");
            AID agente = null;
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType(serviceType);
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                if (result.length > 0) {
                    agente = result[0].getName();
                }
            } catch (FIPAException fe) {
                fe.printStackTrace();
            }
            return agente;
        }
    }
}
