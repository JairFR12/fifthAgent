package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet5 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet5(){
        x = new float [] {2050, 2045, 2040, 2035, 2030, 2025, 2020, 2019, 2018, 2017, 2016, 2015, 2010, 2005, 2000, 1995, 1990, 1985, 1980, 1975, 1970, 1965, 1960, 1955};
        y = new float [] {0.23f, 0.35f, 0.5f, 0.66f, 0.8f, 0.92f, 1.04f, 1.02f, 1.04f, 1.07f, 1.1f, 1.2f, 1.47f, 1.67f, 1.85f, 1.99f, 2.17f, 2.33f, 2.32f, 2.33f, 2.15f, 2.07f, 1.91f, 1.72f};

    }
    public DataSet5(float x[], float y[]){
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