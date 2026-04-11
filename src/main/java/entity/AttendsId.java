package entity;

import java.io.Serializable;

/**
 * Composite primary key class for the Attends entity.
 *
 * <p>This class represents a combined identifier consisting of a course ID
 * and a student ID. It is used to uniquely identify an enrollments relationship
 * between a student and a course.</p>
 *
 * <p>This class implements {@link Serializable} as required by JPA.</p>
 */
public class AttendsId implements Serializable {

    /**
     * Identifier of the course.
     */
    private Integer courseId;

    /**
     * Identifier of the student.
     */
    private Integer studentId;

    /**
     * Default constructor required by JPA.
     */
    public AttendsId() {}

    /**
     * Constructs an AttendsId with the given course and student identifiers.
     *
     * @param courseId the identifier of the course
     * @param studentId the identifier of the student
     */
    public AttendsId(Integer courseId, Integer studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    /**
     * Returns the student ID.
     *
     * @return the id of the student
     */
    public int getStudentId() {return this.studentId;}

    /**
     * Returns the course ID.
     *
     * @return the id of the course
     */
    public int getCourseId() {return this.courseId;}

}
