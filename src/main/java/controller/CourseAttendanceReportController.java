package controller;

import dao.*;
import entity.*;
import i18n.I18nManager;
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
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        view.displayCourseIdentifierAndName(course.getIdentifier(), course.getName(lang));
    }

    public void showTeacherInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        TeacherDao teacherDao = new TeacherDao();
        Teacher teacher = teacherDao.find(teacherId);

        view.displayTeacherInfo(teacher.getFirstname(lang), teacher.getLastname(lang), teacher.getEmail());
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
        System.out.println(attCheck);
        List<Checks> checks = checksDao.findByAttendanceCheck(attCheck.getId());
        // Go through all of them, and if student was present add it to a new list
        List<Checks> present = new ArrayList<>();
        for (Checks checksCheck : checks) {
            if (Objects.equals(checksCheck.getAttendanceStatus(), "PRESENT")) {
                present.add(checksCheck);
            }
        }
        // Count the attendance percentage for this attendance check
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
            if (percentage <= currentLowest) {
                System.out.println("Current lowest: " + percentage + attendanceCheck);
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
            if (percentage >= currentHighest) {
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

        System.out.println("Lowest check: " + lowestCheck);
        System.out.println("Highest check: " + highestCheck);
        double lowestPercentage = countAttendanceCheckPercentage(lowestCheck);
        double highestPercentage = countAttendanceCheckPercentage(highestCheck);


        view.displayCourseReportLines(numOfStudents, numOfChecks, absences, excuses, lowestPercentage, lowestCheck.getCheckDate(), lowestCheck.getCheckTime(),
                highestPercentage, highestCheck.getCheckDate(), highestCheck.getCheckTime());
    }

    public void createAndSaveResults(File destinationFile) {
        String lang = I18nManager.getCurrentLocale().getLanguage();
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
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.title").toUpperCase() + "   " + LocalDate.now());
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.course") + course.getIdentifier() + " - " + course.getName(lang));
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.created") + course.getCreated());
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.archived") + course.getArchived());
            bufferedWriter.newLine();
            bufferedWriter.write("---------------------------------------------------------------");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.statistics"));
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.percentage") + countCourseAttendancePercentage() + "%");
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.students") + numOfStudents);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.checks") + numOfChecks);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.absences") + absences);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.excused") + excuses);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.lowpercentage") + lowestPercentage + "%  " + lowestCheck.getCheckDate() + "  " + lowestCheck.getCheckTime());
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.highpercentage") + highestPercentage + "%  " + highestCheck.getCheckDate() + "  " + highestCheck.getCheckTime());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
