import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class Populate {

    private static DatabaseConnection dbConnection = new DatabaseConnection();
    private static Connection connection = dbConnection.getConnection();

    public static void main(String[] args) {
        if (args.length > 0) {
            String photographer = args[0];
            String photo = args[1];
            String building = args[2];
//            populateDatabase(building);
//            populateDatabase(photographer);
            populateDatabase(photo);

        } else {
            System.out.println("Please enter the Command line arguments");

        }
    }


    /*
    This method reads the data files and populates the tables
     */
    private static void populateDatabase(String filePath) {
        String line = "";
        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                if (filePath.contains("photographer.xy")) {
                    String[] eachRow = line.split(",\\s*");
                    executePhotographer(eachRow);
                } else if (filePath.contains("photo.xy")) {
                    String[] eachRow = line.split(",\\s*");
                    populatePhoto(eachRow);
                } else if (filePath.contains("building.xy")) {
                    String[] eachRow = line.split(",\\s*", 4);
                    populateBuilding(eachRow);
                }
            }
            br.close();
            try {
                connection.commit();
            } catch (SQLException e) {
                System.out.println("Error due to commit");
            }
        } catch (IOException e) {
            System.out.println("Error in reading file");
            e.printStackTrace();
        }
        try {
            connection.close();
            System.out.println("Closing the connection");
        } catch (SQLException e) {
            System.out.println("Error in closing connection");
            e.printStackTrace();
        }
    }

    /*
    This method build and execute the query for photographer table
     */
    private static void executePhotographer(String[] photographerData) {

        String photographerId = photographerData[0];
        int xCoordinate = Integer.parseInt(photographerData[1]);
        int yCoordinate = Integer.parseInt(photographerData[2]);
        String insert = "insert into photographer values(" + "'" + photographerId + "'" + ","
                + "MDSYS.SDO_GEOMETRY(2001,null" + ","
                + "MDSYS.SDO_POINT_TYPE("
                + xCoordinate + ","
                + yCoordinate + ","
                + "null)," +
                "null,null))";
        System.out.println(insert);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            System.out.println("Inserted record in the table");
        } catch (SQLException e) {
            System.out.println("Error due to executing query");
            e.printStackTrace();
        }
    }


    /*
    This method builds and execute the insert queries for photo table
     */
    private static void populatePhoto(String[] eachRow) {
        String photoId = eachRow[0];
        String photographerId = eachRow[1];
        int xCoordinate = Integer.parseInt(eachRow[2]);
        int yCoordinate = Integer.parseInt(eachRow[3]);
        String insert = "insert into photo values(" + "'" + photoId + "'" + ","
                + "'" + photographerId + "'" + ","
                + "MDSYS.SDO_GEOMETRY(2001,null" + ","
                + "MDSYS.SDO_POINT_TYPE("
                + xCoordinate + ","
                + yCoordinate + ","
                + "null)," +
                "null,null))";
        System.out.println(insert);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            System.out.println("Inserted record in Photo table");
        } catch (SQLException e) {
            System.out.println("Error due to executing query");
            e.printStackTrace();
        }
    }


    /*
     This method builds and execute the insert queries for building table
      */
    private static void populateBuilding(String[] eachRow) {
        String buildingID = eachRow[0];
        String buildingName = eachRow[1];
        int numOfVertices = Integer.parseInt(eachRow[2]);
        String coordinates = eachRow[3];
        String[] lastCoordinates = coordinates.split(",\\s*", 3);
        String x1 = lastCoordinates[0];
        String y1 = lastCoordinates[1];


        String insert = ("insert into building values(" + "'" + buildingID + "'" + "," +
                "'" + buildingName + "'" + ","
                + numOfVertices + ","
                + "MDSYS.SDO_GEOMETRY(" + "2003" + ","
                + "null" + ","
                + "null" + ","
                + "SDO_ELEM_INFO_ARRAY(1,1003,1)" + ","
                + "MDSYS.SDO_ORDINATE_ARRAY(" + coordinates + ","
                + x1 + ","
                + y1 +
                ")))"
        );

        System.out.println(insert);
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            System.out.println("Inserted record in Building table");
        } catch (SQLException e) {
            System.out.println("Error due to executing query");
            e.printStackTrace();
        }

    }


}
