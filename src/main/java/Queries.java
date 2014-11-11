import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queries
{
    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection connection = dbConnection.getConnection();

    public static void main(String[] args)
    {
        Queries q = new Queries();
//        q.getBuilding();
        dbConnection.closeConnection();
        double[] all = new double[4];
        all[0] =   337.0;
        all[1] = 209.0;
        all[2] = 389.0;
        all[3] = 236.0;
        List<Double> x = q.separateCoordinates(all, 1);
        System.out.println(x);

                //[337.0, 209.0, 389.0, 236.0, 385.0, 242.0, 390.0, 244.0,
    }

    private void getBuilding()
    {
        String buildingGeo = "select geo from building where buildingcode ='b2'";
        try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(buildingGeo);

            rs.next();
            STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
            JGeometry j_geom = JGeometry.load(st);
            double[] coordinates =  j_geom.getOrdinatesArray();
//            double[] xPoly =
            System.out.println(Arrays.toString(j_geom.getOrdinatesArray()));
            statement.close();

        }catch (SQLException e)
        {
            System.out.println("Error while retrieving data from building table");
            e.printStackTrace();
        }
    }

    private List<Double> separateCoordinates(double[] coordinates, int index)
    {
        List<Double> points = new ArrayList<Double>();
        for(int i = index; i < coordinates.length; i = i+2)
        {
            points.add(coordinates[i]);
        }
        return points;
    }

}
