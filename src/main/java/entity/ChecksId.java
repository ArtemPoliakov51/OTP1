package entity;

import java.io.Serializable;

/**
 * Composite primary key class for the Checks entity.
 *
 * <p>This class represents a combined identifier consisting of a student ID
 * and an attendance check ID. It is used to uniquely identify an attendance relationship
 * between a student and an attendance check.</p>
 *
 * <p>This class implements {@link Serializable} as required by JPA.</p>
 */
public class ChecksId implements Serializable {

    /**
     * Identifier of the attendance check.
     */
    private Integer attendanceCheckId;

    /**
     * Identifier of the student.
     */
    private Integer studentId;

    /**
     * Default constructor required by JPA.
     */
    public ChecksId() {}

    /**
     * Constructs an ChecksId with the given attendance check and student identifiers.
     *
     * @param attChekId the identifier of the attendance check
     * @param studentId the identifier of the student
     */
    public ChecksId(Integer attChekId, Integer studentId) {
        this.attendanceCheckId = attChekId;
        this.studentId = studentId;
    }

    /**
     * Returns the attendance check ID.
     *
     * @return the id of the student
     */
    public int getAttendanceCheckId() {return attendanceCheckId;}

    /**
     * Returns the student ID.
     *
     * @return the id of the student
     */
    public int getStudentId() {return studentId;}
}
