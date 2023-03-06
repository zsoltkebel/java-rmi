package examples.distcollege;

import java.rmi.RemoteException;

import java.util.Vector;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a generic college implementation that can be specialised
 * for specific colleges by specifying the name in the constructor.
 * 
 * @see CollegeInterface
 * 
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class CollegeImpl implements CollegeInterface {
	private String _courses;
	private String _enrollments;
	private Connection _dbCon;

	/**
	 * The name of the college is specified in the constructor.
	 */
	public CollegeImpl(String dbdriver,
			String dburl,
			String dbuname,
			String dbpasswd,
			String college)
			throws RemoteException {
		try {
			Class.forName(dbdriver);
			_courses = college + "Courses";
			_enrollments = college + "Enrollments";
			_dbCon = DriverManager.getConnection(dburl, dbuname, dbpasswd);
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load driver.");
			System.err.println(e.getMessage());
			e.printStackTrace(System.err);
			return;
		} catch (SQLException e) {
			System.err.println("Problem with database access.");
			e.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * Request information on a specific course. A
	 * NoSuchCourseException is thrown if the course with identifier
	 * cid does not exist at the college.
	 * 
	 * <p>
	 * An important issue of note here is that no SQLExceptions are
	 * thrown and no database-specific data structures are passed
	 * between remote objects. This is good practice as it means that
	 * this remote object encapsulates all database access
	 * functionality and error handling.
	 */
	public Course findCourse(int cid)
			throws RemoteException,
			NoSuchCourseException,
			ServiceNotAvailableException {
		try {
			Statement stmt = _dbCon.createStatement();
			ResultSet rs;
			String sql;

			sql = "select Teacher, CourseName from " + _courses +
					" where CID = " + cid;
			rs = stmt.executeQuery(sql);

			// Move cursor to the first entry (there should only be one
			// entry). If there is no first entry in the ResultSet, then
			// the course does not exist.
			if (rs.next()) {
				String teacher = (String) rs.getObject("Teacher");
				String course = (String) rs.getObject("CourseName");
				return new Course(cid, teacher, course);
			} else {
				throw new NoSuchCourseException();
			}
		} catch (SQLException e) {
			System.err.println("Problem with database access.");
			e.printStackTrace(System.err);
			throw new ServiceNotAvailableException();
		}
	}

	/**
	 * Returns a Vector of student IDs indicating those who are
	 * registered on a specified course.
	 * 
	 * <p>
	 * Note that a Vector contains elements that extend Object, but
	 * int is not an object in Java (one of the weaknesses of Java's
	 * object model), so the Vector returned will contain instances of
	 * java.lang.Integer. I could get around this by just returning
	 * the ResultSet, but I want to avoid the need for the client to
	 * have to deal with database issues.
	 */
	public Vector<Integer> findRegisteredStudents(int cid)
			throws RemoteException,
			ServiceNotAvailableException {
		try {
			Statement stmt = _dbCon.createStatement();
			ResultSet rs;
			String sql;

			sql = "select SID from " + _enrollments + " where CID = " + cid;
			rs = stmt.executeQuery(sql);

			// Note that NoSuchCourseException is not thrown by this
			// method. There is no way to distinguish between a
			// course that does not exist (and hence has no
			// enrollments) and a course that does exist and has no
			// registered students. I could invoke findCourse() and
			// simply allow the NoSuchCourseException to propogate
			// back to the client, but I shall leave the task of
			// interpreting the Vector returned to the client.
			Vector<Integer> sids = new Vector<Integer>(5);
			while (rs.next()) {
				sids.add(rs.getInt("SID"));
			}
			return sids;
		} catch (SQLException e) {
			System.err.println("Problem with database access.");
			e.printStackTrace(System.err);
			throw new ServiceNotAvailableException();
		}
	}

	/**
	 * Returns a Vector of course IDs indicating the courses that a
	 * specific student is registered for.
	 */
	public Vector<Integer> findCoursesEnrolled(int sid)
			throws RemoteException,
			ServiceNotAvailableException {
		try {
			Statement stmt = _dbCon.createStatement();
			ResultSet rs;
			String sql;

			sql = "select CID from " + _enrollments + " where SID = " + sid;
			rs = stmt.executeQuery(sql);

			Vector<Integer> cids = new Vector<Integer>(5);
			while (rs.next()) {
				cids.add(rs.getInt("CID"));
			}
			return cids;
		} catch (SQLException e) {
			System.err.println("Problem with database access.");
			e.printStackTrace(System.err);
			throw new ServiceNotAvailableException();
		}
	}
}
