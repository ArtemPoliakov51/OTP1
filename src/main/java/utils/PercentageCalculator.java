package utils;

import dao.ChecksDao;
import entity.AttendanceCheck;
import entity.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Utility class for calculating attendance percentages for courses and attendance checks.
 *
 * <p>This class provides methods for calculating the overall attendance percentages
 * for courses and individual attendance checks.</p>
 *
 */
public class PercentageCalculator {

    /**
     * Calculates the overall attendance percentage for a course.
     *
     * <p>The result is computed as the average of all attendance check percentages.</p>
     *
     * @return overall course attendance percentage
     */
    public static int countCourseAttendancePercentage(List<AttendanceCheck> attendanceChecks) {
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
     * Calculates the attendance percentage for a single attendance check.
     *
     * @param attCheck the attendance check to analyze
     * @return attendance percentage for the given check
     */
    public static double countAttendanceCheckPercentage(AttendanceCheck attCheck) {
        ChecksDao checksDao = new ChecksDao();
        List<Checks> checks = checksDao.findByAttendanceCheck(attCheck.getId());
        // Go through all of them, and if student was present add it to a new list
        List<Checks> present = new ArrayList<>();
        for (Checks checksCheck : checks) {
            if (Objects.equals(checksCheck.getAttendanceStatus(), "PRESENT")) {
                present.add(checksCheck);
            }
        }
        // Count the attendance percentage for this attendance check
        double attCheckPercentage;
        if (!checks.isEmpty()) {
            attCheckPercentage = (double) present.size() / (double) checks.size() * 100;
        } else {
            attCheckPercentage = 0;
        }
        return (int) attCheckPercentage;
    }
}
