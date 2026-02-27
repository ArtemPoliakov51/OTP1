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
        datasource.MariaDBJpaConnection.getTestEntityManager();
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
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course", "TEST-2026-S1", teacher.getId());
        Course course = courseDao.find(courseId);

        System.out.println("Create and insert a new attendance check to the database.");
        int attCheckId = attCheckDao.persist(courseId);
        LocalDate testDate = LocalDate.now();
        LocalTime testTime = LocalTime.now();

        System.out.println("Try to find the inserted attendance check from database.");
        AttendanceCheck found = attCheckDao.find(attCheckId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(testDate, found.getCheckDate());
        assertEquals(testTime.getHour(), found.getCheckTime().getHour());
        assertEquals(testTime.getMinute(), found.getCheckTime().getMinute());
        assertEquals(courseId,found.getCourse().getId());

        System.out.println("Delete created attendance check " + attCheckId + "from the database.");
        attCheckDao.delete(attCheckId);

        AttendanceCheck found2 = attCheckDao.find(attCheckId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
    }

    @Test
    @DisplayName("AttendanceCheckDAO delete() does not delete course test")
    void deleteTeacherNotCourse() {
        System.out.println("Create and insert new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course", "TEST-2026-S1", teacher.getId());

        System.out.println("Create and insert a new attendance check to the database.");
        int attCheckId = attCheckDao.persist(courseId);

        System.out.println("Delete created attendance check from the database.");
        attCheckDao.delete(attCheckId);
        AttendanceCheck found2 = attCheckDao.find(attCheckId);
        System.out.println("Find function returned: " + found2);
        assertNull(found2);

        System.out.println("Make sure that course was not deleted from the database.");
        Course foundCourse = courseDao.find(courseId);
        System.out.println("Find function returned: " + foundCourse);
        assertNotNull(foundCourse);
    }

    @Test
    @DisplayName("TeacherDAO update() test")
    void update() {
        System.out.println("Create and insert new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course", "TEST-2026-S1", teacher.getId());

        System.out.println("Create and insert a new attendance check to the database.");
        int attCheckId = attCheckDao.persist(courseId);
        AttendanceCheck attCheck = attCheckDao.find(attCheckId);

        int course2Id = courseDao.persist("New Course", "NEW-2026-S1", teacher.getId());
        Course course2 = courseDao.find(course2Id);

        attCheck.setCheckDate(LocalDate.of(2026,2,10));
        attCheck.setCheckTime(LocalTime.of(18,10,40));
        attCheck.setCourse(course2);

        attCheckDao.update(attCheck);

        AttendanceCheck found = attCheckDao.find(attCheck.getId());
        System.out.println("Found attendance check: " + found);

        assertNotNull(found);
        assertEquals(LocalDate.of(2026,2,10), found.getCheckDate());
        assertEquals(LocalTime.of(18,10,40), found.getCheckTime());
        assertEquals(course2Id, found.getCourse().getId());

        attCheckDao.delete(attCheckId);
    }

}