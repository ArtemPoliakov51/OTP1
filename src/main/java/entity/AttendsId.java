package entity;

import java.io.Serializable;

public class AttendsId implements Serializable {
    private Integer courseId;
    private Integer studentId;

    public AttendsId() {}

    public AttendsId(Integer courseId, Integer studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
    }

    public int getStudentId() {return this.studentId;}

    public int getCourseId() {return this.courseId;}

}
