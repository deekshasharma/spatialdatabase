import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String username = "dsharma";
    private static final String pass = "chulbul6";
    private static Connection dbConnection;


    public DatabaseConnection()
    {
             connectToDatabase();
    }

    /*
   This method creates the data base dbConnection with Oracle 11g database
    */
    private static Connection connectToDatabase() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver missing!");
            e.printStackTrace();
        }
        System.out.println("Oracle JDBC Driver Registered!");

        try {

            dbConnection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:db11g", username,
                    pass);


        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        if (dbConnection != null) {
            System.out.println("Database connected");
        } else {
            System.out.println("Failed to connect the database");
        }
        return dbConnection;

    }

    /*
    This method closes the Connection to the database
     */
    protected void closeConnection()
    {
        try {
            dbConnection.close();
            System.out.println("Closing the connection");
        } catch (SQLException e) {
            System.out.println("Error in closing connection");
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return dbConnection;
    }

}
