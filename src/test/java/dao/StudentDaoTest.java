package dao;

import entity.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDaoTest {

    private static StudentDao studentDao = new StudentDao();

    @BeforeEach
    void setUp(){
        datasource.MariaDBJpaConnection.getTestEntityManager();
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("StudentDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert a new student to the database.");
        Student testStudent = new Student("Test", "Student", "testStudent_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        System.out.println("Try to find the inserted student from database.");
        int studentId = testStudent.getId();
        Student found = studentDao.find(studentId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(testStudent.getId(), found.getId());
        assertEquals("Test", found.getFirstname());
        assertEquals("Student", found.getLastname());

        System.out.println("Delete created student from the database.");
        studentDao.delete(testStudent.getId());

        Student found2 = studentDao.find(studentId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
    }

    @Test
    @DisplayName("StudentDAO findAll() test")
    void findAll() {
        System.out.println("Create and insert new students to the database.");
        Student testStudent = new Student("Test", "Student", "testStudent_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        Student testStudent2 = new Student("Test", "Student2", "testStudent2_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent2);

        Student testStudent3 = new Student("Test", "Student3", "testStudent3_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent3);

        List<Student> found = studentDao.findAll();
        System.out.println("Found students: " + found);

        assertNotNull(found);
        assertEquals(3, found.size());
        assertEquals(testStudent.getId(), found.get(0).getId());
        assertEquals(testStudent2.getId(), found.get(1).getId());
        assertEquals(testStudent3.getId(), found.get(2).getId());
    }

    @Test
    @DisplayName("StudentDAO update() test")
    void update() {
        System.out.println("Create and insert a new student to the database.");
        Student testStudent = new Student("Test", "Student", "testStudent_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        testStudent.setFirstname("New");
        testStudent.setLastname("Name");
        testStudent.setEmail("new@email.com");

        studentDao.update(testStudent);

        Student found = studentDao.find(testStudent.getId());
        System.out.println("Found student: " + found);

        assertNotNull(found);
        assertEquals("New", found.getFirstname());
        assertEquals("Name", found.getLastname());
        assertEquals("new@email.com", found.getEmail());
    }
}
