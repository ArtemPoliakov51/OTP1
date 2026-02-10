package dao;

import entity.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChecksDaoTest {

    private static AttendanceCheck attCheck;
    private static AttendanceCheckDao attCheckDao;
    private static Student student;
    private static StudentDao studentDao;
    private static Course course;
    private static Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("Test", "Teacher","test_" + System.nanoTime() + "@email.com", "superSecret111");
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.persist(teacher);

        course = new Course("Checks Test Course", "CHECKS-2026-S1", teacher);
        CourseDao courseDao = new CourseDao();
        courseDao.persist(course);

        attCheck = new AttendanceCheck();
        attCheckDao = new AttendanceCheckDao();
        attCheckDao.persist(attCheck);

        student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        studentDao = new StudentDao();
        studentDao.persist(student);
    }


    @Test
    @DisplayName("ChecksDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        System.out.println("Create and insert new checks data to the database.");
        Checks checks = new Checks(student, attCheck);
        ChecksDao checksDao = new ChecksDao();
        checksDao.persist(checks);

        System.out.println("Try to find the inserted data from database.");
        int checksId = checks.getId();
        Checks found = checksDao.find(checksId);
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(checks, found);
        assertEquals(attCheck, found.getAttendanceCheck());
        assertEquals(student, found.getStudent());

        System.out.println("Delete created checks data from the database.");
        checksDao.delete(checks);

        Checks found2 = checksDao.find(checksId);
        System.out.println("Find function returned: " + found2);

        assertNull(found2);

        System.out.println("Make sure that attendance check was not deleted from the database.");
        AttendanceCheck foundAttCheck = attCheckDao.find(attCheck.getId());
        System.out.println("Find function returned: " + foundAttCheck);
        assertNotNull(foundAttCheck);

        System.out.println("Make sure that student was not deleted from the database.");
        Student foundStudent = studentDao.find(student.getId());
        System.out.println("Find function returned: " + foundStudent);
        assertNotNull(foundStudent);
    }

    @Test
    @DisplayName("AttendsDAO findByCourse() test")
    void findByAttendanceCheck() {
        System.out.println("Create and insert new checks data to the database.");
        Checks checks = new Checks(student, attCheck);
        ChecksDao checksDao = new ChecksDao();
        checksDao.persist(checks);


        System.out.println("Try to find the checks data by the course.");
        List<Checks> found = checksDao.findByAttendanceCheck(attCheck);
        System.out.println("Found checks data: " + found);

        assertNotNull(found);
        assertEquals(checks, found.get(0));
        assertEquals(attCheck, found.get(0).getAttendanceCheck());
        assertEquals(student, found.get(0).getStudent());

        Student student2 = new Student("Test2", "Student2", "anotherEmail@email.com");
        Student student3 = new Student("Test3", "Student3", "email@email.com");
        studentDao.persist(student2);
        studentDao.persist(student3);

        System.out.println("Create and insert more courses to the database.");
        Checks checks2 = new Checks(student2, attCheck);
        Checks checks3 = new Checks(student3, attCheck);
        checksDao.persist(checks2);
        checksDao.persist(checks3);

        System.out.println("Try again to find the checks data by the course.");
        List<Checks> found2 = checksDao.findByAttendanceCheck(attCheck);
        System.out.println("Found checks data: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(checks, found2.get(0));
        assertEquals(attCheck, found2.get(0).getAttendanceCheck());
        assertEquals(student, found2.get(0).getStudent());

        assertEquals(checks2, found2.get(1));
        assertEquals(attCheck, found2.get(1).getAttendanceCheck());
        assertEquals(student2, found2.get(1).getStudent());

        assertEquals(checks3, found2.get(2));
        assertEquals(attCheck, found2.get(2).getAttendanceCheck());
        assertEquals(student3, found2.get(2).getStudent());
    }

    @Test
    @DisplayName("ChecksDAO + AttendanceDAO delete() deletes checks data test")
    void deleteChecksWhenCourseIsDeleted() {
        System.out.println("Create and insert new checks data to the database.");
        Checks checks = new Checks(student, attCheck);
        ChecksDao checksDao = new ChecksDao();
        checksDao.persist(checks);

        System.out.println("Delete attendance check.");
        attCheckDao.delete(attCheck);

        // Clear the EntityManager so it reloads from DB (Had to add this so the test passes)
        datasource.MariaDBJpaConnection.getInstance().clear();

        Checks found = checksDao.find(checks.getId());
        System.out.println("Found checks data: " + found);
        assertNull(found);
    }

    @Test
    @DisplayName("ChecksDAO + StudentDAO delete() deletes checks data test")
    void deleteChecksWhenStudentIsDeleted() {
        System.out.println("Create and insert new checks data to the database.");
        Checks checks = new Checks(student, attCheck);
        ChecksDao checksDao = new ChecksDao();
        checksDao.persist(checks);

        System.out.println("Delete student.");
        studentDao.delete(student);

        // Clear the EntityManager so it reloads from DB (Had to add this so the test passes)
        datasource.MariaDBJpaConnection.getInstance().clear();

        Checks found = checksDao.find(checks.getId());
        System.out.println("Found checks data: " + found);
        assertNull(found);
    }

    @Test
    @DisplayName("ChecksDAO update() test")
    void update() {
        AttendanceCheck attendanceCheck = new AttendanceCheck(course);
        attCheckDao.persist(attendanceCheck);

        Student student1 = new Student("Checks", "Student", "checksEmail@email.com");
        studentDao.persist(student1);

        System.out.println("Create and insert new checks data to the database.");
        Checks checks = new Checks(student, attCheck);
        ChecksDao checksDao = new ChecksDao();
        checksDao.persist(checks);

        checks.setAttendanceCheck(attendanceCheck);
        checks.setStudent(student1);

        checksDao.update(checks);

        Checks found = checksDao.find(checks.getId());
        System.out.println("Found checks data: " + found);

        assertNotNull(found);
        assertEquals(attendanceCheck, found.getAttendanceCheck());
        assertEquals(student1, found.getStudent());
    }

}