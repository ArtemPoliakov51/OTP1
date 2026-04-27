package controller;

import dao.*;
import entity.*;
import i18n.I18nManager;
import view.StudentAttendanceReportView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Controller responsible for generating and managing a student's attendance report
 * for a specific course.
 *
 * <p>This class calculates attendance statistics for an individual student, including
 * attendance percentage, absences, and excused absences. It also provides detailed
 * per-check absence history and supports exporting the report to a file.</p>
 *
 * <p>It communicates with DAO classes to retrieve course, student, and attendance data,
 * and passes formatted information to the view.</p>
 */
public class StudentAttendanceReportController {

    /** The ID of the selected course. */
    private final int courseId;

    /** The ID of the selected student. */
    private final int studentId;

    /** DAO used for accessing course data. */
    private final CourseDao courseDao = new CourseDao();

    /** DAO used for accessing student data. */
    private final StudentDao studentDao = new StudentDao();

    /** DAO used for accessing attendance check data. */
    private final AttendanceCheckDao attendanceCheckDao = new AttendanceCheckDao();

    /** View responsible for displaying student attendance report data. */
    private final StudentAttendanceReportView view;

    /** The ID of the currently logged-in teacher. */
    private final int teacherId;

    /**
     * Constructs a new StudentAttendanceReportController.
     *
     * @param reportView the view used to display the student report
     * @param courseId the ID of the course
     * @param studentId the ID of the student
     */
    public StudentAttendanceReportController(StudentAttendanceReportView reportView, int courseId, int studentId) {
        this.courseId = courseId;
        this.studentId = studentId;
        this.view = reportView;
        this.teacherId = LoginController.getInstance().getLoggedInTeacherId();
    }

    /**
     * Retrieves and displays course and student information in the view.
     */
    public void updateViewInfo() {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        Student student = studentDao.find(studentId);
        view.displayCourseIdentifierAndName(course.getIdentifier(), course.getName(lang));
        view.displayStudentInfo(student.getFirstname(lang), student.getLastname(lang), student.getId());
    }

    /**
     * Calculates the attendance percentage of the student in the course.
     *
     * @return attendance percentage (0–100)
     */
    private int countStudentAttendancePercentage() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

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

    /**
     * Displays the student's attendance percentage in the view.
     */
    public void showAttendancePercentage() {
        view.displayAttendancePercentage(countStudentAttendancePercentage());
    }

    /**
     * Displays all absence and excused absence records for the student.
     *
     * <p>Results are sorted chronologically by date and time.</p>
     */
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

    /**
     * Finds all ABSENT records for the selected student in the course.
     *
     * @return list of absent checks
     */
    private List<Checks> findAllAbsences() {
        Student student = studentDao.find(studentId);
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allStudentsChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck.getId());
            for (Checks aChecks : checksList) {
                if (aChecks.getStudent().getId() == student.getId()) {
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

    /**
     * Finds all EXCUSED absence records for the selected student in the course.
     *
     * @return list of excused checks
     */
    private List<Checks> findAllExcuses() {
        Student student = studentDao.find(studentId);
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);

        ChecksDao checksDao = new ChecksDao();
        List<Checks> allStudentsChecks = new ArrayList<>();
        for (AttendanceCheck attendanceCheck : attendanceChecks) {
            List<Checks> checksList = checksDao.findByAttendanceCheck(attendanceCheck.getId());
            for (Checks aChecks : checksList) {
                if (aChecks.getStudent().getId() == student.getId()) {
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

    /**
     * Displays summary statistics for the student's attendance report.
     */
    public void showStudentReportLines() {
        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        int numOfChecks = attendanceChecks.size();

        int absences = findAllAbsences().size();
        int excuses = findAllExcuses().size();

        view.displayStudentReportLines(numOfChecks, absences, excuses);
    }

    /**
     * Generates and saves a detailed attendance report file for the student.
     *
     * <p>The report includes course details, student information, statistics,
     * and a full list of absence and excused absence records.</p>
     *
     * @param destinationFile directory where the report file will be saved
     */
    public void createAndSaveResults(File destinationFile) {
        String lang = I18nManager.getCurrentLocale().getLanguage();
        Course course = courseDao.find(courseId);
        Student student = studentDao.find(studentId);

        List<AttendanceCheck> attendanceChecks = attendanceCheckDao.findByCourse(courseId);
        int numOfChecks = attendanceChecks.size();

        int absences = findAllAbsences().size();
        int excuses = findAllExcuses().size();

        List<Checks> allAbsences = new ArrayList<>();
        allAbsences.addAll(findAllAbsences());
        allAbsences.addAll(findAllExcuses());

        try (
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter(
                            destinationFile.getPath() + "/" + course.getIdentifier()
                                    + "_student_" + student.getId() + "_attendance_report_"
                                    + LocalDateTime.now().getDayOfYear() + LocalDateTime.now().getMonthValue()
                                    + LocalDateTime.now().getYear() + "_" +LocalDateTime.now().getHour()
                                    + LocalDateTime.now().getMinute() + LocalDateTime.now().getSecond() + ".txt"))) {
            bufferedWriter.write(I18nManager.getResourceBundle().getString("coursereport.title").toUpperCase() + "   " +  LocalDate.now());
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.course") + course.getIdentifier() + " - " + course.getName(lang));
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.student") + student.getId());
            bufferedWriter.newLine();
            bufferedWriter.write(student.getFirstname(lang) + " " + student.getLastname(lang));
            bufferedWriter.newLine();
            bufferedWriter.write(student.getEmail());
            bufferedWriter.newLine();
            bufferedWriter.write("---------------------------------------------------------------");
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.statistics"));
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.percentage") + countStudentAttendancePercentage() + "%");
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("studentreport.label.checks") + numOfChecks);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("studentreport.label.absences") + absences);
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("studentreport.label.excuses") + excuses);
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.allabsences"));
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (Checks aChecks : allAbsences) {
                LocalTime time =  aChecks.getAttendanceCheck().getCheckTime();
                String correctMin = time.getMinute() < 10 ? "0" + time.getMinute() : Integer.toString(time.getMinute());
                bufferedWriter.write(aChecks.getAttendanceCheck().getCheckDate() + "  " + time.getHour() + ":" + correctMin + " -------------- " +
                        (aChecks.getAttendanceStatus().equals("ABSENT") ?
                                I18nManager.getResourceBundle().getString("studentreport.label.absent") :
                                I18nManager.getResourceBundle().getString("studentreport.label.excused")));
                bufferedWriter.newLine();
                bufferedWriter.write(I18nManager.getResourceBundle().getString("reportcontroller.text.notes") + (aChecks.getNotes() != null ? aChecks.getNotes() : ""));
                bufferedWriter.newLine();
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error while writing attendance report file");
        }
    }
}
