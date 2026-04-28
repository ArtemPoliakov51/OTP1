package controller;

import dao.*;
import entity.*;
import service.I18nManager;
import utils.PercentageCalculator;
import view.CourseAttendanceReportView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller responsible for generating and managing course attendance reports.
 *
 * <p>This class calculates attendance statistics for a course, including overall attendance
 * percentage, absences, excused absences, and identifies the attendance check with the
 * highest and lowest attendance rates.</p>
 *
 * <p>It also handles displaying report data in the view and exporting the report
 * to a text file.</p>
 */
public class CourseAttendanceReportController {

    /** The ID of the course for which the report is generated. */
    private final int courseId;

    /** DAO used for accessing course data from the database. */
    private final CourseDao courseDao = new CourseDao();

    /** DAO used for accessing attendance check data from the database. */
    private final AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    /** The view responsible for displaying the attendance report. */
    private final CourseAttendanceReportView view;

    /**
     * Logger used for recording events and errors related to the generation,
     * calculation, and export of course attendance reports.
     *
     * <p>This logger provides structured logging in place of direct stack trace
     * output, enabling better diagnostics and maintainability in production
     * environments.</p>
     */
    private static final Logger LOGGER =
            Logger.getLogger(CourseAttendanceReportController.class.getName());

    /**
     * Constructs a new CourseAttendanceReportController.
     *
     * @param reportView the view used to display attendance report data
     * @param courseId the ID of the course for which the report is generated
     */
    public CourseAttendanceReportController(CourseAttendanceReportView reportView, int courseId) {
        this.courseId = courseId;
        this.view = reportView;
    }

    /**
     * Retrieves course identifier and name and sends them to the view.
     * Used for displaying report header information.
     */
    public void updateViewInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        view.displayCourseIdentifierAndName(course.getIdentifier(), course.getName(lang));
    }

    /**
     * Calculates the overall attendance percentage for the course.
     *
     * <p>The result is computed as the average of all attendance check percentages.</p>
     *
     * @return overall course attendance percentage
     */
    private int getAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        return PercentageCalculator.countCourseAttendancePercentage(attendanceChecks);
    }

    /**
     * Displays the overall course attendance percentage in the view.
     */
    public void showAttendancePercentage() {
        view.displayCourseAttendancePercentage(getAttendancePercentage());
    }

    /**
     * Counts all absent attendance records for the course.
     *
     * @return number of absences
     */
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

    /**
     * Counts all excused absence records for the course.
     *
     * @return number of excused absences
     */
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

    /**
     * Finds the attendance check with the lowest attendance percentage.
     *
     * @return attendance check with the lowest attendance rate
     */
    private AttendanceCheck findCheckWithLowestAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        double currentLowest = 100;
        AttendanceCheck lowestAttendanceCheck = null;
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            double percentage = PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck);
            if (percentage <= currentLowest) {
                currentLowest = percentage;
                lowestAttendanceCheck = attendanceCheck;
            }
        }
        return lowestAttendanceCheck;
    }


    /**
     * Finds the attendance check with the highest attendance percentage.
     *
     * @return attendance check with the highest attendance rate
     */
    private AttendanceCheck findCheckWithHighestAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        double currentHighest = 0;
        AttendanceCheck highestAttendanceCheck = null;
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            double percentage = PercentageCalculator.countAttendanceCheckPercentage(attendanceCheck);
            if (percentage >= currentHighest) {
                currentHighest = percentage;
                highestAttendanceCheck = attendanceCheck;
            }
        }
        return highestAttendanceCheck;
    }

    /**
     * Displays summary statistics of the course attendance report in the view.
     */
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

        // Only count the attendance percentages if checks were found
        double lowestPercentage;
        LocalDate lowestDate;
        LocalTime lowestTime;
        if (lowestCheck != null) {
            lowestPercentage = PercentageCalculator.countAttendanceCheckPercentage(lowestCheck);
            lowestDate = lowestCheck.getCheckDate();
            lowestTime = lowestCheck.getCheckTime();
        } else {
            lowestPercentage = 0;
            lowestDate = null;
            lowestTime = null;
        }

        double highestPercentage;
        LocalDate highestDate;
        LocalTime highestTime;
        if (highestCheck != null) {
            highestPercentage = PercentageCalculator.countAttendanceCheckPercentage(highestCheck);
            highestDate = highestCheck.getCheckDate();
            highestTime = highestCheck.getCheckTime();
        } else {
            highestPercentage = 0;
            highestDate = null;
            highestTime = null;
        }

        view.displayCourseReportLinesPartOne(numOfStudents, numOfChecks, absences, excuses);
        view.displayCourseReportLinesPartTwo(lowestPercentage, lowestDate, lowestTime,
                highestPercentage, highestDate, highestTime);
    }

    /**
     * Generates and saves the attendance report as a text file.
     *
     * <p>The report includes course information, attendance statistics,
     * and formatted timestamps.</p>
     *
     * @param destinationFile directory where the report file will be saved
     */
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

        // Only count the attendance percentages if checks were found
        double lowestPercentage;
        LocalDate lowestDate;
        LocalTime lowestTime;
        if (lowestCheck != null) {
            lowestPercentage = PercentageCalculator.countAttendanceCheckPercentage(lowestCheck);
            lowestDate = lowestCheck.getCheckDate();
            lowestTime = lowestCheck.getCheckTime();
        } else {
            lowestPercentage = 0;
            lowestDate = null;
            lowestTime = null;
        }

        double highestPercentage;
        LocalDate highestDate;
        LocalTime highestTime;
        if (highestCheck != null) {
            highestPercentage = PercentageCalculator.countAttendanceCheckPercentage(highestCheck);
            highestDate = highestCheck.getCheckDate();
            highestTime = highestCheck.getCheckTime();
        } else {
            highestPercentage = 0;
            highestDate = null;
            highestTime = null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(I18nManager.getCurrentLocale());
        DateTimeFormatter formatter2 = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(I18nManager.getCurrentLocale());

        String localizedLowestDate = lowestDate != null ? lowestDate.format(formatter) : "";
        String localizedHighestDate = highestDate != null ? highestDate.format(formatter) : "";

        String localizedLowestTime = lowestTime != null ? lowestTime.format(formatter2) : "";
        String localizedHighestTime = highestTime != null ? highestTime.format(formatter2) : "";

        try (
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile.getPath() + "/" + course.getIdentifier() + "_attendance_report_"
                    + LocalDateTime.now().getDayOfYear() + LocalDateTime.now().getMonthValue() + LocalDateTime.now().getYear()
                    + "_" +LocalDateTime.now().getHour() + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond()
                    + ".txt"))) {
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
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.percentage") + getAttendancePercentage() + "%");
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.students") + numOfStudents);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.checks") + numOfChecks);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.absences") + absences);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.label.excused") + excuses);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager
                    .getResourceBundle()
                    .getString("coursereport.label.lowpercentage")
                    + lowestPercentage + "%  " + localizedLowestDate + "  " + localizedLowestTime);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager
                    .getResourceBundle()
                    .getString("coursereport.label.highpercentage")
                    + highestPercentage + "%  " + localizedHighestDate + "  " + localizedHighestTime);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create and save attendance report", e);
        }
    }
}
