package entity;

import dao.CourseDao;
import dao.TeacherDao;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private static Teacher testTeacher;
    private static Course testCourse;
    private static CourseDao courseDao;
    private static LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        datasource.MariaDBJpaConnection.getTestEntityManager();

        testTeacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                "test_" + System.nanoTime() + "@email.com", "superSecret111");
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.persist(testTeacher);
        testTime = LocalDateTime.now();
        courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course EN",
                "Test Course FI", "Test Course JA", "Test Course EL", "TEST-2026-S1", testTeacher.getId());
        testCourse = courseDao.find(courseId);
    }

    @AfterEach
    void cleanUp() {
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.delete(testTeacher.getId());
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("Course getName() test")
    void getName() {
        String name = testCourse.getName("fi");
        assertEquals("Test Course FI", name);
    }

    @Test
    @DisplayName("Course getIdentifier() test")
    void getIdentifier() {
        String ident = testCourse.getIdentifier();
        assertEquals("TEST-2026-S1", ident);
    }

    @Test
    @DisplayName("Course getStatus() test")
    void getStatus() {
        String status = testCourse.getStatus();
        System.out.println("New course status: " + status);
        assertEquals("ACTIVE", status);
    }

    @Test
    @DisplayName("Course getCreated() test")
    void getCreated() {
        LocalDateTime dateTime = testCourse.getCreated();
        System.out.println("Datetime comparison (test - found): " + testTime + " vs. " + dateTime);
        assertEquals(testTime.getYear(), dateTime.getYear());
        assertEquals(testTime.getMonth(), dateTime.getMonth());
        assertEquals(testTime.getDayOfMonth(), dateTime.getDayOfMonth());
        assertEquals(testTime.getHour(), dateTime.getHour());
        assertEquals(testTime.getMinute(), dateTime.getMinute());
    }

    @Test
    @DisplayName("Course getArchived() test")
    void getArchived() {
        LocalDateTime dateTime = testCourse.getArchived();
        System.out.println("New course archived date: " + dateTime);
        assertNull(dateTime);
    }
}
