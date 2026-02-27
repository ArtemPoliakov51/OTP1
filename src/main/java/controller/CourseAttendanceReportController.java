package controller;

import dao.*;
import entity.*;
import view.CourseAttendanceReportView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseAttendanceReportController {

    /** The Course entity for course data */
    private int courseId;
    /** The CourseDao class instance for database operations on the course table */
    private CourseDao courseDao = new CourseDao();
    private AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();
    /** The AddStudentsView class instance */
    private CourseAttendanceReportView view;

    private int teacherId;

    /**
     * Constructor for AddStudentsController
     * @param reportView The instance of the CourseAttendanceReportView class
     * @param courseId The unique ID of the course
     */
    public CourseAttendanceReportController(CourseAttendanceReportView reportView, int courseId) {
        this.courseId = courseId;
        this.view = reportView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Method for passing the course's unique identifier and name for the view
     */
    public void updateViewInfo() {
        Course course = courseDao.find(courseId);
        view.displayCourseIdentifierAndName(course.getIdentifier(), course.getName());
    }

    public void showTeacherInfo() {
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);
        System.out.println("AllCoursesView");
        System.out.println(teacher.getFirstname());

        view.displayTeacherInfo(teacher.getFirstname(), teacher.getLastname(), teacher.getEmail());
    }

    /**
     * Method for counting the overall attendance percentage for the course
     * @return the total attendance percentage for single course
     */
    private int countCourseAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        List<Double> attPercentages = new ArrayList<>();
        for (AttendanceCheck attCheck : attendanceChecks) {
            attPercentages.add(countAttendanceCheckPercentage(attCheck));
        }
        int totalAttendancePercentage = 0;

        if (attPercentages.size() != 0) {
            double total = 0;
            for (Double percentage : attPercentages) {
                total = total + percentage;
            }
            totalAttendancePercentage = (int) total/attPercentages.size();
        }

        return totalAttendancePercentage;
    }

    /**
     * Method for counting the attendance percentage for a single attendance check
     * @param attCheck The instance of the AttendanceCheck class
     * @return attendance percentage for single attendance check
     */
    private double countAttendanceCheckPercentage(AttendanceCheck attCheck) {
        ChecksDao checksDao = new ChecksDao();
        List<Checks> checks = checksDao.findByAttendanceCheck(attCheck.getId());
        // Go through all of them, and if student was present add it to a new list
        List<Checks> present = new ArrayList<>();
        for (Checks checksCheck : checks) {
            if (Objects.equals(checksCheck.getAttendanceStatus(), "PRESENT")) {
                System.out.println("Present!");
                present.add(checksCheck);
            }
        }
        // Count the attendance percentage for this attendance check
        System.out.println(present.size());
        System.out.println(checks.size());
        System.out.println(present.size() / checks.size());
        double attCheckPercentage = (double) present.size() / (double) checks.size() * 100;
        return attCheckPercentage;
    }

    public void showAttendancePercentage() {
        view.displayCourseAttendancePercentage(countCourseAttendancePercentage());
    }

    private int countAllAbsences() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck.getId());
            allChecks.addAll(checksList);
        }

        List<Checks> absences = new ArrayList<>();
        for (Checks aChecks : allChecks) {
            if (aChecks.getAttendanceStatus().equals("ABSENT")) {
                absences.add(aChecks);
            }
        }
        return absences.size();
    }

    private int countAllExcusedAbsences() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck.getId());
            allChecks.addAll(checksList);
        }

        List<Checks> excuses = new ArrayList<>();
        for (Checks aChecks : allChecks) {
            if (aChecks.getAttendanceStatus().equals("EXCUSED")) {
                excuses.add(aChecks);
            }
        }
        return excuses.size();
    }

    private AttendanceCheck findCheckWithLowestAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        double currentLowest = 100;
        AttendanceCheck lowestAttendanceCheck = null;
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            double percentage = countAttendanceCheckPercentage(attendanceCheck);
            if (percentage < currentLowest) {
                currentLowest = percentage;
                lowestAttendanceCheck = attendanceCheck;
            }
        }
        return lowestAttendanceCheck;
    }

    private AttendanceCheck findCheckWithHighestAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        double currentHighest = 0;
        AttendanceCheck highestAttendanceCheck = null;
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            double percentage = countAttendanceCheckPercentage(attendanceCheck);
            if (percentage > currentHighest) {
                currentHighest = percentage;
                highestAttendanceCheck = attendanceCheck;
            }
        }
        return highestAttendanceCheck;
    }

    public void showCourseReportLines() {
        AttendsDao attendsDao = new AttendsDao();
        List<Attends> attends = attendsDao.findByCourse(courseId);
        int numOfStudents = attends.size();

        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        int numOfChecks = attendanceChecks.size();

        int absences = countAllAbsences();
        int excuses = countAllExcusedAbsences();

        AttendanceCheck lowestCheck = findCheckWithLowestAttendancePercentage();
        AttendanceCheck highestCheck = findCheckWithHighestAttendancePercentage();

        double lowestPercentage = countAttendanceCheckPercentage(lowestCheck);
        double highestPercentage = countAttendanceCheckPercentage(highestCheck);


        view.displayCourseReportLines(numOfStudents, numOfChecks, absences, excuses, lowestPercentage, lowestCheck.getCheckDate(), lowestCheck.getCheckTime(),
                highestPercentage, highestCheck.getCheckDate(), highestCheck.getCheckTime());
    }

    public void createAndSaveResults(File destinationFile) {
        Course course = courseDao.find(courseId);

        AttendsDao attendsDao = new AttendsDao();
        List<Attends> attends = attendsDao.findByCourse(courseId);
        int numOfStudents = attends.size();

        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        int numOfChecks = attendanceChecks.size();

        int absences = countAllAbsences();
        int excuses = countAllExcusedAbsences();

        AttendanceCheck lowestCheck = findCheckWithLowestAttendancePercentage();
        AttendanceCheck highestCheck = findCheckWithHighestAttendancePercentage();

        double lowestPercentage = countAttendanceCheckPercentage(lowestCheck);
        double highestPercentage = countAttendanceCheckPercentage(highestCheck);

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile.getPath() + "/" + course.getIdentifier() + "_attendance_report_"
                    + LocalDateTime.now().getDayOfYear() + LocalDateTime.now().getMonthValue() + LocalDateTime.now().getYear()
                    + "_" +LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()
                    + ".txt"));
            bufferedWriter.write("ATTENDANCE REPORT  " + LocalDate.now());
            bufferedWriter.newLine();
            bufferedWriter.write("COURSE: " + course.getIdentifier() + " - " + course.getName());
            bufferedWriter.newLine();
            bufferedWriter.write("Created: " + course.getCreated());
            bufferedWriter.newLine();
            bufferedWriter.write("Archived: " + course.getArchived());
            bufferedWriter.newLine();
            bufferedWriter.write("---------------------------------------------------------------");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("STATISTICS: ");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("Attendance Percentage: " + countCourseAttendancePercentage() + "%");
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Students: " + numOfStudents);
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Attendance Checks: " + numOfChecks);
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Absences: " + absences);
            bufferedWriter.newLine();
            bufferedWriter.write("Total of Excused Absences: " + excuses);
            bufferedWriter.newLine();
            bufferedWriter.write("Lowest Attendance Percentage: " + lowestPercentage + "%  " + lowestCheck.getCheckDate() + "  " + lowestCheck.getCheckTime());
            bufferedWriter.newLine();
            bufferedWriter.write("Highest Attendance Percentage: " + highestPercentage + "%  " + highestCheck.getCheckDate() + "  " + highestCheck.getCheckTime());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
