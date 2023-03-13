package examples.secure;

import java.util.Scanner;

import examples.secure.QueryTool;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Provides a simple text-based client to enable a user to query
 * the staff salary table.
 * 
 * <p>
 * The relations contained in the 3NF database are:
 * <ul>
 * <li>Staff( ID, Name, Salary )
 * </ul>
 * 
 * @author Tim Norman, University of Aberdeen
 * @author Felipe Meneguzzi, University of Aberdeen (Updates)
 * @version 1.1
 */

public class QueryTool {
	String _staff, _login;
	Connection _dbCon;
	Scanner _userin;

	/**
	 * Load the database driver, create the database query tool and
	 * run the tool.
	 * 
	 * <p>
	 * The arguments are as follows:
	 * <ul>
	 * <li>args[0] = The database driver.
	 * <li>args[1] = The database URL.
	 * <li>args[2] = The username to be used to connect to the database.
	 * <li>args[3] = The database password for this user.
	 * </ul>
	 */
	public static void main(String args[]) {
		try {
			if (args.length < 4) {
				System.err.println("Usage: java QueryTool <driver> <URL> <database-username> <database-password>");
				return;
			}
			// Load the database driver as specified in the
			// command-line arguments.
			Class.forName(args[0]);
			// Create the query tool.
			QueryTool qt = new QueryTool(args[1], args[2], args[3]);
			// Start the tool.
			qt.go();
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
			return;
		}
	}

	/**
	 * This constructor records the table names, and establishes a
	 * connection to the database.
	 */
	public QueryTool(String dburl,
			String dbuname,
			String dbpasswd)
			throws SQLException {
		String uname = System.getProperty("user.name");
		_staff = uname + "Staff";
		_login = uname + "Login";
		_userin = new Scanner(System.in);
		_dbCon = DriverManager.getConnection(dburl, dbuname, dbpasswd);
	}

	/**
	 * Ask for the user to login, then allow them access to salaries by staff ID
	 */
	private void go()
			throws SQLException {
		System.out.print("Enter username: ");
		String username = _userin.nextLine();
		System.out.print("Enter password: ");
		String password = _userin.nextLine();
		if (loginok(username, password)) {
			System.out.println("Login OK");
			querystaff();
		} else
			System.out.println("Failed login");
		_dbCon.close();
	}

	private boolean loginok(String username, String password) {
		try {
			String sql = "SELECT password from " + _login +
					" WHERE name = '" + username +
					"' AND password = '" + password + "'";
			Statement stmt = _dbCon.createStatement();
			ResultSet results = stmt.executeQuery(sql);
			if (results.first()) {
				return true;
			} else
				return false;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		return true;
	}

	private void querystaff() {
		try {
			System.out.print("\nPlease enter a staff ID: ");
			String id = _userin.nextLine();
			Statement stmt = _dbCon.createStatement();
			String sql = "select Name, Salary from " + _staff + " where ID = " + id;
			ResultSet rs = stmt.executeQuery(sql);
			displayResultSet(rs);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	private void displayResultSet(ResultSet rs)
			throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		System.out.println("------------------------------------------------------------");
		for (int i = 1; i <= cols; i++)
			printField(rsmd.getColumnName(i));
		System.out.println("\n------------------------------------------------------------");
		while (rs.next()) {
			for (int i = 1; i <= cols; i++)
				printField(rs.getString(i));
			System.out.println();
		}
		System.out.println("------------------------------------------------------------\n");
	}

	private void printField(String name) {
		if (name == null)
			name = "NULL";
		System.out.print(name + "\t");
	}
}
