package dao;

import datasource.MariaDBJpaConnection;
import entity.Attends;
import entity.Course;
import entity.Student;
import entity.Teacher;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AttendsDaoTest {

    private Course course;
    private CourseDao courseDao;

    private Student student;
    private StudentDao studentDao;

    private Teacher teacher;
    private TeacherDao teacherDao;

    @BeforeEach
    void setUp() {

        teacherDao = new TeacherDao();
        courseDao = new CourseDao();
        studentDao = new StudentDao();

        teacher = new Teacher("Test", "Teacher", "test_" + System.nanoTime() + "@email.com", "superSecret111");
        teacherDao.persist(teacher);

        course = new Course("Attends Course", "TEST", teacher);
        courseDao.persist(course);

        student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        studentDao.persist(student);
    }

    @Test
    @DisplayName("AttendsDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert new attends data to the database.");
        Attends attends = new Attends(course, student);
        AttendsDao attendsDao = new AttendsDao();
        attendsDao.persist(attends);

        System.out.println("Try to find the inserted data from database.");
        int attendsId = attends.getId();
        Attends found = attendsDao.find(attendsId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(attends, found);
        assertEquals(course, found.getCourse());
        assertEquals(student, found.getStudent());

        System.out.println("Delete created attends data from the database.");
        attendsDao.delete(attends);

        Attends found2 = attendsDao.find(attendsId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);

        System.out.println("Make sure that course was not deleted from the database.");
        Course foundCourse = courseDao.find(course.getId());
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
        System.out.println("Create and insert new attends data to the database.");
        Attends attends = new Attends(course, student);
        AttendsDao attendsDao = new AttendsDao();
        attendsDao.persist(attends);

        System.out.println("Try to find the attends data by the course.");
        List<Attends> found = attendsDao.findByCourse(course);
        System.out.println("Found attends: " + found);

        assertNotNull(found);
        assertEquals(attends, found.get(0));
        assertEquals(course, found.get(0).getCourse());
        assertEquals(student, found.get(0).getStudent());

        Student student2 = new Student("Test2", "Student2", "anotherEmail@email.com");
        Student student3 = new Student("Test3", "Student3", "email@email.com");
        studentDao.persist(student2);
        studentDao.persist(student3);

        System.out.println("Create and insert more courses to the database.");
        Attends attends2 = new Attends(course, student2);
        Attends attends3 = new Attends(course, student3);
        attendsDao.persist(attends2);
        attendsDao.persist(attends3);

        System.out.println("Try again to find the attends data by the course.");
        List<Attends> found2 = attendsDao.findByCourse(course);
        System.out.println("Found attends data: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(attends, found2.get(0));
        assertEquals(course, found2.get(0).getCourse());
        assertEquals(student, found2.get(0).getStudent());

        assertEquals(attends2, found2.get(1));
        assertEquals(course, found2.get(1).getCourse());
        assertEquals(student2, found2.get(1).getStudent());

        assertEquals(attends3, found2.get(2));
        assertEquals(course, found2.get(2).getCourse());
        assertEquals(student3, found2.get(2).getStudent());
    }

    @Test
    @DisplayName("AttendsDAO + CourseDAO delete() deletes attends data test")
    void deleteAttendsWhenCourseIsDeleted() {
        System.out.println("Create and insert new attends data to the database.");
        Attends attends = new Attends(course, student);
        AttendsDao attendsDao = new AttendsDao();
        attendsDao.persist(attends);

        System.out.println("Delete course.");
        courseDao.delete(course);

        // Clear the EntityManager so it reloads from DB (Had to add this so the test passes)
        datasource.MariaDBJpaConnection.getInstance().clear();

        System.out.println("Attends ID: " + attends.getId());
        AttendsDao attendsDao2 = new AttendsDao();
        Attends found = attendsDao2.find(attends.getId());
        System.out.println("Found attends data: " + found);
        assertNull(found);
    }

    @Test
    @DisplayName("AttendsDAO + StudentDAO delete() deletes attends data test")
    void deleteAttendsWhenStudentIsDeleted() {
        System.out.println("Create and insert new attends data to the database.");
        Attends attends = new Attends(course, student);
        AttendsDao attendsDao = new AttendsDao();
        attendsDao.persist(attends);

        System.out.println("Delete student.");
        studentDao.delete(student);

        // Clear the EntityManager so it reloads from DB (Had to add this so the test passes)
        datasource.MariaDBJpaConnection.getInstance().clear();

        System.out.println("Attends ID: " + attends.getId());
        Attends found = attendsDao.find(attends.getId());
        System.out.println("Found attends data: " + found);
        assertNull(found);
    }

    @Test
    @DisplayName("AttendsDAO update() test")
    void update() {
        Course course1 = new Course("Attends New", "AN-2026-S1", teacher);
        courseDao.persist(course1);

        Student student1 = new Student("Student", "Second", "attendsEmail@email.com");
        studentDao.persist(student1);

        System.out.println("Create and insert new attends data to the database.");
        Attends attends = new Attends(course, student);
        AttendsDao attendsDao = new AttendsDao();
        attendsDao.persist(attends);

        attends.setCourse(course1);
        attends.setStudent(student1);

        attendsDao.update(attends);

        Attends found = attendsDao.find(attends.getId());
        System.out.println("Found attends data: " + found);

        assertNotNull(found);
        assertEquals(course1, found.getCourse());
        assertEquals(student1, found.getStudent());
    }

}