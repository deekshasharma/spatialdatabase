import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Queries {
    private static DatabaseConnection dbConnection;
    private static Connection connection;

    public Queries(DatabaseConnection dbConnection, Connection connection) {
        this.dbConnection = dbConnection;
        this.connection = connection;
    }

//    public static void main(String[] args) {
//        dbConnection = new DatabaseConnection();
//        connection = dbConnection.getConnection();
//        Queries queries = new Queries();
//        List<ArrayList<Integer>> photo = queries.getAllBuildingGeo();
//        dbConnection.closeConnection();
//        System.out.println(photo);
//
//    }

    /*
    This method returns the List of ArrayList containing coordinates of each building
     */
    protected List<ArrayList<Integer>> getAllBuildingGeo() {
        String buildingGeo = "select GEO from building";
        List<ArrayList<Integer>> allBuildingsGeo = new ArrayList<ArrayList<Integer>>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(buildingGeo);
            allBuildingsGeo = processResultSet(rs,"building");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Building table");
            e.printStackTrace();
        }
//        System.out.println(allBuildingsGeo);
        return allBuildingsGeo;
    }


    /*
    This method converts the double array of ordinates received from database into int arraylist
     */
    protected List<Integer> convertToIntegerList(double[] coordinates) {
        List<Integer> points = new ArrayList<Integer>();
        for (int i = 0; i < coordinates.length; i++) {
            Double value = coordinates[i];
            points.add(value.intValue());
        }
        return points;
    }

    /*
     This method returns the List of ArrayList containing coordinates of all photos
     */
    protected List<ArrayList<Integer>> getAllPhotoGeo()
    {
        List<ArrayList<Integer>> allPhotoGeo = new ArrayList<ArrayList<Integer>>();
        String photoGeo = "select PHOTOCOORDINATES from photo";

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(photoGeo);
            allPhotoGeo = processResultSet(rs,"photo");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Photo table");
            e.printStackTrace();
        }
        return allPhotoGeo;
    }

    /*
     This method returns the List of ArrayList containing location of all photographers
     */
    protected List<ArrayList<Integer>> getAllPhotographerGeo()
    {
        List<ArrayList<Integer>> allPhotographerGeo = new ArrayList<ArrayList<Integer>>();
        String photographerGeo = "select PHOTOGRAPHERLOC from photographer";

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(photographerGeo);
            allPhotographerGeo = processResultSet(rs,"photographer");
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error while retrieving data from Photographer table");
            e.printStackTrace();
        }
        return allPhotographerGeo;
    }

    /*
    This method takes the ResultSet as input and returns the combined list of all coordinates for the tables.
     */
    private List<ArrayList<Integer>> processResultSet(ResultSet rs, String tableName)
    {
        List<Integer> coordinateList ;
        List<ArrayList<Integer>> allCoordinates = new ArrayList<ArrayList<Integer>>();

        try{
            while (rs.next()) {
                STRUCT st = (oracle.sql.STRUCT) rs.getObject(1);
                JGeometry j_geom = JGeometry.load(st);
                if(tableName.equalsIgnoreCase("building"))
                {
                    double[] coordinates = j_geom.getOrdinatesArray();
                    coordinateList = convertToIntegerList(coordinates);
                    allCoordinates.add((ArrayList<Integer>) coordinateList);
                }else if (tableName.equalsIgnoreCase("photo") || tableName.equalsIgnoreCase("photographer"))
                {
                    double[] coordinates = j_geom.getPoint();
                    coordinateList = convertToIntegerList(coordinates);
                    allCoordinates.add((ArrayList<Integer>) coordinateList);
                }
            }
        } catch(SQLException e)
        {
            System.out.println("Error while processing ResultSet");
            e.printStackTrace();
        }
        return allCoordinates;

    }

    /*
    This methods returns an array of xCoordinates or yCoordinates depending upon the index provided
     */
    protected int[] separatePolyCoordinates(List<Integer> coordinateList, int index) {
        int[] points = new int[coordinateList.size() / 2];
        int indexOfCoordinates = index;
        for (int i = 0; i < points.length; i++) {
            points[i] = coordinateList.get(indexOfCoordinates);
            indexOfCoordinates += 2;
        }
        return points;

    }




}
