package Agentes.FourthAgent;

import java.util.Random;

class Individual {
    private double B0;
    private double B1;
    private double fitness;

    public Individual() {
        Random rand = new Random();
        this.B0 = -100 + rand.nextDouble() * 200;
        this.B1 = -100 + rand.nextDouble() * 200;
        this.fitness = Double.MAX_VALUE;
    }

    public Individual(double B0, double B1) {
        this.B0 = B0;
        this.B1 = B1;
        this.fitness = Double.MAX_VALUE;
    }

    public double getB0() {
        return B0;
    }

    public double getB1() {
        return B1;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setB0(double B0) {
        this.B0 = B0;
    }

    public void setB1(double B1) {
        this.B1 = B1;
    }
}
