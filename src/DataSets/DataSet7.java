package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet7 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet7() {
        x = new float[]{38.1f, 36.6f, 35f, 33.3f, 31.7f, 30f, 28.4f, 27.1f, 27.1f, 27.1f, 27.1f, 26.8f, 25.1f, 23.8f, 22.7f, 21.8f, 21.1f, 20.6f, 20.2f, 19.7f, 19.3f, 19.6f, 20.2f, 20.7f};
        y = new float[]{2.24f, 2.24f, 2.24f, 2.24f, 2.24f, 2.24f, 2.24f, 2.36f, 2.36f, 2.36f, 2.36f, 2.4f, 2.8f, 3.14f, 3.48f, 3.83f, 4.27f, 4.68f, 4.97f, 5.41f, 5.72f, 5.89f, 5.9f, 5.9f};
    }
    public DataSet7(float x[], float y[]){
        this.x = x;
        this.y = y;
    }

    public float[] getX(){

        return this.x;
    }

    public float[] getY(){

        return this.y;
    }


}