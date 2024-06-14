package Agentes.FirstAgent;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;

public class regresionLineal extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");

        // Registrar el servicio de regresión lineal simple en el Directory Facilitator
        registrarServicio("SLRAgent", "regresion-lineal");

        // Comportamiento para recibir el dataset del DataSetAgent
        addBehaviour(new ReceiveDataSet());
    }

    protected void takeDown() {
        // Deregistrar el servicio al terminar el agente
        deregistrarServicio();
        System.out.println("Agent " + getLocalName() + " terminating.");
    }

    private void registrarServicio(String agentName, String serviceType) {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(agentName);
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println("Service registered: " + agentName + " - " + serviceType);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    private void deregistrarServicio() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    public static class ReceiveDataSet extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String response = msg.getContent(); // Supongamos que el mensaje contiene el dataset
                String[] parts = response.split(";");

                // Extraer los datos del dataset
                String[] xValues = parts[0].replace("x:", "").split(",");
                String[] yValues = parts[1].replace("y:", "").split(",");

                // Convertir los datos a un formato adecuado para regresión lineal
                Float[] xData = Arrays.stream(xValues).mapToDouble(Double::parseDouble).mapToObj(d -> (float) d).toArray(Float[]::new);
                Float[] yData = Arrays.stream(yValues).mapToDouble(Double::parseDouble).mapToObj(d -> (float) d).toArray(Float[]::new);

                // Crear objetos necesarios para la regresión lineal
                DiscreteMaths discreteMaths = new DiscreteMaths();
                SLR slr = new SLR(toPrimitive(xData), toPrimitive(yData), discreteMaths);

                // Realizar cálculos y operaciones de la regresión lineal
                slr.calculateIntersection();
                slr.calculateSlope();
                slr.printRegEquation();
                slr.predictAdvertisingSales();
            } else {
                block();
            }
        }

        private float[] toPrimitive(Float[] floatArray) {
            float[] result = new float[floatArray.length];
            for (int i = 0; i < floatArray.length; i++) {
                result[i] = floatArray[i];
            }
            return result;
        }
    }
}
