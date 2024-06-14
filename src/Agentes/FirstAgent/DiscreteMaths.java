package Agentes.FirstAgent;

public class DiscreteMaths {
    public float sumX(float x[]) {
        float totalX = 0;

        for (int i = 0; i < x.length; i++)
            totalX = totalX + x[i];
        return totalX;
    }

    public float sumY(float y[]) {
        float totalY = 0;
        for (int i = 0; i < y.length; i++)
            totalY = totalY + y[i];
        return totalY;
    }

    public float sumXY(float x[], float y[]) {
        float totalXY = 0;
        for (int i = 0; i < x.length; i++)
            totalXY = totalXY + (x[i] * y[i]);
        return totalXY;
    }

    public float multiplySum(float x[], float y[]) {
        return sumX(x) * sumY(y);
    }

    public float xSquared(float x[]) {
        float xSquared = 0;
        for (int i = 0; i < x.length; i++) {
            x[i] = x[i] * x[i];
            xSquared = x[i];
        }
        return xSquared;
    }

    public float totalXSquared(float x[]) {
        float total = 0;
        xSquared(x);
        for (int i = 0; i < x.length; i++) {
            total = total + x[i];
        }
        return total;
    }

    float totalXY(float[] x, float[] y) {
        float totalxy = 0;
        float[] z = new float[x.length];
        for (int i = 0; i < x.length; i++) {
            z[i] = x[i] * y[i];
        }
        for (int i = 0; i < z.length; i++) {
            totalxy = totalxy + z[i];
        }
        return totalxy;
    }
}
