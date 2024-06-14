package regresionPolinomial;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;

public class RegresionPolinomial extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");

        // Registrar el servicio de regresión polinomial en el Directory Facilitator
        registrarServicio("PolyRAgent", "regresion-polinomial");

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

                // Convertir los datos a un formato adecuado para tu código de regresión polinomial
                double[] xData = Arrays.stream(xValues).mapToDouble(Double::parseDouble).toArray();
                double[] yData = Arrays.stream(yValues).mapToDouble(Double::parseDouble).toArray();

                // Crear el objeto para la regresión polinomial (grado 3 por defecto)
                PolynomialRegression polyReg = new PolynomialRegression(xData, yData, 3); // Grado del polinomio

                // Realizar cálculos y operaciones de la regresión polinomial
                polyReg.fit();
                polyReg.printRegEquation();
                polyReg.predict(6); // Ejemplo de predicción para x = 6

                // Otros pasos de tu lógica después de la regresión polinomial
            } else {
                block();
            }
        }
    }
}

class PolynomialRegression {
    private final double[] xData;
    private final double[] yData;
    private final int degree;
    private double[] coefficients;

    public PolynomialRegression(double[] xData, double[] yData, int degree) {
        this.xData = xData;
        this.yData = yData;
        this.degree = degree;
    }

    public void fit() {
        double[] x = xData;
        double[] y = yData;

        int N = x.length;
        double[][] X = new double[N][degree + 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= degree; j++) {
                X[i][j] = Math.pow(x[i], j);
            }
        }

        // Resolver el sistema de ecuaciones normales (X^T * X) * a = X^T * y
        Matrix X_matrix = new Matrix(X);
        Matrix y_matrix = new Matrix(y, N);
        Matrix X_transpose = X_matrix.transpose();
        Matrix XTX = X_transpose.times(X_matrix);
        Matrix XTy = X_transpose.times(y_matrix);
        Matrix coefficients_matrix = XTX.solve(XTy);
        coefficients = coefficients_matrix.getColumnPackedCopy();
    }

    public void printRegEquation() {
        System.out.print("Ecuación de regresión: y = ");
        for (int i = 0; i < coefficients.length; i++) {
            System.out.print(coefficients[i] + " * x^" + i);
            if (i < coefficients.length - 1) {
                System.out.print(" + ");
            }
        }
        System.out.println();
    }

    public void predict(double x) {
        double y = 0;
        for (int i = 0; i < coefficients.length; i++) {
            y += coefficients[i] * Math.pow(x, i);
        }
        System.out.println("Predicción para x = " + x + " es y = " + y);
    }
}

class Matrix {
    private final double[][] data;

    public Matrix(double[][] data) {
        this.data = data;
    }

    public Matrix(double[] data, int N) {
        this.data = new double[N][1];
        for (int i = 0; i < N; i++) {
            this.data[i][0] = data[i];
        }
    }

    public Matrix transpose() {
        int m = data.length;
        int n = data[0].length;
        double[][] transposedData = new double[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                transposedData[j][i] = data[i][j];
            }
        }
        return new Matrix(transposedData);
    }

    public Matrix times(Matrix other) {
        int m1 = data.length;
        int n1 = data[0].length;
        int n2 = other.data[0].length;
        double[][] resultData = new double[m1][n2];
        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < n1; k++) {
                    resultData[i][j] += data[i][k] * other.data[k][j];
                }
            }
        }
        return new Matrix(resultData);
    }

    public Matrix solve(Matrix other) {
        int n = data.length;
        double[][] augmentedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(data[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][n] = other.data[i][0];
        }

        for (int i = 0; i < n; i++) {
            double maxEl = Math.abs(augmentedMatrix[i][i]);
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmentedMatrix[k][i]) > maxEl) {
                    maxEl = Math.abs(augmentedMatrix[k][i]);
                    maxRow = k;
                }
            }

            double[] temp = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = augmentedMatrix[i];
            augmentedMatrix[i] = temp;

            double diagElement = augmentedMatrix[i][i];
            for (int j = 0; j < n + 1; j++) {
                augmentedMatrix[i][j] /= diagElement;
            }

            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < n + 1; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        double[][] solution = new double[n][1];
        for (int i = 0; i < n; i++) {
            solution[i][0] = augmentedMatrix[i][n];
        }
        return new Matrix(solution);
    }

    public double[] getColumnPackedCopy() {
        int m = data.length;
        int n = data[0].length;
        double[] result = new double[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i + j * m] = data[i][j];
            }
        }
        return result;
    }
}