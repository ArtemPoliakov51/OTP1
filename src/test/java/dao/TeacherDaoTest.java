package dao;

import entity.Course;
import entity.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDaoTest {

    private static TeacherDao teacherDao;

    @BeforeAll
    static void setUp(){
        teacherDao = new TeacherDao();
    }

    @Test
    @DisplayName("TeacherDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert a new teacher to the database.");
        Teacher testTeacher = new Teacher("Test", "Teacher", "test@email.com", "password");
        teacherDao.persist(testTeacher);

        System.out.println("Try to find the inserted teacher from database.");
        int teacherId = testTeacher.getId();
        Teacher found = teacherDao.find(teacherId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(testTeacher, found);
        assertEquals("Test", found.getFirstname());
        assertEquals("Teacher", found.getLastname());
        assertEquals("test@email.com", found.getEmail());
        assertEquals("password", found.getPassword());

        System.out.println("Delete created teacher from the database.");
        teacherDao.delete(testTeacher);

        Teacher found2 = teacherDao.find(teacherId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
    }

    @Test
    @DisplayName("TeacherDAO delete() does not delete course test")
    void deleteTeacherNotCourse() {
        System.out.println("Create and insert a new teacher to the database.");
        Teacher testTeacher = new Teacher("Test", "Teacher", "test@email.com", "password");
        teacherDao.persist(testTeacher);

        System.out.println("Create and insert a new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", testTeacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        int teacherId = testTeacher.getId();
        int courseId = course.getId();

        System.out.println("Delete created teacher from the database.");
        teacherDao.delete(testTeacher);
        Teacher found2 = teacherDao.find(teacherId);
        System.out.println("Find function returned: " + found2);
        assertNull(found2);

        System.out.println("Make sure that course was not deleted from the database.");
        Course foundCourse = courseDao.find(courseId);
        System.out.println("Find function returned: " + foundCourse);
        assertNotNull(foundCourse);
    }

    @Test
    @DisplayName("CourseDAO update() + Course setName(), setIdentifier(), setArchived() and setStatus() test")
    void update() {
    }
}