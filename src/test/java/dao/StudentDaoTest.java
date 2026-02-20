package dao;

import entity.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudentDaoTest {

    private static StudentDao studentDao = new StudentDao();

    @BeforeEach
    void setUp(){
        datasource.MariaDBJpaConnection.getTestInstance();
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
        assertEquals(testStudent, found);
        assertEquals("Test", found.getFirstname());
        assertEquals("Student", found.getLastname());

        System.out.println("Delete created student from the database.");
        studentDao.delete(testStudent);

        Student found2 = studentDao.find(studentId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);
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

        studentDao.delete(testStudent);
    }
}
