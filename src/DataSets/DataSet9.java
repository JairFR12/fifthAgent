package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet9 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet9() {
        x = new float[]{876613025, 811749463, 744380367, 675456367, 607341981, 542742539, 483098640, 471828295, 460779764, 449963381, 439391699, 429069459, 380744554, 334479406, 291350282, 255558824, 222296728, 190321782, 160941941, 132533810, 109388950, 93493844, 80565723, 71958495};
        y = new float[]{16.84f, 17.09f, 17.31f, 17.48f, 17.59f, 17.66f, 17.7f, 17.71f, 17.73f, 17.74f, 17.75f, 17.75f, 17.74f, 17.54f, 17.2f, 16.78f, 16.39f, 16.1f, 15.68f, 15.27f, 15f, 14.95f, 14.85f, 14.78f};
    }

    public DataSet9(float x[], float y[]){
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