package regresionMultiple;

public class Matrix {
    public final double[][] data;

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
        // Aquí se implementa la solución del sistema de ecuaciones
        // Esta es una implementación muy simplificada utilizando el método de eliminación de Gauss-Jordan

        // Suponiendo que `data` es una matriz cuadrada y `other` tiene una columna
        int n = data.length;
        double[][] augmentedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(data[i], 0, augmentedMatrix[i], 0, n);
            augmentedMatrix[i][n] = other.data[i][0];
        }

        // Aplicar eliminación Gaussiana
        for (int i = 0; i < n; i++) {
            // Buscar el máximo en la columna i
            double maxEl = Math.abs(augmentedMatrix[i][i]);
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmentedMatrix[k][i]) > maxEl) {
                    maxEl = Math.abs(augmentedMatrix[k][i]);
                    maxRow = k;
                }
            }

            // Intercambiar filas
            double[] temp = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = augmentedMatrix[i];
            augmentedMatrix[i] = temp;

            // Hacer que el elemento de la diagonal sea 1
            double diagElement = augmentedMatrix[i][i];
            for (int j = 0; j < n + 1; j++) {
                augmentedMatrix[i][j] /= diagElement;
            }

            // Hacer que todos los elementos en la columna i sean 0, excepto el de la diagonal
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmentedMatrix[k][i];
                    for (int j = 0; j < n + 1; j++) {
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        // Extraer la solución de la matriz aumentada
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
