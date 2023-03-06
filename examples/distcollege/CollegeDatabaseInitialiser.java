package examples.distcollege;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sets up the database table that captures information about students
 * to be used by QueryTool and the courses and enrollments tables (two
 * of each) to be used by the two instances of CollegeImpl
 * (representing the Millbank and VictoriaStreet colleges.
 * 
 * <p>
 * The relations are:
 * <ul>
 * <li>students( SID, FirstName, LastName )
 * <li>courses( CID, Teacher, Day, Capacity )
 * <li>enrollments( SID, CID )
 * </ul>
 * 
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class CollegeDatabaseInitialiser {
	Connection conn;

	/**
	 * Load the database driver, create the database initialiser and
	 * populate the students table.
	 */
	public static void main(String args[]) {
		try {
			if (args.length < 5) {
				System.err.println(
						"Usage: java StudentDatabaseInitialiser <driver> <URL> <databse-name> <database-username> <database-password>");
				return;
			}
			// Load the database driver as specified in the
			// command-line arguments.
			Class.forName(args[0]);
			// Create the initialiser.
			CollegeDatabaseInitialiser coll = new CollegeDatabaseInitialiser(args[1], args[2], args[3], args[4]);
			// Initialise the tables.
			coll.initialiseStudents();
			coll.initialiseMillbankCourses();
			coll.initialiseVictoriaStreetCourses();
			coll.initialiseMillbankEnrollments();
			coll.initialiseVictoriaStreetEnrollments();
			coll.close();
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load driver.");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			return;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * Record the username and database URL, and establish a
	 * connection to the database.
	 */
	public CollegeDatabaseInitialiser(String url, String dbname, String dbuname, String dbpasswd) throws SQLException {
		
		// try to get database if exists
		try {
			conn = DriverManager.getConnection(url + dbname, dbuname, dbpasswd);
		} catch (SQLException e) {
			System.out.println("creating database");
			conn = DriverManager.getConnection(url, dbuname, dbpasswd);

			String sqlStmt = "CREATE DATABASE " + dbname;

			// connect to XAMPP database
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sqlStmt);
			conn.close();

			System.out.println("createDB> create database " + dbuname + " done.");
			conn = DriverManager.getConnection(url + dbname, dbuname, dbpasswd);
		}
	}

	/**
	 * Initialise the students table. Students( StudentID, FirstName,
	 * LastName )
	 */
	public void initialiseStudents()
			throws SQLException {
		// To ensure that you geneate a unique table name by
		// prepending it with your username.
		String table = "Students";

		// Make sure the table exists and is empty.
		clearTable(table, "(SID int, FirstName char(10), LastName char(10))");

		// Now populate the table with the initial data using the
		// batch method combined with a prepared statement. This is
		// more efficient that the method used in the previous example
		// because only one database statement is executed for the
		// entire batch rather than one for each insertion, minimising
		// the database access overheads, and through the use of a
		// prepared statement, the SQL string is parsed only once
		// rather than once for each insertion.

		// Set up the prepared statement.
		PreparedStatement pstmt = conn.prepareStatement("insert into " + table +
				" (SID, FirstName, LastName)" +
				" values( ?, ?, ? )");

		System.out.println("Batching insertions for the table...");
		addStudentToBatch(pstmt, 1234, "Tony", "Blair");
		addStudentToBatch(pstmt, 2341, "Michael", "Howard");
		addStudentToBatch(pstmt, 3412, "Charles", "Kennedy");
		addStudentToBatch(pstmt, 4123, "Gordon", "Brown");
		addStudentToBatch(pstmt, 4321, "Oliver", "Letwing");
		addStudentToBatch(pstmt, 3214, "Vincent", "Cable");
		System.out.println("...executing the batched insertions...");
		pstmt.executeBatch();
		System.out.println("...done.");
	}

	void addStudentToBatch(PreparedStatement pstmt,
			int sid,
			String firstName,
			String lastName)
			throws SQLException {
		pstmt.clearParameters();
		pstmt.setInt(1, sid);
		pstmt.setString(2, firstName);
		pstmt.setString(3, lastName);
		pstmt.addBatch();
	}

	/**
	 * Initialise the MillbankCourses table. MillbankCourses( CID,
	 * CourseName, Teacher, Day, Capacity )
	 */
	public void initialiseMillbankCourses()
			throws SQLException {
		String table = "MillbankCourses";

		clearTable(table, "(CID int, CourseName char(30), Teacher char(20), Day char(10), Capacity int)");

		// Set up the prepared statement.
		PreparedStatement pstmt = conn.prepareStatement("insert into " + table +
				" (CID, CourseName, Teacher, Day, Capacity)" +
				" values( ?, ?, ?, ?, ? )");

		System.out.println("Batching insertions for the table...");
		addCourseToBatch(pstmt, 1, "Giving Constructive Criticism", "David Blunkett", "Monday", 5);
		addCourseToBatch(pstmt, 2, "How to be Popular", "Peter Mandleson", "Tuesday", 3);
		addCourseToBatch(pstmt, 5, "Public Speaking", "George W. Bush", "Thursday", 5);
		addCourseToBatch(pstmt, 6, "Writing a Manifesto", "Michael Foot", "Wednesday", 4);
		System.out.println("...executing the batched insertions...");
		pstmt.executeBatch();
		System.out.println("...done.");
	}

	void addCourseToBatch(PreparedStatement pstmt,
			int cid,
			String courseName,
			String teacher,
			String day,
			int capacity)
			throws SQLException {
		pstmt.clearParameters();
		pstmt.setInt(1, cid);
		pstmt.setString(2, courseName);
		pstmt.setString(3, teacher);
		pstmt.setString(4, day);
		pstmt.setInt(5, capacity);
		pstmt.addBatch();
	}

	/**
	 * Initialise the MillbankEnrollments table. MillbankEnrollments(
	 * SID, CID )
	 */
	public void initialiseMillbankEnrollments()
			throws SQLException {
		String table = "MillbankEnrollments";

		clearTable(table, "(CID int, SID int)");

		// Set up the prepared statement.
		PreparedStatement pstmt = conn.prepareStatement("insert into " + table +
				" (CID, SID)" +
				" values( ?, ? )");

		System.out.println("Batching insertions for the table...");
		addEnrollmentToBatch(pstmt, 1, 2341);
		addEnrollmentToBatch(pstmt, 1, 3412);
		addEnrollmentToBatch(pstmt, 2, 3412);
		addEnrollmentToBatch(pstmt, 5, 3412);
		addEnrollmentToBatch(pstmt, 5, 3214);
		addEnrollmentToBatch(pstmt, 5, 1234);
		addEnrollmentToBatch(pstmt, 5, 2341);
		System.out.println("...executing the batched insertions...");
		pstmt.executeBatch();
		System.out.println("...done.");
	}

	void addEnrollmentToBatch(PreparedStatement pstmt,
			int cid,
			int sid)
			throws SQLException {
		pstmt.clearParameters();
		pstmt.setInt(1, cid);
		pstmt.setInt(2, sid);
		pstmt.addBatch();
	}

	/**
	 * Initialise the VictoriaStreetCourses
	 * table. VictoriaStreetCourses( CID, CourseName, Teacher, Day,
	 * Capacity )
	 */
	public void initialiseVictoriaStreetCourses()
			throws SQLException {
		// To ensure that you geneate a unique table name by
		// prepending it with your username.
		String table = "VictoriaStreetCourses";

		clearTable(table, "(CID int, CourseName char(30), Teacher char(20), Day char(10), Capacity int)");

		PreparedStatement pstmt = conn.prepareStatement("insert into " + table +
				" (CID, CourseName, Teacher, Day, Capacity)" +
				" values( ?, ?, ?, ?, ? )");

		System.out.println("Batching insertions for the table...");
		addCourseToBatch(pstmt, 3, "Diary Management", "Jeffrey Archer", "Friday", 4);
		addCourseToBatch(pstmt, 4, "Media Management", "Boris Johnson", "Monday", 3);
		addCourseToBatch(pstmt, 7, "How to Win", "Iain Duncan-Smith", "Wednesday", 4);
		addCourseToBatch(pstmt, 8, "Managing the Economy", "Norman Lamont", "Tuesday", 4);
		addCourseToBatch(pstmt, 9, "Team Managment", "John Major", "Tuesday", 4);
		System.out.println("...executing the batched insertions...");
		pstmt.executeBatch();
		System.out.println("...done.");
	}

	/**
	 * Initialise the VictoriaStreetEnrollments
	 * table. VictoriaStreetEnrollments( SID, CID )
	 */
	public void initialiseVictoriaStreetEnrollments()
			throws SQLException {
		String table = "VictoriaStreetEnrollments";

		clearTable(table, "(CID int, SID int)");

		// Set up the prepared statement.
		PreparedStatement pstmt = conn.prepareStatement("insert into " + table +
				" (CID, SID)" +
				" values( ?, ? )");

		System.out.println("Batching insertions for the table...");
		addEnrollmentToBatch(pstmt, 3, 4321);
		addEnrollmentToBatch(pstmt, 3, 3214);
		addEnrollmentToBatch(pstmt, 4, 4123);
		addEnrollmentToBatch(pstmt, 7, 4123);
		addEnrollmentToBatch(pstmt, 7, 3214);
		addEnrollmentToBatch(pstmt, 8, 3214);
		addEnrollmentToBatch(pstmt, 8, 2341);
		addEnrollmentToBatch(pstmt, 9, 1234);
		addEnrollmentToBatch(pstmt, 9, 4123);
		addEnrollmentToBatch(pstmt, 9, 4321);
		System.out.println("...executing the batched insertions...");
		pstmt.executeBatch();
		System.out.println("...done.");
	}

	void clearTable(String table, String fields)
			throws SQLException {
		Statement stmt = conn.createStatement();
		// If the table doesn't exist, this query will throw an
		// SQLException.
		try {
			System.out.println("Checking for the existence of table " + table + "...");
			stmt.execute("create table " + table + " " + fields);
			System.out.println("...new table created in database.");
		}
		// Otherwise, empty it.
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("...table exists; deleting all entries...");
			stmt.execute("delete from " + table);
			System.out.println("...done.");
		}
	}

	void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.err.println("An error occurred when closing the database connection.");
		}
	}
}
