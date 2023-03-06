package examples.distcollege;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.Vector;
import java.util.Enumeration;

import java.rmi.Naming;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a simple text-based client to enable a user to manipulate
 * the student records (managed locally) and the courses and
 * enrollments (managed remotely).
 * 
 * <p>
 * The relation contained in the local database tables is:
 * <ul>
 * <li>Students( SID, FirstName, LastName )
 * </ul>
 * 
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class QueryTool {
	String _students;
	Connection _dbCon;

	CollegeInterface _millbank;
	CollegeInterface _victoriastreet;

	/**
	 * Load the database driver, create the database query tool and
	 * run the tool.
	 * 
	 * <p>
	 * The arguments are as follows:
	 * <ul>
	 * <li>args[0] = The host of the rmiregistry.
	 * <li>args[1] = The port of the rmiregistry.
	 * <li>args[2] = The database driver.
	 * <li>args[3] = The database URL.
	 * <li>args[4] = The username to be used to connect to the database.
	 * <li>args[5] = The database password for this user.
	 * </ul>
	 */
	public static void main(String args[]) {
		if (args.length < 6) {
			System.err.println(
					"Usage: java QueryTool <host> <port> <driver> <URL> <database-username> <database-password>");
			return;
		}
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);

		try {
			// Load the database driver as specified in the
			// command-line arguments.
			Class.forName(args[2]);
			// Create the query tool.
			QueryTool qt = new QueryTool(hostname, port, args[3], args[4], args[5]);
			// Start the tool.
			qt.go();
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load driver.");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			return;
		} catch (SQLException e) {
			System.err.println("Problem with database access.");
			e.printStackTrace(System.err);
			return;
		} catch (Exception e) {
			System.err.println("Cannot obtain a reference to one of the colleges.");
			e.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * This constructor obtains remote references to the Millbank and
	 * VictoriaStreet colleges, records the table name for students,
	 * and establishes a connection to the database.
	 */
	public QueryTool(String hostname,
			int port,
			String dburl,
			String dbuname,
			String dbpasswd)
			throws Exception {
		_students = "Students";

		// Contact the rmiregistry and obtain remote references
		// for the Millbank and Victoria Street colleges.
		String regURL;
		regURL = "rmi://" + hostname + ":" + port + "/Millbank";
		_millbank = (CollegeInterface) Naming.lookup(regURL);

		regURL = "rmi://" + hostname + ":" + port + "/VictoriaStreet";
		_victoriastreet = (CollegeInterface) Naming.lookup(regURL);

		_dbCon = DriverManager.getConnection(dburl, dbuname, dbpasswd);
	}

	/**
	 * Provide the user with various options to query and manipulate
	 * the database.
	 */
	public void go()
			throws SQLException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		char sel;
		// Prompt the user for a query type to be performed.
		try {
			do {
				System.out.println("\n\n\n\n\nWelcome to the student information management system." +
						"\nPlease select one of the following options:\n\n" +
						"\t s\t View a student record.\n\n" +
						"\t c\t View a course record.\n\n" +
						"\t e\t Enroll a student on a course.\n\n" +
						"\t q\t Quit.\n\n");
				System.out.print("Enter [s/c/e/q]: ");
				sel = in.readLine().charAt(0);
				switch (sel) {
					case 's':
						System.out.print("Enter the student ID: ");
						int sid = Integer.parseInt(in.readLine());
						displayStudent(sid);
						System.out.print("Press return to continue...");
						in.readLine();
						break;
					case 'c':
						System.out.print("Enter the Course ID: ");
						int cid = Integer.parseInt(in.readLine());
						displayCourse(cid);
						System.out.print("Press return to continue...");
						in.readLine();
						break;
					case 'e':
						// Need to check both that the student is not
						// registered for a course on the same day and
						// that the course is not at capacity before
						// committing an enrollment.
						break;
					default:
				}
			} while (sel != 'q');
		} catch (IOException e) {
			System.err.println("Failure to parse user response; exiting...");
		} catch (StringIndexOutOfBoundsException e) {
			System.err.println("You need to enter a valid option; exiting...");
		}
	}

	public void displayStudent(int sid)
			throws SQLException {
		Statement stmt = _dbCon.createStatement();
		ResultSet rs;
		String sql;

		sql = "select FirstName, LastName from " + _students +
				" where SID = " + sid;
		rs = stmt.executeQuery(sql);

		// Move cursor to the first entry (there should only be one
		// entry). If there is no first entry in the ResultSet, then
		// the student does not exist.
		if (rs.next()) {
			String firstname = (String) rs.getObject("FirstName");
			String lastname = (String) rs.getObject("LastName");
			System.out.println("\nStudent record for:\n" + sid + ": "
					+ firstname + lastname);
		} else {
			System.out.println("Student does not exist.");
			return;
		}
		stmt.close();

		// Now obtain the list of courses that the student is
		// registered for.
		System.out.println("\nCourses for which the student is registered:");
		Vector<Integer> enrollments;
		Course course;
		try {
			System.out.println("------------------------------------------------------------");
			System.out.println("CID\tTeacher\t\tCourseName");
			System.out.println("------------------------------------------------------------");
			enrollments = _millbank.findCoursesEnrolled(sid);
			for (Enumeration<Integer> e = enrollments.elements(); e.hasMoreElements();) {
				int cid = ((Integer) e.nextElement()).intValue();
				course = _millbank.findCourse(cid);
				System.out.println(cid + "\t" + course.teacher() + "\t" + course.name());
			}
			enrollments = _victoriastreet.findCoursesEnrolled(sid);
			for (Enumeration<Integer> e = enrollments.elements(); e.hasMoreElements();) {
				int cid = ((Integer) e.nextElement()).intValue();
				course = _victoriastreet.findCourse(cid);
				System.out.println(cid + "\t" + course.teacher() + "\t" + course.name());
			}
			System.out.println("------------------------------------------------------------");
		} catch (NoSuchCourseException e) {
			System.err.println("The database seems to be in an inconsistent state!");
		} catch (Exception e) {
			System.err.println("Remote database services not available.");
		}
	}

	public void displayCourse(int cid)
			throws SQLException {
		Course course;
		Vector<Integer> enrollments;
		try {
			course = _millbank.findCourse(cid);
			enrollments = _millbank.findRegisteredStudents(cid);
		} catch (NoSuchCourseException e) {
			try {
				course = _victoriastreet.findCourse(cid);
				enrollments = _victoriastreet.findRegisteredStudents(cid);
			} catch (NoSuchCourseException ee) {
				System.out.println("Course does not exist.");
				return;
			} catch (Exception ee) {
				System.out.println("Course information not available.");
				return;
			}
		} catch (Exception e) {
			System.out.println("Course information not available.");
			return;
		}
		System.out.println("\n" + cid + ": " + course.name() +
				"\nby\n" + course.teacher());

		// We have the Vector of student IDs for those registered on
		// the course, so now we need to go through this list an
		// display each student's details.
		System.out.println("\nClass list:");

		// For efficiency reasons I am going to use a
		// PreparedStatement here; this enables the SQL statement to
		// be precompiled.
		PreparedStatement pstmt = _dbCon.prepareStatement("select FirstName, LastName from " +
				_students + " where SID = ?");
		ResultSet rs;
		System.out.println("------------------------------------------------------------");
		System.out.println("SID\tFirstName\tLastName");
		System.out.println("------------------------------------------------------------");
		for (Enumeration<Integer> e = enrollments.elements(); e.hasMoreElements();) {
			int sid = ((Integer) e.nextElement()).intValue();
			pstmt.clearParameters();
			pstmt.setInt(1, sid);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println(sid + "\t" + (String) rs.getObject("FirstName") +
						"\t" + (String) rs.getObject("LastName"));
			}
		}
		System.out.println("------------------------------------------------------------");
		pstmt.close();
	}
}