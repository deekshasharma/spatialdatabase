import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class Populate {
    private static final String username = "dsharma";
    private static final String pass = "chulbul6";
    private static Connection connection;


    public static void main(String[] args) {
        connectToDatabase();
        String photographerFilePath = args[0];
        String photoFilePath = args[1];
//        populateDatabase(photographerFilePath);
        populateDatabase(photoFilePath);
    }

    /*
    This method creates the data base connection with Oracle 11g database
     */
    private static void connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver missing!");
            e.printStackTrace();
        }
        System.out.println("Oracle JDBC Driver Registered!");

        try {

            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:db11g", username,
                    pass);


        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        if (connection != null) {
            System.out.println("Database connected");
        } else {
            System.out.println("Failed to connect the database");
        }
    }


    private static void populateDatabase(String filePath) {
        String line = "";
        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null)
            {
                String[] eachRow = line.split(",\\s*");
                if (filePath.contains("photographer.xy")) {
                    populatePhotographer(eachRow);
                } else if (filePath.contains("photo.xy")) {
                    populatePhoto(eachRow);
                } else {
                    populateBuilding(eachRow);
                }
            }
            br.close();
            try{connection.commit();}catch (SQLException e){
                System.out.println("Error due to commit");}
        } catch (IOException e) {
            System.out.println("Error in reading file");
            e.printStackTrace();
        }
        try
        {
            connection.close();
            System.out.println("Closing the connection");
        }catch(SQLException e)
        {
            System.out.println("Error in closing connection");
            e.printStackTrace();
        }
    }

    /*
    This method build and execute the query for photographer table
     */
    private static void populatePhotographer(String[] photographerData) {

        String photographerId = photographerData[0];
        int xCoordinate = Integer.parseInt(photographerData[1]);
        int yCoordinate = Integer.parseInt(photographerData[2]);
        String insert = "insert into photographer values(" + "'" + photographerId + "'" + "," + "MDSYS.SDO_POINT_TYPE(" +
                xCoordinate + "," + yCoordinate + "," + "null" + "))";
        System.out.println(insert);
        try
        {
            Statement statement = connection.createStatement();
            statement.executeUpdate(insert);
            System.out.println("Inserted record in the table");
        }catch(SQLException e)
        {
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
        String insert = "insert into photo values(" + "'" + photoId + "'" + "," + "'" + photographerId + "'" + "," + "MDSYS.SDO_POINT_TYPE(" +
                xCoordinate + "," + yCoordinate + "," + "null" + "))";
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

    private static void populateBuilding(String[] eachRow)
    {

    }


}
