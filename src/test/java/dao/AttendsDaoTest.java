package dao;

import entity.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendsDaoTest {

    private Teacher teacher;
    private TeacherDao teacherDao;

    @BeforeEach
    void setUp() {
        datasource.MariaDBJpaConnection.getTestEntityManager();
        teacherDao = new TeacherDao();
        teacher = new Teacher("Test", "Teacher", "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao.persist(teacher);
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }

    @Test
    @DisplayName("AttendsDAO persist() and find() test")
    void persistAndFindAndDelete() {
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Attends Course", "TEST", teacher.getId());

        Student student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        StudentDao studentDao = new StudentDao();
        studentDao.persist(student);

        System.out.println("Create and insert new attends data to the database.");
        AttendsDao attendsDao = new AttendsDao();
        AttendsId attendsId = attendsDao.persist(courseId, student.getId());

        System.out.println("Try to find the inserted data from database.");
        Attends found = attendsDao.find(attendsId.getCourseId(), attendsId.getStudentId());
        System.out.println("Find function returned: " + found.getId().getStudentId());

        assertNotNull(found);
        assertEquals(attendsId.getCourseId(), found.getId().getCourseId());
        assertEquals(attendsId.getStudentId(), found.getId().getStudentId());
        assertEquals(courseId, found.getCourse().getId());
        assertEquals(student.getId(), found.getStudent().getId());
    }

    @Test
    @DisplayName("AttendsDAO delete() test")
    void delete() {
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Attends Delete Course", "TEST", teacher.getId());

        Student student = new Student("Attends", "Delete Student", "student_" + System.nanoTime() + "@email.com");
        StudentDao studentDao = new StudentDao();
        studentDao.persist(student);

        System.out.println("Create and insert new attends data to the database.");
        AttendsDao attendsDao = new AttendsDao();
        AttendsId attendsId = attendsDao.persist(courseId, student.getId());

        System.out.println("Delete created attends data from the database.");
        attendsDao.delete(attendsId.getCourseId(), attendsId.getStudentId());

        AttendsDao attendsDao2 = new AttendsDao();
        Attends found = attendsDao2.find(attendsId.getCourseId(), attendsId.getStudentId());
        System.out.println("Find function returned: " + found);

        assertNull(found);

        System.out.println("Make sure that course was not deleted from the database.");
        Course foundCourse = courseDao.find(courseId);
        System.out.println("Find function returned: " + foundCourse);
        assertNotNull(foundCourse);

        System.out.println("Make sure that student was not deleted from the database.");
        Student foundStudent = studentDao.find(student.getId());
        System.out.println("Find function returned: " + foundStudent);
        assertNotNull(foundStudent);
    }

    @Test
    @DisplayName("AttendsDAO findByCourse() test")
    void findByCourse() {
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Attends Course", "TEST", teacher.getId());

        Student student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        StudentDao studentDao = new StudentDao();
        studentDao.persist(student);

        System.out.println("Create and insert new attends data to the database.");
        AttendsDao attendsDao = new AttendsDao();
        AttendsId attendsId = attendsDao.persist(courseId, student.getId());

        System.out.println("Try to find the attends data by the course.");
        List<Attends> found = attendsDao.findByCourse(courseId);
        System.out.println("Found attends: " + found);

        Attends attends = attendsDao.find(courseId, student.getId());

        assertNotNull(found);
        assertEquals(attendsId.getCourseId(), found.get(0).getId().getCourseId());
        assertEquals(attendsId.getStudentId(), found.get(0).getId().getStudentId());
        assertEquals(courseId, found.get(0).getCourse().getId());
        assertEquals(student.getId(), found.get(0).getStudent().getId());

        Student student2 = new Student("Test2", "Student2", "anotherEmail@email.com");
        Student student3 = new Student("Test3", "Student3", "email@email.com");
        studentDao.persist(student2);
        studentDao.persist(student3);

        System.out.println("Create and insert more courses to the database.");
        AttendsId attendsId2 = attendsDao.persist(courseId, student2.getId());
        AttendsId attendsId3 = attendsDao.persist(courseId, student3.getId());

        System.out.println("Try again to find the attends data by the course.");
        List<Attends> found2 = attendsDao.findByCourse(courseId);
        System.out.println("Found attends data: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(attendsId.getCourseId(), found2.get(0).getId().getCourseId());
        assertEquals(attendsId.getStudentId(), found2.get(0).getId().getStudentId());

        assertEquals(attendsId2.getCourseId(), found2.get(1).getId().getCourseId());
        assertEquals(attendsId2.getStudentId(), found2.get(1).getId().getStudentId());

        assertEquals(attendsId3.getCourseId(), found2.get(2).getId().getCourseId());
        assertEquals(attendsId3.getStudentId(), found2.get(2).getId().getStudentId());
    }

    @Test
    @DisplayName("AttendsDAO findByStudent() test")
    void findByStudent() {
        CourseDao courseDao = new CourseDao();
        int courseId = courseDao.persist("Attends Course", "TEST", teacher.getId());

        Student student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        StudentDao studentDao = new StudentDao();
        studentDao.persist(student);

        System.out.println("Create and insert new attends data to the database.");
        AttendsDao attendsDao = new AttendsDao();
        AttendsId attendsId = attendsDao.persist(courseId, student.getId());

        System.out.println("Try to find the attends data by the course.");
        List<Attends> found = attendsDao.findByStudent(student);
        System.out.println("Found attends: " + found);

        assertNotNull(found);
        assertEquals(attendsId.getCourseId(), found.get(0).getId().getCourseId());
        assertEquals(attendsId.getStudentId(), found.get(0).getId().getStudentId());
        assertEquals(courseId, found.get(0).getCourse().getId());
        assertEquals(student.getId(), found.get(0).getStudent().getId());
    }

}