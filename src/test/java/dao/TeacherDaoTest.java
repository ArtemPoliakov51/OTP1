package dao;

import entity.Course;
import entity.Teacher;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TeacherDaoTest {

    private final static TeacherDao teacherDao = new TeacherDao();

    @BeforeEach
    void setUp(){
        datasource.MariaDBJpaConnection.getTestEntityManager();
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("TeacherDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert a new teacher to the database.");
        Teacher testTeacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao.persist(testTeacher);

        System.out.println("Try to find the inserted teacher from database.");
        int teacherId = testTeacher.getId();
        Teacher found = teacherDao.find(teacherId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(testTeacher.getId(), found.getId());
        assertEquals("Test_JA", found.getFirstname("ja"));
        assertEquals("Teacher_EL", found.getLastname("el"));
        assertEquals("superSecret111", found.getPassword());

        System.out.println("Delete created teacher from the database.");
        teacherDao.delete(testTeacher.getId());

        Teacher found2 = teacherDao.find(teacherId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
    }

    @Test
    @DisplayName("TeacherDAO findByEmail() test")
    void findByEmail() {
        System.out.println("Create and insert a new teacher to the database.");
        String email = "testTeacher_" + System.nanoTime() + "@email.com";
        Teacher testTeacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                email, "superSecret111");
        teacherDao.persist(testTeacher);

        Teacher found = teacherDao.findByEmail(email);
        System.out.println("Found teacher: " + found);

        assertNotNull(found);
        assertEquals(testTeacher.getId(), found.getId());
        assertEquals("Test_EN", found.getFirstname("en"));
        assertEquals("Teacher_FI", found.getLastname("fi"));
        assertEquals(email, found.getEmail());
        assertEquals("superSecret111", found.getPassword());
    }

    @Test
    @DisplayName("TeacherDAO delete() does not delete course test")
    void deleteTeacherNotCourse() {
        System.out.println("Create and insert a new teacher to the database.");
        Teacher testTeacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao.persist(testTeacher);

        System.out.println("Create and insert a new course to the database.");
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Test Course EN",
                "Test Course EN", "Test Course FI", "Test Course JA", "Test Course EL", testTeacher.getId());

        int teacherId = testTeacher.getId();

        System.out.println("Delete created teacher from the database.");
        teacherDao.delete(testTeacher.getId());
        Teacher found2 = teacherDao.find(teacherId);
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
        System.out.println("Create and insert a new teacher to the database.");
        Teacher testTeacher = new Teacher("Test_EN", "Test_FI", "Test_JA", "Test_EL",
                "Teacher_EN", "Teacher_FI", "Teacher_JA", "Teacher_EL",
                "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao.persist(testTeacher);

        testTeacher.setFirstname("ja","New JA");
        testTeacher.setLastname("en","Lastname EN");
        testTeacher.setEmail("testTeacher_" + testTeacher.getId() + "@email.com");
        testTeacher.setPassword("newpassword");

        teacherDao.update(testTeacher);

        Teacher found = teacherDao.find(testTeacher.getId());
        System.out.println("Found teacher: " + found);

        assertNotNull(found);
        assertEquals("New JA", found.getFirstname("ja"));
        assertEquals("Lastname EN", found.getLastname("en"));
        assertEquals("testTeacher_" + testTeacher.getId() + "@email.com", found.getEmail());
        assertEquals("newpassword", found.getPassword());

        teacherDao.delete(testTeacher.getId());
    }
}