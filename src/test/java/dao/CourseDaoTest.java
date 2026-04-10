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
        datasource.MariaDBJpaConnection.getTestEntityManager();

        teacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao = new TeacherDao();
        teacherDao.persist(teacher);
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("CourseDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course EN",
                "Test Course FI", "Test Course JA", "Test Course EL", "TEST-2026-S1", teacher.getId());

        System.out.println("Try to find the inserted course from database.");
        Course found = courseDao.find(courseId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(courseId, found.getId());
        assertEquals("Test Course EN", found.getName("en"));
        assertEquals("TEST-2026-S1", found.getIdentifier());
        assertEquals(teacher.getId(), found.getTeacher().getId());

        System.out.println("Delete created course from the database.");
        courseDao.delete(courseId);

        Course found2 = courseDao.find(courseId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);

        System.out.println("Make sure that teacher was not deleted from the database.");
        int teachId = teacher.getId();
        Teacher foundTeacher = teacherDao.find(teachId);
        System.out.println("Find function returned: " + foundTeacher);
        assertNotNull(foundTeacher);
    }

    @Test
    @DisplayName("CourseDAO findByTeacher() test")
    void findByTeacher() {
        System.out.println("Create and insert new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course EN",
                "Test Course FI", "Test Course JA", "Test Course EL", "TEST-2026-S1", teacher.getId());

        System.out.println("Try to find the course by the teacher.");
        List<Course> found = courseDao.findByTeacher(teacher.getId());
        System.out.println("Found courses: " + found);

        assertNotNull(found);
        assertEquals(courseId, found.get(0).getId());
        assertEquals("Test Course FI", found.get(0).getName("fi"));
        assertEquals("TEST-2026-S1", found.get(0).getIdentifier());
        assertEquals(teacher.getId(), found.get(0).getTeacher().getId());

        System.out.println("Create and insert more courses to the database.");
        int course2Id = courseDao.persist("Unit Testing EN", "Unit Testing FI","Unit Testing JA","Unit Testing EL","UT-2026-S2", teacher.getId());
        int course3Id = courseDao.persist("The Art of Testing EN", "The Art of Testing FI","The Art of Testing JA","The Art of Testing EL","AT-2026-S3", teacher.getId());

        System.out.println("Try again to find the courses by the teacher.");
        List<Course> found2 = courseDao.findByTeacher(teacher.getId());
        System.out.println("Found courses: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(courseId, found2.get(0).getId());
        assertEquals("Test Course EL", found2.get(0).getName("el"));
        assertEquals("TEST-2026-S1", found2.get(0).getIdentifier());
        assertEquals(teacher.getId(), found2.get(0).getTeacher().getId());

        assertEquals(course2Id, found2.get(1).getId());
        assertEquals("Unit Testing JA", found2.get(1).getName("ja"));
        assertEquals("UT-2026-S2", found2.get(1).getIdentifier());
        assertEquals(teacher.getId(), found2.get(1).getTeacher().getId());

        assertEquals(course3Id, found2.get(2).getId());
        assertEquals("The Art of Testing EN", found2.get(2).getName("en"));
        assertEquals("AT-2026-S3", found2.get(2).getIdentifier());
        assertEquals(teacher.getId(), found2.get(2).getTeacher().getId());

        courseDao.delete(courseId);
    }

    @Test
    @DisplayName("CourseDAO update() + Course setName(), setIdentifier(), setArchived() and setStatus() test")
    void update() {
        System.out.println("Create and insert new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course EN",
                "Test Course EN", "Test Course FI", "Test Course JA", "Test Course EL", teacher.getId());
        Course course = courseDao.find(courseId);

        System.out.println("Make changes to the course data and use the update method.");
        course.setNameFI("Advanced Test Course FI");
        course.setIdentifier("ATC-2026-S10");
        course.setArchived(LocalDateTime.of(2026,2,10,14,6,35));
        course.setStatus("ARCHIVED");

        courseDao.update(course);

        Course found = courseDao.find(course.getId());
        System.out.println("Found course: " + found);

        assertNotNull(found);
        assertEquals("Advanced Test Course FI", found.getName("fi"));
        assertEquals("ATC-2026-S10", found.getIdentifier());
        assertEquals(LocalDateTime.of(2026,2,10,14,6,35), found.getArchived());
        assertEquals("ARCHIVED", found.getStatus());
    }
}