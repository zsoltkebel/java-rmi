package examples.distcollege;

import java.io.Serializable;

/**
 * A data structure that encapsulates the details of a course.
 * 
 * <p>
 * A course has a course id (cid), a teacher and a name.
 * 
 * <p>
 * It must implement the Serializable interface so that instances
 * can be marshalled and unmarshalled for transmission over the
 * network.
 * 
 * @author Tim Norman, refactored by Zsolt Kebel, University of Aberdeen
 */

public class Course implements Serializable {
    private int cid;
    private String teacher;
    private String name;

    /**
     * The only constructor requires all three fields to be specified.
     */
    public Course(int cid, String teacher, String name) {
        this.cid = cid;
        this.teacher = teacher;
        this.name = name;
    }

    /**
     * Read only access to the course identifier.
     */
    public int cid() {
        return cid;
    }

    /**
     * Read only access to the teacher's name.
     */
    public String teacher() {
        return teacher;
    }

    /**
     * Read only access to the course name.
     */
    public String name() {
        return name;
    }
}
