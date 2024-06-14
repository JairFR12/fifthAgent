package Agentes.FourthAgent;

import jade.core.behaviours.Behaviour;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
    class GeneticAlgorithmBehaviour extends Behaviour {
        private double[] x;
        private double[] y;
        private static final int POPULATION_SIZE = 100;
        private static final double CROSSOVER_RATE = 0.7;
        private static final double MUTATION_RATE = 0.02;
        private static final int MAX_GENERATIONS = 500;
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
            for (int i = 0; i < POPULATION_SIZE; i++) {
                population[i] = new Individual();
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
            return error;
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
    }



