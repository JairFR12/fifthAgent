package Agentes;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class fourthAgent extends Agent {
    protected void setup() {
        System.out.println("Agente " + getLocalName() + " iniciado.");

        // Registrar el servicio de algoritmo genético en el Directory Facilitator
        registrarServicio("GeneticAlgorithmAgent", "algoritmo-genetico");

        // Añadir comportamiento para recibir el dataset del DataSetAgent
        addBehaviour(new ReceiveDataSet());
    }

    protected void takeDown() {
        // Deregistrar el servicio al terminar el agente
        deregistrarServicio();
        System.out.println("Agente " + getLocalName() + " finalizado.");
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
            System.out.println("Servicio registrado: " + agentName + " - " + serviceType);
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

    private class ReceiveDataSet extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                String response = msg.getContent(); // Supongamos que el mensaje contiene el dataset
                String[] parts = response.split(";");

                // Extraer los datos del dataset
                String[] xValues = parts[0].replace("x:", "").split(",");
                String[] yValues = parts[1].replace("y:", "").split(",");

                // Convertir los datos a un formato adecuado para tu algoritmo genético
                double[] xData = Arrays.stream(xValues).mapToDouble(Double::parseDouble).toArray();
                double[] yData = Arrays.stream(yValues).mapToDouble(Double::parseDouble).toArray();

                // Añadir comportamiento del Algoritmo Genético
                myAgent.addBehaviour(new GeneticAlgorithmBehaviour(xData, yData));
            } else {
                block();
            }
        }
    }

    private class GeneticAlgorithmBehaviour extends Behaviour {
        private double[] x;
        private double[] y;
        private static final int POPULATION_SIZE = 50;
        private static final double CROSSOVER_RATE = 0.7;
        private static final double MUTATION_RATE = 0.04;
        private static final int MAX_GENERATIONS = 100;
        private Individual[] population;
        private int generation;

        public GeneticAlgorithmBehaviour(double[] x, double[] y) {
            this.x = x;
            this.y = y;
            this.population = new Individual[POPULATION_SIZE];
            this.generation = 0;
            initializePopulation();
        }

        private void initializePopulation() {
            Random rand = new Random();
            for (int i = 0; i < POPULATION_SIZE; i++) {
                double B0 = rand.nextDouble() * 100; // Inicializa B0 en un rango más razonable
                double B1 = rand.nextDouble() * 100; // Inicializa B1 en un rango más razonable
                population[i] = new Individual(B0, B1);
            }
        }

        private double calculateFitness(Individual individual) {
            double B0 = individual.getB0();
            double B1 = individual.getB1();
            double error = 0.0;
            for (int i = 0; i < x.length; i++) {
                double predictedY = B0 + B1 * x[i];
                error += Math.pow(y[i] - predictedY, 2);
            }
            return error / x.length; // Utiliza el error cuadrático medio (MSE)
        }

        private Individual selectParent() {
            Random rand = new Random();
            Individual parent1 = population[rand.nextInt(POPULATION_SIZE)];
            Individual parent2 = population[rand.nextInt(POPULATION_SIZE)];
            return (calculateFitness(parent1) < calculateFitness(parent2)) ? parent1 : parent2;
        }

        private Individual crossover(Individual parent1, Individual parent2) {
            Random rand = new Random();
            double B0 = (rand.nextDouble() < CROSSOVER_RATE) ? parent1.getB0() : parent2.getB0();
            double B1 = (rand.nextDouble() < CROSSOVER_RATE) ? parent1.getB1() : parent2.getB1();
            return new Individual(B0, B1);
        }

        private void mutate(Individual individual) {
            Random rand = new Random();
            if (rand.nextDouble() < MUTATION_RATE) {
                individual.setB0(individual.getB0() + (-1 + rand.nextDouble() * 2));
            }
            if (rand.nextDouble() < MUTATION_RATE) {
                individual.setB1(individual.getB1() + (-1 + rand.nextDouble() * 2));
            }
            // Asegúrate de que los valores de B0 y B1 permanezcan en un rango razonable
            individual.setB0(Math.max(0, Math.min(100, individual.getB0())));
            individual.setB1(Math.max(0, Math.min(100, individual.getB1())));
        }

        private void evaluatePopulation() {
            for (Individual individual : population) {
                double fitness = calculateFitness(individual);
                individual.setFitness(fitness);
            }
        }

        private void evolvePopulation() {
            Individual[] newPopulation = new Individual[POPULATION_SIZE];
            for (int i = 0; i < POPULATION_SIZE; i++) {
                Individual parent1 = selectParent();
                Individual parent2 = selectParent();
                Individual child = crossover(parent1, parent2);
                mutate(child);
                newPopulation[i] = child;
            }
            population = newPopulation;
        }

        public void action() {
            if (generation < MAX_GENERATIONS) {
                evaluatePopulation();
                evolvePopulation();
                generation++;
                Individual bestIndividual = getBestIndividual();
                System.out.println("Generación " + generation + ": Mejor Fitness = " + bestIndividual.getFitness() +
                        ", B0 = " + bestIndividual.getB0() + ", B1 = " + bestIndividual.getB1());
            } else {
                Individual bestIndividual = getBestIndividual();
                System.out.println("\nMejor solución encontrada: B0 = " + bestIndividual.getB0() + ", B1 = " + bestIndividual.getB1() +
                        ", Fitness = " + bestIndividual.getFitness());
                System.out.println("Ecuación de Regresión: y = " + bestIndividual.getB0() + " + " + bestIndividual.getB1() + "x");
                myAgent.doDelete();
            }
        }

        public boolean done() {
            return generation >= MAX_GENERATIONS;
        }

        private Individual getBestIndividual() {
            return Arrays.stream(population).min(Comparator.comparingDouble(Individual::getFitness)).orElse(null);
        }

        private class Individual {
            private double B0;
            private double B1;
            public double fitness;

            public Individual(double B0, double B1) {
                this.B0 = B0;
                this.B1 = B1;
            }

            public double getB0() {
                return B0;
            }

            public void setB0(double B0) {
                this.B0 = B0;
            }

            public double getB1() {
                return B1;
            }

            public void setB1(double B1) {
                this.B1 = B1;
            }

            public double getFitness() {
                return fitness;
            }

            public void setFitness(double fitness) {
                this.fitness = fitness;
            }
        }
    }
}