package DataSets;

import Agentes.SecondAgent.DataSetBase;

public class DataSet6 extends DataSetBase {

    private float x[];
    private float y[];

    public DataSet6() {
        x = new float[]{3711367, 5585537, 7793541, 10016298, 11726140, 13001447, 13970396, 13775474, 13965495, 14159536, 14364846, 15174247, 17334249, 18206876, 18530592, 18128958, 17783558, 17081433, 15169989, 13582621, 11213294, 9715129, 8133417, 6711079};
        y = new float[]{0, -414772, -415736, -415732, -440124, -464081, -532687, -532687, -532687, -532687, -532687, -470015, -531169, -377797, -136514, -110590, 9030, 115942, 222247, 421208, -68569, -17078, -30805, -21140};
    }
    public DataSet6(float x[], float y[]){
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