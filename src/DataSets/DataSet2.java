package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet2 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet2(){
      x =   new float[]  {1.2f, 1.4f, 1.6f, 2.1f, 2.3f, 3f, 3.1f, 3.3f, 3.3f, 3.8f, 4f, 4.1f, 4.1f, 4.2f, 4.6f, 5f, 5.2f, 5.4f, 6f, 6.1f, 6.9f, 7.2f, 8f, 8.3f, 8.8f, 9.1f, 9.6f, 9.7f, 10.4f, 10.6f};
      y = new float[] {39344, 46206, 37732, 43526, 39892, 56643, 60151, 54446, 64446, 57190, 63219, 55795, 56958, 57082, 61112, 67939, 66030, 83089, 81364, 93941, 91739, 98274, 101303, 113813, 109432, 105583, 116970, 112636, 122392, 121873};

    }
    public DataSet2(float x[], float y[]){
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