package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet10 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet10() {
        x = new float[]{9735033990f, 9481803274f, 9198847240f, 8887524213f, 8548487400f, 8184437460f, 7794798739f, 7713468100f, 7631091040f, 7547858925f, 7464022049f, 7379797139f, 6956823603f, 6541907027f, 6143493823f, 5744212979f, 5327231061f, 4870921740f, 4458003514f, 4079480606f, 3700437046f, 3339583597f, 3034949748f, 2773019936f};
        y = new float[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    }

    public DataSet10(float x[], float y[]){
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