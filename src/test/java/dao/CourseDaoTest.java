package dao;

import entity.Course;
import entity.Teacher;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest {

    private static Teacher teacher;
    private static TeacherDao teacherDao;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("Test", "Teacher","test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao = new TeacherDao();
        teacherDao.persist(teacher);
    }

    @Test
    @DisplayName("CourseDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Try to find the inserted course from database.");
        int courseId = course.getId();
        Course found = courseDao.find(courseId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(course, found);
        assertEquals("Test Course", found.getName());
        assertEquals("TEST-2026-S1", found.getIdentifier());
        assertEquals(teacher, found.getTeacher());

        System.out.println("Delete created course from the database.");
        courseDao.delete(course);

        Course found2 = courseDao.find(courseId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);

        System.out.println("Make sure that teacher was not deleted from the database.");
        int teachId = teacher.getId();
        Teacher foundTeacher = teacherDao.find(teachId);
        System.out.println("Find function returned: " + foundTeacher);
        assertNotNull(foundTeacher);
    }

    // ADD: Test for checking that deleting a course also deletes all attendance checks for it

    @Test
    @DisplayName("CourseDAO findByTeacher() test")
    void findByTeacher() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Try to find the course by the teacher.");
        List<Course> found = courseDao.findByTeacher(teacher);
        System.out.println("Found courses: " + found);

        assertNotNull(found);
        assertEquals(course, found.get(0));
        assertEquals("Test Course", found.get(0).getName());
        assertEquals("TEST-2026-S1", found.get(0).getIdentifier());
        assertEquals(teacher, found.get(0).getTeacher());

        System.out.println("Create and insert more courses to the database.");
        Course course2 = new Course("Unit Testing", "UT-2026-S2", teacher);
        Course course3 = new Course("The Art of Testing", "AT-2026-S3", teacher);
        courseDao.persist(course2);
        courseDao.persist(course3);

        System.out.println("Try again to find the courses by the teacher.");
        List<Course> found2 = courseDao.findByTeacher(teacher);
        System.out.println("Found courses: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(course, found2.get(0));
        assertEquals("Test Course", found2.get(0).getName());
        assertEquals("TEST-2026-S1", found2.get(0).getIdentifier());
        assertEquals(teacher, found2.get(0).getTeacher());

        assertEquals(course2, found2.get(1));
        assertEquals("Unit Testing", found2.get(1).getName());
        assertEquals("UT-2026-S2", found2.get(1).getIdentifier());
        assertEquals(teacher, found2.get(1).getTeacher());

        assertEquals(course3, found2.get(2));
        assertEquals("The Art of Testing", found2.get(2).getName());
        assertEquals("AT-2026-S3", found2.get(2).getIdentifier());
        assertEquals(teacher, found2.get(2).getTeacher());

        courseDao.delete(course);
    }

    @Test
    @DisplayName("CourseDAO update() + Course setName(), setIdentifier(), setArchived() and setStatus() test")
    void update() {
        System.out.println("Create and insert new course to the database.");
        Course course = new Course("Test Course", "TEST-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        System.out.println("Make changes to the course data and use the update method.");
        course.setName("Advanced Test Course");
        course.setIdentifier("ATC-2026-S10");
        course.setArchived(LocalDateTime.of(2026,2,10,14,6,35));
        course.setStatus("ARCHIVED");

        courseDao.update(course);

        Course found = courseDao.find(course.getId());
        System.out.println("Found course: " + found);

        assertNotNull(found);
        assertEquals("Advanced Test Course", found.getName());
        assertEquals("ATC-2026-S10", found.getIdentifier());
        assertEquals(LocalDateTime.of(2026,2,10,14,6,35), found.getArchived());
        assertEquals("ARCHIVED", found.getStatus());

        courseDao.delete(course);
    }
}