package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet8 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet8(){
        x = new float[]  {551, 545, 536, 523, 506, 486, 464, 460, 455, 450, 445, 441, 415, 386, 355, 324, 294, 264, 235, 210, 187, 168, 152, 138};
        y = new float[] {53.5f, 50.1f, 46.7f, 43.5f, 40.4f, 37.6f, 35f, 34.5f, 34.1f, 33.6f, 33.2f, 32.7f, 30.8f, 29.1f, 27.6f, 26.5f, 25.5f, 24.3f, 23f, 21.3f, 19.7f, 18.7f, 17.9f, 17.6f};
    }
    public DataSet8(float x[], float y[]){
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