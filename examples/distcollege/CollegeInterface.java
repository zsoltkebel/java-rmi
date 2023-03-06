/*******************************************************************
 * cs3515.examples.distcollege.CollegeInterface                    *
 *******************************************************************/

package examples.distcollege;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.Vector;

/**
 * The remote persona of a College, which provides the means for a
 * client to find out the details of a course, and what students are
 * enrolled on a course.

 * <p> Any implementation of this interface must implement at least
 * these methods.

 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public interface CollegeInterface extends Remote
{
    /**
     * Request information on a specific course.  A
     * NoSuchCourseException is thrown if the course with identifier
     * cid does not exist at the college.
     */
    public Course findCourse( int cid )
	throws RemoteException,
	       NoSuchCourseException,
	       ServiceNotAvailableException;

    /**
     * Request a Vector of student identifiers indicating those
     * students enrolled the course with identifier cid.  A
     * NoSuchCourseException is thrown if the course with identifier
     * cid does not exist at the college.
     */
    public Vector<Integer> findRegisteredStudents( int cid )
	throws RemoteException,
	       ServiceNotAvailableException;

    /**
     * Request a Vector of course identifiers indicating those courses
     * that the student with identifier sid is registered for.
     */
    public Vector<Integer> findCoursesEnrolled( int sid )
	throws RemoteException,
	       ServiceNotAvailableException;
}
