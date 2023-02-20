package examples.college;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CollegeDatabaseInitialiser
{
    
    private void delDB ( String url, String dbname )
    {
        // DROP DATABASE dbname;
    }
    private void deleteDB ( String url ) throws SQLException
    {
        Connection conn = DriverManager.getConnection( url, "root", "" ) ;

    }

    private Connection connectDB ( String url ) throws ClassNotFoundException, SQLException
    {
        // load JDBC driver com.mysql.jdbc.Driver
        Class.forName ( "com.mysql.jdbc.Driver" ) ;
        
        // connect to XAMPP database
        Connection conn = DriverManager.getConnection( url, "root", "" ) ;
        
        return conn ;
        
    }
    
    private void addUser ( String username, String password )
    {
        // GRANT ALL PRIVILEGES ON *.* TO 'username'@'localhost' IDENTIFIED BY 'password';
        String sqlStmt = "GRANT ALL PRIVILEGES ON *.* TO '" + username + 
                         "'@localhost IDENTIFIED BY '" + password + "'";
        
        
        
    }
    
    private void delUser ( String username )
    {
        // DELETE FROM mysql.user 
        // WHERE  user = 'root' 
        //        AND host = 'localhost';
    }
    
    
    public CollegeDatabaseInitialiser (String dbname ) throws ClassNotFoundException, SQLException
    {
        System.out.println ( "CollegeDatabaseInitialiser> start") ;
        
        // create database, if it doesn't exist yet
        //Connection college = createOrConnectDB ( dbServerURL, dbname ) ;
        
        Connection college = CollegeDBConnector.createOrConnectDB( dbname ) ;
        
        initialiseStudents(college);
        initialiseCourses(college);
        initialiseEnrollments(college);

        college.close();
        
        System.out.println ( "CollegeDatabaseInitialiser> end") ;
    }
    

    public static void main(String[] args)
    {
        System.out.println ( "College> start") ;
        
        System.out.println ( "College> run the initialiser") ;
        try {
            CollegeDatabaseInitialiser college = new CollegeDatabaseInitialiser( "CS3524" ) ;
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println ( "College> database connection not working") ;
            e.printStackTrace();
        }
        System.out.println ( "College> end") ;
    }
    
    
    public void initialiseStudents( Connection conn ) throws SQLException
    {
        String tableName = "Students";
        Statement stmt   = conn.createStatement();
        
        // If the table doesn't exist, this query will throw an
        // SQLException.
    
        try {
        
            System.out.println( "Checking for the existence of table " + tableName + "..." );
        
            stmt.execute( "create table " + tableName + " (" +
                          "SID int, FirstName char(10), " +
                          "LastName char(10))" );
        
            System.out.println( "...new table created in database." );
    
        }
        catch (SQLException e) {
            // Otherwise, empty it.
            System.out.println( "...table exists; deleting all entries..." );
            stmt.execute( "delete from " + tableName );
            System.out.println( "...done." );
        }

        // Now populate the table with the initial data.
    
        System.out.println( "Populating the table..." );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 1234, 'Tony', 'Blair' )" );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 2341, 'Michael', 'Howard' )" );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 3412, 'Charles', 'Kennedy' )" );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 4123, 'Gordon', 'Brown' )" );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 4321, 'Oliver', 'Letwing' )" );
    
        stmt.execute( "insert into " + tableName + " (SID, FirstName, LastName) " +
              "values( 3214, 'Vincent', 'Cable' )" );
    
        System.out.println( "...done." );
    
        stmt.close() ;
    }

    public void initialiseCourses( Connection conn ) throws SQLException
    {
    
        String    tableName = "Courses";
        Statement stmt      =  conn.createStatement();
    
        // Make sure the table exists and that it is empty.
    
        try {
            System.out.println( "Checking for the existence of table " + tableName + "..." );
        
            stmt.execute( "create table " + tableName + " (" +
              "CID int, CourseName char(30), " +
              "Teacher char(20), Day char(10), " +
              "Capacity int)" );
        
            System.out.println( "...new table created in database." );
        }
    
        // Otherwise, empty it.
    
        catch (SQLException e) {
            System.out.println( "...table exists; deleting all entries..." );
            stmt.execute( "delete from " + tableName );
            System.out.println( "...done." );
        }
    
        // Now populate the table with the initial data.
    
        System.out.println( "Populating the table..." );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 1, 'Giving Constructive Criticism', " +
              "'David Blunkett', 'Monday', 5 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 2, 'How to be Popular', " +
              "'Peter Mandleson', 'Tuesday', 3 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 3, 'Diary Management', " +
              "'Jeffrey Archer', 'Friday', 4 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 4, 'Media Management', " +
              "'Boris Johnson', 'Monday', 3 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 5, 'Public Speaking', " +
              "'George W. Bush', 'Thursday', 5 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 6, 'Writing a Manifesto', " +
              "'Michael Foot', 'Wednesday', 4 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 7, 'How to Win', " +
              "'Ian Duncan-Smith', 'Wednesday', 4 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 8, 'Managing the Economy', " +
              "'Norman Lamont', 'Tuesday', 4 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, CourseName, " +
              "Teacher, Day, Capacity) values( 9, 'Team Managment', " +
              "'John Major', 'Tuesday', 4 )" );
    
        System.out.println( "...done." );
    
        stmt.close() ;
    }
    
    public void initialiseEnrollments( Connection conn ) throws SQLException
    {
        String    tableName = "Enrollments";
        Statement stmt      = conn.createStatement();
    
        // Make sure the table exists and that it is empty.
    
        try {
        
            System.out.println( "Checking for the existence of table " + tableName + "..." );
        
            stmt.execute( "create table " + tableName + " (" +
              "CID int, SID int)" );
        
            System.out.println( "...new table created in database." );
    
        }
    
        // Otherwise, empty it.
    
        catch (SQLException e) {
            System.out.println( "...table exists; deleting all entries..." );
            stmt.execute( "delete from " + tableName );
            System.out.println( "...done." );
        }
    
        // Now populate the table with the initial data.
    
        System.out.println( "Populating the table..." );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 1, 2341 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 1, 3412 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 2, 3412 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 3, 4321 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 3, 3214 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 4, 4123 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 5, 3412 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 5, 3214 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 5, 1234 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 5, 2341 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 7, 4123 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 7, 3214 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 8, 3214 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 8, 2341 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 9, 1234 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 9, 4123 )" );
    
        stmt.execute( "insert into " + tableName + " (CID, SID) " +
              "values( 9, 4321 )" );
    
        System.out.println( "...done." );
    
        stmt.close() ;
    }
}
