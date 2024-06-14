package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet4 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet4(){
       x= new float[] {18, 22, 23, 26, 28, 31, 33};
        y=new float[] {10000, 15000, 18000, 21000, 24000, 26500, 27000};

    }
    public DataSet4(float x[], float y[]){
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