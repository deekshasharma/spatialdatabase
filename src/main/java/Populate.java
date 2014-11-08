import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class Populate
{
    private static final String username = "dsharma";
    private static final String pass = "chulbul6";
    private static Connection connection;
    private static final String filePathPhotographer = "/Users/deeksha/Desktop/photographer.xy";
    private static final String filePathPhoto = "/Users/deeksha/Desktop/photo.xy";
    private static final String filePathBuilding = "/Users/deeksha/Desktop/building.xy";

    public static void main(String[] args)
    {
        connectToDatabase();
//        populateDatabase();


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


    /*
    This method populates the database tables
     */
    private static void populateDatabase()
    {

//        readFile(filePathPhotographer);
//        readFile(filePathPhoto);
        readFile(filePathBuilding);
//        try
//        {
//            connection.close();
//            System.out.println("Closing the connection");
//        }catch(SQLException e)
//        {
//            System.out.println("Error in closing connection");
//            e.printStackTrace();
//        }
    }



    private static String[] readFile(String filePath)
    {
        String line = "";
        String[] eachRow = null;
//        String filepath = "/Users/deeksha/Desktop/photographer.xy";
        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while((line = br.readLine()) != null)
            {
//                photographerData =  line.split("\\s*(=>|,|\\s)\\s*");
                if(filePath.equalsIgnoreCase("filePathPhotographer"))
                    eachRow =  line.split(",\\s*");
//                populatePhotographer(eachRow);
            }
            try{connection.commit();}catch (SQLException e){
                System.out.println("Error due to commit");}

            br.close();
        }catch (IOException e)
        {
            System.out.println("Error in reading file");
            e.printStackTrace();
        }

        return eachRow;

    }

    private static void populatePhotographer(String[] photographerData)
    {

        String  photographerId = photographerData[0];
        int xCoordinate = Integer.parseInt(photographerData[1]);
        int yCoordinate = Integer.parseInt(photographerData[2]);
        String insert = "insert into photographer values("+"'"+photographerId+"'"+","+"MDSYS.SDO_POINT_TYPE("+
                xCoordinate+","+yCoordinate+","+"null"+"))";
        System.out.println(insert);
//        try
//        {
//            Statement statement = connection.createStatement();
//            statement.executeUpdate(insert);
//            connection.commit();
//            System.out.println("Inserted record in the table");
//        }catch(SQLException e)
//        {
//            System.out.println("Error due to executing query");
//            e.printStackTrace();
//        }
    }

}
