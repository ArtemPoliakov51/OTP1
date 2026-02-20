package dao;

import entity.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class AttendanceCheckDaoTest {

    private static AttendanceCheckDao attCheckDao;
    private static Teacher teacher;

    @BeforeEach
    void setUp(){
        datasource.MariaDBJpaConnection.getTestInstance();
        attCheckDao = new AttendanceCheckDao();
        teacher = new Teacher("Test", "Teacher","test_" + System.nanoTime() + "@email.com", "superSecret111");
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.persist(teacher);
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("AttendanceCheckDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Create and insert a new attendance check to the database.");
        AttendanceCheck attCheck = new AttendanceCheck(course);
        attCheckDao.persist(attCheck);
        LocalDate testDate = LocalDate.now();
        LocalTime testTime = LocalTime.now();

        System.out.println("Try to find the inserted attendance check from database.");
        int attCheckId = attCheck.getId();
        AttendanceCheck found = attCheckDao.find(attCheckId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(attCheck, found);
        assertEquals(testDate, found.getCheckDate());
        assertEquals(testTime.getHour(), found.getCheckTime().getHour());
        assertEquals(testTime.getMinute(), found.getCheckTime().getMinute());
        assertEquals(course,found.getCourse());

        System.out.println("Delete created attendance check " + attCheckId + "from the database.");
        attCheckDao.delete(attCheck);

        AttendanceCheck found2 = attCheckDao.find(attCheckId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
    }

    @Test
    @DisplayName("AttendanceCheckDAO delete() does not delete course test")
    void deleteTeacherNotCourse() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Create and insert a new attendance check to the database.");
        AttendanceCheck attCheck = new AttendanceCheck(course);
        attCheckDao.persist(attCheck);

        System.out.println("Delete created attendance check from the database.");
        attCheckDao.delete(attCheck);
        AttendanceCheck found2 = attCheckDao.find(attCheck.getId());
        System.out.println("Find function returned: " + found2);
        assertNull(found2);

        System.out.println("Make sure that course was not deleted from the database.");
        Course foundCourse = courseDao.find(course.getId());
        System.out.println("Find function returned: " + foundCourse);
        assertNotNull(foundCourse);
    }

    @Test
    @DisplayName("TeacherDAO update() test")
    void update() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Create and insert a new attendance check to the database.");
        AttendanceCheck attCheck = new AttendanceCheck(course);
        attCheckDao.persist(attCheck);

        Course course2 = new Course("New Course", "NEW-2026-S1", teacher);
        courseDao.persist(course2);

        attCheck.setCheckDate(LocalDate.of(2026,2,10));
        attCheck.setCheckTime(LocalTime.of(18,10,40));
        attCheck.setCourse(course2);

        attCheckDao.update(attCheck);

        AttendanceCheck found = attCheckDao.find(attCheck.getId());
        System.out.println("Found attendance check: " + found);

        assertNotNull(found);
        assertEquals(LocalDate.of(2026,2,10), found.getCheckDate());
        assertEquals(LocalTime.of(18,10,40), found.getCheckTime());
        assertEquals(course2, found.getCourse());

        attCheckDao.delete(attCheck);
    }

}