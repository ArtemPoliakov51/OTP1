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
        Student testStudent = new Student("Test EN", "Test FI", "Test JA", "Test EL",
                "Student EN", "Student FI", "Student JA", "Student EL",
                "student_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        System.out.println("Try to find the inserted student from database.");
        int studentId = testStudent.getId();
        Student found = studentDao.find(studentId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(testStudent.getId(), found.getId());
        assertEquals("Test EN", found.getFirstname("en"));
        assertEquals("Student JA", found.getLastname("ja"));

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
        Student testStudent = new Student("Test EN", "Test FI", "Test JA", "Test EL",
                "Student EN", "Student FI", "Student JA", "Student EL",
                "student_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        Student testStudent2 = new Student("Test2 EN", "Test2 FI", "Test2 JA", "Test2 EL",
                "Student2 EN", "Student2 FI", "Student2 JA", "Student2 EL",
                "student_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent2);

        Student testStudent3 = new Student("Test3 EN", "Test3 FI", "Test3 JA", "Test3 EL",
                "Student3 EN", "Student3 FI", "Student3 JA", "Student3 EL",
                "student_" + System.nanoTime() + "@email.com");
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
        Student testStudent = new Student("Test EN", "Test FI", "Test JA", "Test EL",
                "Student EN", "Student FI", "Student JA", "Student EL",
                "student_" + System.nanoTime() + "@email.com");
        studentDao.persist(testStudent);

        testStudent.setFirstname("el","New EL");
        testStudent.setLastname("fi", "Lastname FI");
        testStudent.setEmail("new@email.com");

        studentDao.update(testStudent);

        Student found = studentDao.find(testStudent.getId());
        System.out.println("Found student: " + found);

        assertNotNull(found);
        assertEquals("New EL", found.getFirstname("el"));
        assertEquals("Lastname FI", found.getLastname("fi"));
        assertEquals("new@email.com", found.getEmail());
    }
}
