package controller;

import dao.*;
import entity.*;
import view.StudentAttendanceReportView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Comparator;

public class StudentAttendanceReportController {
    /** The Course entity for course data */
    private Course course;
    private Student student;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    private StudentDao studentDao = new StudentDao();
    private AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();
    /** The AddStudentsView class instance */
    private StudentAttendanceReportView view;

    private int teacherId;

    /**
     * Constructor for AddStudentsController
     * @param reportView The instance of the StudentAttendanceReportView class
     * @param courseId The unique ID of the course
     */
    public StudentAttendanceReportController(StudentAttendanceReportView reportView, int courseId, int studentId) {
        this.course = courseDao.find(courseId);
        this.student = studentDao.find(studentId);
        this.view = reportView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Method for passing the course's unique identifier and name, and student's first and lastname and id for the view
     */
    public void updateViewInfo() {
        view.displayCourseIdentifierAndName(course.getIdentifier(), course.getName());
        view.displayStudentInfo(student.getFirstname(), student.getLastname(), student.getId());
    }

    public void showTeacherInfo() {
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        view.displayTeacherInfo(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail());
    }

    /**
     * Method for counting student's attendance percentage on the course
     * @return the student's attendance percentage
     */
    private int countStudentAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);

        List<Checks> allAbsences = new ArrayList<>();
        allAbsences.addAll(findAllAbsences());
        allAbsences.addAll(findAllExcuses());

        double attendancePercentage = 100;
        if (!allAbsences.isEmpty()) {
            attendancePercentage = (((double) attendanceChecks.size() - (double) allAbsences.size()) / attendanceChecks.size()) * 100;
            System.out.println(attendancePercentage);
        }

        return (int) attendancePercentage;
    }

    public void showAttendancePercentage() {
        view.displayAttendancePercentage(countStudentAttendancePercentage());
    }

    public void showAbsences() {
        List<Checks> all = new ArrayList<>();

        all.addAll(findAllAbsences());
        all.addAll(findAllExcuses());

        all.sort(Comparator.comparing((Checks a) ->
                a.getAttendanceCheck().getCheckDate()
        ).thenComparing(a -> a.getAttendanceCheck().getCheckTime()));

        for (Checks aChecks : all) {
            view.addToAbsencesList(aChecks.getAttendanceStatus(), aChecks.getAttendanceCheck().getCheckDate(), aChecks.getAttendanceCheck().getCheckTime());
        }
    }

    private List<Checks> findAllAbsences() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allStudentsChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck);
            for (Checks aChecks : checksList) {
                if (aChecks.getStudent().equals(student)) {
                    allStudentsChecks.add(aChecks);
                }
            }
        }

        List<Checks> absences = new ArrayList<>();
        for (Checks aChecks : allStudentsChecks) {
            if (aChecks.getAttendanceStatus().equals("ABSENT")) {
                absences.add(aChecks);
            }
        }
        return absences;
    }

    private List<Checks> findAllExcuses() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allStudentsChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck);
            for (Checks aChecks : checksList) {
                if (aChecks.getStudent().equals(student)) {
                    allStudentsChecks.add(aChecks);
                }
            }
        }

        List<Checks> excuses = new ArrayList<>();
        for (Checks aChecks : allStudentsChecks) {
            if (aChecks.getAttendanceStatus().equals("EXCUSED")) {
                excuses.add(aChecks);
            }
        }
        return excuses;
    }

    public void showStudentReportLines() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);
        int numOfChecks = attendanceChecks.size();

        int absences = findAllAbsences().size();
        int excuses = findAllExcuses().size();

        view.displayStudentReportLines(numOfChecks, absences, excuses);
    }

    public void createAndSaveResults(File destinationFile) {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(course);
        int numOfChecks = attendanceChecks.size();

        int absences = findAllAbsences().size();
        int excuses = findAllExcuses().size();

        List<Checks> allAbsences = new ArrayList<>();
        allAbsences.addAll(findAllAbsences());
        allAbsences.addAll(findAllExcuses());

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile.getPath() + "/" + course.getIdentifier() + "_student_" + student.getId() + "_attendance_report_"
                    + LocalDateTime.now().getDayOfYear() + LocalDateTime.now().getMonthValue() + LocalDateTime.now().getYear()
                    + "_" +LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()
                    + ".txt"));
            bufferedWriter.write("ATTENDANCE REPORT  " + LocalDate.now());
            bufferedWriter.newLine();
            bufferedWriter.write("COURSE: " + course.getIdentifier() + " - " + course.getName());
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("STUDENT " + student.getId());
            bufferedWriter.newLine();
            bufferedWriter.write(student.getFirstname() + " " + student.getLastname());
            bufferedWriter.newLine();
            bufferedWriter.write(student.getEmail());
            bufferedWriter.newLine();
            bufferedWriter.write("---------------------------------------------------------------");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("STATISTICS: ");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("Attendance Percentage: " + countStudentAttendancePercentage() + "%");
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Attendance Checks: " + numOfChecks);
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Absences: " + absences);
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Excused Absences: " + excuses);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("ALL ABSENCES: ");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (Checks aChecks : allAbsences) {
                LocalTime time =  aChecks.getAttendanceCheck().getCheckTime();
                String correctMin = time.getMinute() < 10 ? "0" + time.getMinute() : Integer.toString(time.getMinute());
                bufferedWriter.write(aChecks.getAttendanceCheck().getCheckDate() + "  " + time.getHour() + ":" + correctMin + " -------------- " + aChecks.getAttendanceStatus());
                bufferedWriter.newLine();
                bufferedWriter.write("Notes: " + (aChecks.getNotes() != null ? aChecks.getNotes() : ""));
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
