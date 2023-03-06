package examples.distcollege;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;
import java.net.InetAddress;

public class CollegeMainline {

	/**
	 * The server mainline that generates and registers two instances of
 	 * CollegeImpl: one for Millbank and one for VictoriaStreet.
	 *
	 * @param args 
	 * args[0] = The port of the rmiregistry.
 	 * args[1] = The port at which the college is exported.
     * args[2] = The database driver.
     * args[3] = The database URL.
     * args[4] = The username to be used to connect to the database.
     * args[5] = The database password for this user.
	 */
	public static void main(String args[]) {
		if (args.length < 6) {
			System.err.println(
					"Usage:\njava CollegeMainline <registryport> <serverport> <driver> <URL> <database-username> <database-password>");
			return;
		}

		try {
			String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();
			int registryport = Integer.parseInt(args[0]);
			int serverport = Integer.parseInt(args[1]);

			String dbdriver = args[2];
			String dburl = args[3];
			String dbuname = args[4];
			String dbpasswd = args[5];

			CollegeImpl millbank = new CollegeImpl(dbdriver, dburl, dbuname, dbpasswd, "Millbank");
			CollegeInterface millbankstub = (CollegeInterface) UnicastRemoteObject.exportObject(millbank, serverport);

			CollegeImpl victoriastreet = new CollegeImpl(dbdriver, dburl, dbuname, dbpasswd, "VictoriaStreet");
			CollegeInterface victoriastreetstub = (CollegeInterface) UnicastRemoteObject.exportObject(victoriastreet, serverport);

			// NOTE:
			// It might be better to export the different colleges on different server
			// ports!

			Naming.rebind("rmi://" + hostname + ":" + registryport + "/Millbank", millbankstub);
			Naming.rebind("rmi://" + hostname + ":" + registryport + "/VictoriaStreet", victoriastreetstub);
		} catch (java.net.UnknownHostException e) {
			System.err.println(e.getMessage());
		} catch (java.io.IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
