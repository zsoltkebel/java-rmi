package examples.secure;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Sets up two database tables: logins and staff salaries.
 * 
 * <p>
 * The relations are:
 * <ul>
 * <li>login( name, password )
 * <li>staff( ID, Name, Salary )
 * </ul>
 * 
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class DBInit {
	String _uname;
	Connection _dbCon;

	/**
	 * Load the database driver, create the database initialiser and
	 * populate the table.
	 */
	public static void main(String args[]) {
		try {
			if (args.length < 4) {
				System.err.println("Usage: java DBInit <driver> <URL> <database-username> <database-password>");
				return;
			}
			// Load the database driver as specified in the
			// command-line arguments.
			Class.forName(args[0]);
			// Create the initialiser.
			DBInit db = new DBInit(args[1], args[2], args[3]);
			// Initialise the tables.
			db.initialiseLogins();
			db.initialiseStaff();
			db.close();
		} catch (ClassNotFoundException e) {
			System.err.println("Failed to load driver.");
			e.printStackTrace(System.err);
			return;
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * Record the username and database URL, and establish a
	 * connection to the database.
	 */
	public DBInit(String url,
			String dbuname,
			String dbpasswd)
			throws SQLException {
		_uname = System.getProperty("user.name");
		_dbCon = DriverManager.getConnection(url, dbuname, dbpasswd);
	}

	/**
	 * Initialise the logins table.
	 */
	public void initialiseLogins()
			throws SQLException {
		// To ensure that you geneate a unique table name by
		// prepending it with your username.
		String tableName = _uname + "Login";
		Statement stmt = _dbCon.createStatement();

		// If the table doesn't exist, this query will throw an
		// SQLException.
		try {
			stmt.executeUpdate("drop table " + tableName);
		} catch (SQLException e) {
			// Ignore the exception.
		}
		// Create the table we want (salary is recorded as a number of pennies).
		stmt.executeUpdate("create table " + tableName + " (name char(32), password char(32))");
		// Now populate the table with the initial data.
		stmt.executeUpdate("insert into " + tableName + " (name, password) values( 'tnorman', 'tnorman' )");
		stmt.executeUpdate("insert into " + tableName + " (name, password) values( 'ereiter', 'ereiter' )");
		stmt.executeUpdate("insert into " + tableName + " (name, password) values( 'fmeneguzzi', 'fmeneguzzi' )");
		stmt.close();
	}

	/**
	 * Initialise the staff table. staff( ID, Name, Salary )
	 */
	public void initialiseStaff()
			throws SQLException {
		// To ensure that you geneate a unique table name by
		// prepending it with your username.
		String tableName = _uname + "Staff";
		Statement stmt = _dbCon.createStatement();

		// If the table doesn't exist, this query will throw an
		// SQLException.
		try {
			stmt.executeUpdate("drop table " + tableName);
		} catch (SQLException e) {
			// Ignore the exception.
		}
		// Create the table we want (salary is recorded as a number of pennies).
		stmt.executeUpdate("create table " + tableName + " (ID int, Name char(20), Salary int)");
		// Now populate the table with the initial data.
		stmt.executeUpdate("insert into " + tableName + " (ID, Name, Salary) values( 1, 'Fred', 2000000 )");
		stmt.executeUpdate("insert into " + tableName + " (ID, Name, Salary) values( 2, 'Andy', 2500000 )");
		stmt.executeUpdate("insert into " + tableName + " (ID, Name, Salary) values( 3, 'Sue', 3000000 )");
		stmt.close();
	}

	void close() {
		try {
			_dbCon.close();
		} catch (SQLException e) {
			System.err.println("An error occurred when closing the database connection.");
		}
	}
}
