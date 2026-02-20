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
        datasource.MariaDBJpaConnection.getTestInstance();

        testTeacher = new Teacher("Test", "Teacher", "testTeacher_" + System.nanoTime() + "@email.com", "password");
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.persist(testTeacher);
        testCourse = new Course("Test Course", "TEST-2026-S1", testTeacher);
        testTime = LocalDateTime.now();
        courseDao = new CourseDao();
        courseDao.persist(testCourse);
    }

    @AfterEach
    void cleanUp() {
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.delete(testTeacher);
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("Course getName() test")
    void getName() {
        String name = testCourse.getName();
        assertEquals("Test Course", name);
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
