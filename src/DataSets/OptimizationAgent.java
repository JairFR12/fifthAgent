package DataSets;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class OptimizationAgent extends Agent {

    protected void setup() {
        System.out.println("Agente de Optimización " + getLocalName() + " iniciado.");

        // Registrar el servicio de optimización en las Páginas Amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("optimization-service");
        sd.setName("JADE-optimization");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Añadir comportamientos
        addBehaviour(new PerformOptimization());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Agente de Optimización " + getAID().getName() + " terminado.");
    }

    private class PerformOptimization extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String dataset = msg.getContent();
                System.out.println("Dataset recibido: " + dataset);

                // Aplicar la técnica de optimización aquí
                String resultados = aplicarOptimización(dataset);

                // Enviar los resultados al agente solicitante
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(resultados);
                myAgent.send(reply);
            } else {
                block();
            }
        }

        private String aplicarOptimización(String dataset) {
            // Implementar la lógica para aplicar la técnica de optimización
            // Dependiendo de los datos y la técnica seleccionada
            return "Resultados de la optimización"; // Ejemplo de resultado
        }
    }
}
