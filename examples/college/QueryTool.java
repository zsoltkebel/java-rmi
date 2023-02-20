package examples.college;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryTool {
    private Connection conn = null ;
    
    public QueryTool ( String dbname ) throws ClassNotFoundException, SQLException {
        System.out.println ( "QueryTool> start") ;
        
        // create database, if it doesn't exist yet
        //Connection college = createOrConnectDB ( dbServerURL, dbname ) ;
        
        this.conn = CollegeDBConnector.createOrConnectDB( dbname ) ;
        
        commandLine() ;

        conn.close();
        
        System.out.println ( "QueryTool> end") ;
        
    }

    public static void main(String[] args) {
        System.out.println ( "QueryTool> start") ;
        
        try {
            
            QueryTool query = new QueryTool( "CS3524" ) ;
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println ( "College> database connection not working") ;
            e.printStackTrace();
        }
        System.out.println ( "College> end") ;
    }
    
    private void commandLine() throws SQLException {
        BufferedReader in = new BufferedReader( new InputStreamReader( System.in ));
        
        char sel;
        // Prompt the user for a query type to be performed.
        try {
            do {
            System.out.println( "\n\n\n\n\nWelcome to the student information management system." +
                        "\nPlease select one of the following options:\n\n" +
                        "\t s\t View a student record.\n\n" + 
                        "\t c\t View a course record.\n\n" +
                        "\t e\t Enroll a student on a course.\n\n" +
                        "\t q\t Quit.\n\n" );
            System.out.print( "Enter [s/c/e/q]: " );
            sel = in.readLine().charAt(0);
            int cid, sid;
            switch (sel) {
            case 's':
                System.out.print( "Enter the student ID: " );
                sid = Integer.parseInt( in.readLine() );
                displayStudent( sid );
                System.out.print( "Press return to continue..." );
                in.readLine();
                break;
            case 'c':
                System.out.print( "Enter the Course ID: " );
                cid = Integer.parseInt( in.readLine() );
                displayCourse( cid );
                System.out.print( "Press return to continue..." );
                in.readLine();
                break;
            case 'e':
                System.out.print( "Enter the Student ID: " );
                sid = Integer.parseInt( in.readLine() );
                System.out.print( "Enter the Course ID: " );
                cid = Integer.parseInt( in.readLine() );
                //enrollStudent( sid, cid );
                displayStudent( sid );
                System.out.print( "Press return to continue..." );
                in.readLine();
                break;
                // Need to check both that the student is not
                // registered for a course on the same day and
                // that the course is not at capacity before
                // committing an enrollment.
            default:
            }
            } while ( sel != 'q' );
        }
        catch (IOException e) {
            System.err.println( "Failure to parse user response; exiting..." );
        }
        catch (StringIndexOutOfBoundsException e) {
            System.err.println( "You need to enter a valid option; exiting..." );
        }
    }
    
    public void displayStudent(int sid) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs;
        String sql;

        sql = "select FirstName, LastName from " + "Students" +
              " where SID = " + sid;
        rs = stmt.executeQuery( sql );

        // Move cursor to the first entry (there should only be one
        // entry).  If there is no first entry in the ResultSet, then
        // the student does not exist.
        if (rs.next()) {
            String firstname = (String)rs.getObject( "FirstName" );
            String lastname = (String)rs.getObject( "LastName" );
            System.out.println( "\nStudent record for:\n" + sid + ": "
                    + firstname + lastname );
        }
        else {
            System.out.println( "Student does not exist." );
            return;
        }

        // Now obtain the list of courses that the student is
        // registered for.
        sql = "select " + "Courses" + ".CID, " + "Courses" + ".CourseName, " + "Courses" + ".Teacher " +
            "from " + "Courses" + ", " + "Enrollments" +
            " where " +
            "Enrollments" + ".SID = " + sid + " and " +
            "Enrollments" + ".CID = " + "Courses" + ".CID";
        rs = stmt.executeQuery( sql );
        System.out.println( "\nCourses for which the student is registered:" );
        displayResultSet( rs );

        stmt.close();
    }
    
    public void displayCourse(int cid) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs;
        String sql;

        sql = "select Teacher, CourseName, Day, Capacity from " + "Courses" +
            " where CID = " + cid;
        rs = stmt.executeQuery( sql );
        
        // Move cursor to the first entry (there should only be one
        // entry).  If there is no first entry in the ResultSet, then
        // the course does not exist.
        if (rs.next()) {
            String teacher = rs.getString( "Teacher" );
            String course = rs.getString( "CourseName" );
            String day = rs.getString( "Day" );
            int capacity = rs.getInt( "Capacity" );
            System.out.println( "\n" + cid + ": " + course + "\nby\n" + teacher +
                                "\nSessions each " + day + "\nClass capacity = " +
                                capacity + "\n" );
        }
        else {
            System.out.println( "Course does not exist." );
            return;
        }

        // Now obtain the list of students registered for this course
        // by querying the enrollments and student tables, and display
        // the results using the generic displayResultSet method.
        sql = "select " +
            "Students" + ".SID, " + "Students" + ".FirstName, " +
            "Students" + ".LastName " +
            "from " +
            "Students" + ", " + "Enrollments" +
            " where " +
            "Enrollments" + ".CID = " + cid + " and " +
            "Enrollments" + ".SID = " + "Students" + ".SID";
        rs = stmt.executeQuery( sql );
        System.out.println( "\nClass list:" );
        displayResultSet( rs );

        stmt.close();
    }
    
    public void displayResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData() ;
        int cols = rsmd.getColumnCount() ;
        System.out.println( "------------------------------------------------------------" ) ;
        for (int i = 1; i <= cols; i++)
            printField( rsmd.getColumnName( i ) );
        System.out.println( "\n------------------------------------------------------------" ) ;
        while( rs.next() ) {
            for (int i = 1; i <= cols; i++)
            printField( rs.getString( i ) );
            System.out.println();
        }
        System.out.println( "------------------------------------------------------------\n" ) ;
    }

    public static void printField( String name ) {
        if (name == null)
            name = "NULL";
        System.out.print( name + "\t" );
    }
}
