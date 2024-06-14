package regresionMultiple;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Arrays;

public class RegresionMultiple extends Agent {

    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");

        registrarServicio("MLRAgent", "regresion-multiple");

        addBehaviour(new ReceiveDataSet());
    }

    protected void takeDown() {
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
                String response = msg.getContent();
                String[] parts = response.split(";");

                // Suponiendo que todas las características y el objetivo están en partes separadas
                double[][] xData = new double[parts.length - 1][];
                double[] yData = new double[0];

                for (int i = 0; i < parts.length; i++) {
                    String[] values = parts[i].split(":")[1].split(",");
                    if (i == parts.length - 1) {
                        yData = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                    } else {
                        xData[i] = Arrays.stream(values).mapToDouble(Double::parseDouble).toArray();
                    }
                }

                // Transponer xData para que cada fila sea un conjunto de características
                double[][] xDataTransposed = new double[xData[0].length][xData.length];
                for (int i = 0; i < xData.length; i++) {
                    for (int j = 0; j < xData[i].length; j++) {
                        xDataTransposed[j][i] = xData[i][j];
                    }
                }

                MultipleLinearRegression mlr = new MultipleLinearRegression(xDataTransposed, yData);

                mlr.fit();
                mlr.printRegEquation();
                mlr.predict(new double[]{1.2, 0.8, 2.3}); // Ejemplo de predicción
            } else {
                block();
            }
        }
    }
}

// Clase MultipleLinearRegression
class MultipleLinearRegression {
    private final double[][] xData;
    private final double[] yData;
    private double[] coefficients;

    public MultipleLinearRegression(double[][] xData, double[] yData) {
        this.xData = xData;
        this.yData = yData;
    }

    public void fit() {
        int n = xData.length;
        int p = xData[0].length;

        // Añadir una columna de unos a xData para el término independiente
        double[][] X = new double[n][p + 1];
        for (int i = 0; i < n; i++) {
            X[i][0] = 1.0;
            System.arraycopy(xData[i], 0, X[i], 1, p);
        }

        // Transponer X
        double[][] XT = transpose(X);

        // Calcular XT * X
        double[][] XTX = multiplyMatrices(XT, X);

        // Calcular la inversa de XT * X
        double[][] XTX_inv = invertMatrix(XTX);

        // Calcular XT * y
        double[] XTy = multiplyMatrixVector(XT, yData);

        // Calcular los coeficientes: (XT * X)^-1 * XT * y
        coefficients = multiplyMatrixVector(XTX_inv, XTy);
    }

    private double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        int colsB = b[0].length;
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    private double[] multiplyMatrixVector(double[][] a, double[] b) {
        int rowsA = a.length;
        int colsA = a[0].length;
        double[] result = new double[rowsA];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsA; j++) {
                result[i] += a[i][j] * b[j];
            }
        }
        return result;
    }

    private double[][] invertMatrix(double[][] matrix) {
        int n = matrix.length;
        double[][] identity = new double[n][n];
        for (int i = 0; i < n; i++) {
            identity[i][i] = 1.0;
        }

        for (int i = 0; i < n; i++) {
            double factor = matrix[i][i];
            for (int j = 0; j < n; j++) {
                matrix[i][j] /= factor;
                identity[i][j] /= factor;
            }
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    factor = matrix[j][i];
                    for (int k = 0; k < n; k++) {
                        matrix[j][k] -= factor * matrix[i][k];
                        identity[j][k] -= factor * identity[i][k];
                    }
                }
            }
        }
        return identity;
    }

    public void printRegEquation() {
        System.out.print("Ecuación de regresión: y = ");
        for (int i = 0; i < coefficients.length; i++) {
            if (i == 0) {
                System.out.print(coefficients[i]);
            } else {
                System.out.print(" + " + coefficients[i] + " * x" + i);
            }
        }
        System.out.println();
    }

    public void predict(double[] x) {
        double y = coefficients[0];
        for (int i = 1; i < coefficients.length; i++) {
            y += coefficients[i] * x[i - 1];
        }
        System.out.println("Predicción para x = " + Arrays.toString(x) + " es y = " + y);
    }
}
