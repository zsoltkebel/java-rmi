package examples.college;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CollegeDBConnector
{
    static final String JDBCDriver  = "com.mysql.jdbc.Driver"   ;
    static final String dbServerURL = "jdbc:mysql://localhost/" ;

    static public Connection createOrConnectDB ( String dbname ) throws ClassNotFoundException, SQLException
    {
        System.out.println ( "createDB> create database " + dbname ) ;
        
        Connection conn = null ;
        
        // CREATE DATABASE dbname;
        // load JDBC driver com.mysql.jdbc.Driver
        Class.forName ( "com.mysql.jdbc.Driver" ) ;

        // this is a test whether the database already exists
        try {
            conn = DriverManager.getConnection( dbServerURL + dbname, "root", "" ) ;
            System.out.println ( "College> database already exists.") ;
            //conn.close();
        }
        catch ( SQLException e) {
            // note that if the database doesn't exist, we land here in the exception handling
            // where we then try to create the database
            try {
                
                String sqlStmt = "CREATE DATABASE " + dbname ;
             
                // connect to XAMPP database
                conn = DriverManager.getConnection( dbServerURL, "root", "" ) ;
                Statement stmt  = conn.createStatement() ;
                stmt.executeUpdate( sqlStmt ) ;
                //conn.close();
                System.out.println ( "createDB> create database " + dbname + " done." ) ;
            }
            catch ( SQLException e1)
            {
                System.out.println ( "College> database creation failed") ;
                e.printStackTrace();
                // again throw SQLException to make this error known to the calling method
                throw new SQLException() ;
            }

       }
        return conn;   
    }


}
