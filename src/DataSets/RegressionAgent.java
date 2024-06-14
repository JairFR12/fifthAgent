package DataSets;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegressionAgent extends Agent {

    protected void setup() {
        System.out.println("Agente de Regresión " + getLocalName() + " iniciado.");

        // Registrar el servicio de regresión en las Páginas Amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("regression-service");
        sd.setName("JADE-regression");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Añadir comportamientos
        addBehaviour(new PerformRegression());
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Agente de Regresión " + getAID().getName() + " terminado.");
    }

    private class PerformRegression extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String dataset = msg.getContent();
                System.out.println("Dataset recibido: " + dataset);

                // Aplicar la técnica de regresión aquí
                String resultados = aplicarRegresion(dataset);

                // Enviar los resultados al agente solicitante
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                reply.setContent(resultados);
                myAgent.send(reply);
            } else {
                block();
            }
        }

        private String aplicarRegresion(String dataset) {
            // Implementar la lógica para aplicar la técnica de regresión
            // Dependiendo de los datos y la técnica seleccionada
            return "Resultados de la regresión"; // Ejemplo de resultado
        }
    }
}
