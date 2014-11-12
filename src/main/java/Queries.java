import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Queries
{
    private static DatabaseConnection dbConnection;
    private static Connection connection;

    public Queries(DatabaseConnection dbConnection,Connection connection)
    {
        this.dbConnection = dbConnection;
        this.connection = connection;
    }

    /*
    This method returns the List of ArrayList containing coordinates of each building
     */
    protected List<ArrayList<Integer>> getBuilding()
    {
        String buildingGeo = "select geo from building";
        List<ArrayList<Integer>> allBuildingsGeo = new ArrayList<ArrayList<Integer>>();
        List<Integer> coordinateList;

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(buildingGeo);
            while(rs.next())
            {
                STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
                JGeometry j_geom = JGeometry.load(st);
                double[] coordinates = j_geom.getOrdinatesArray();
                coordinateList = convertToIntegerList(coordinates);
                allBuildingsGeo.add((ArrayList<Integer>) coordinateList);
            }
            statement.close();
        }catch (SQLException e)
        {
            System.out.println("Error while retrieving data from building table");
            e.printStackTrace();
        }
        dbConnection.closeConnection();
        return allBuildingsGeo;
    }


    /*
    This method converts the double array of ordinates received from database into int arraylist
     */
    protected List<Integer> convertToIntegerList(double[] coordinates)
    {
        List<Integer> points = new ArrayList<Integer>();
        for (int i = 0; i < coordinates.length; i++)
        {
            Double value = coordinates[i];
            points.add(value.intValue());
        }
        return points;
    }


    /*
    This methods returns an array of xCoordinates or yCoordinates depending upon the index provided
     */
    protected int[] separateCoordinates(List<Integer> coordinateList, int index)
    {
        int[] points = new int[coordinateList.size()/2];
        int indexOfCoordinates = index;
        for(int i = 0; i < points.length; i++)
        {
            points[i] = coordinateList.get(indexOfCoordinates);
            indexOfCoordinates += 2;
        }
        return points;

    }


}
