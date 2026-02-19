package entity;

import java.io.Serializable;

public class ChecksId implements Serializable {
        private Integer attendanceCheckId;
        private Integer studentId;

        public ChecksId() {}

        public ChecksId(Integer attChekId, Integer studentId) {
            this.attendanceCheckId = attChekId;
            this.studentId = studentId;
        }

    public int getAttendanceCheckId() {return attendanceCheckId;}

    public int getStudentId() {return studentId;}
}
