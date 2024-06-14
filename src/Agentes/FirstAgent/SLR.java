package Agentes.FirstAgent;

public class SLR {
    private float beta0;
    private float beta1;
    private float[] x;
    private float[] y;
    private DiscreteMaths discreteMaths;

    public SLR(float[] x, float[] y, DiscreteMaths discreteMaths) {
        beta0 = 0;
        beta1 = 0;
        this.x = x;
        this.y = y;
        this.discreteMaths = discreteMaths;
    }

    public void calculateIntersection() {
        int n = x.length;

        // Calcular la suma de los valores de x e y
        float sumX = discreteMaths.sumX(x);
        float sumY = discreteMaths.sumY(y);

        // Calcular el promedio de los valores de x e y
        float meanX = sumX / n;
        float meanY = sumY / n;

        // Obtener el valor de la pendiente (beta1)
        beta1 = calculateSlope(); // Actualizar el valor de beta1 en la clase

        // Calcular la intersección beta0
        beta0 = meanY - beta1 * meanX;

        System.out.println("Intersección (beta0): " + beta0);
    }

    public float calculateSlope() {
        int n = x.length;

        // Calcular la suma de x*y y las sumas de x e y
        float sumXY = discreteMaths.totalXY(x, y);
        float sumX = discreteMaths.sumX(x);
        float sumY = discreteMaths.sumY(y);

        // Calcular la suma de los cuadrados de x
        float sumXSquared = discreteMaths.totalXSquared(x);

        // Calcular la pendiente (beta1) utilizando la fórmula de la regresión lineal
        beta1 = (n * sumXY - sumX * sumY) / (n * sumXSquared - sumX * sumX);

        System.out.println("Pendiente (beta1): " + beta1);
        System.out.println("Ecuación de la Línea de Regresión: y = " + beta0 + " + " + beta1 + "x");
        return beta1;
    }

    public void printRegEquation() {
        System.out.println("Ecuación de la Línea de Regresión: y = " + beta0 + " + " + beta1 + "x");
    }

    public void predict(float x) {
        // Utiliza los valores de beta0 y beta1 para predecir y para un valor x dado
        float predictedY = beta0 + beta1 * x;
        System.out.println("Para x = " + x + ", la predicción de y es: " + predictedY);
    }

    public void predictAdvertisingSales() {
        // Realizar e imprimir predicciones para Sales dados los valores de Advertising: 20, 56, 60
        predict(20);
        predict(56);
        predict(60);
    }
}
