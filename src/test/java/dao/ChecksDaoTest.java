package dao;

import entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChecksDaoTest {

    private static AttendanceCheck attCheck;
    private static AttendanceCheckDao attCheckDao;
    private static Student student;
    private static StudentDao studentDao;
    private static int courseId;
    private static Teacher teacher;

    @BeforeEach
    void setUp() {
        datasource.MariaDBJpaConnection.getTestEntityManager();

        teacher = new Teacher("Test", "Teacher","test_" + System.nanoTime() + "@email.com", "superSecret111");
        TeacherDao teacherDao = new TeacherDao();
        teacherDao.persist(teacher);

        CourseDao courseDao = new CourseDao();
        courseId = courseDao.persist("Checks Test Course", "CHECKS-2026-S1", teacher.getId());

        attCheckDao = new AttendanceCheckDao();
        int attCheckId = attCheckDao.persist(courseId);
        attCheck = attCheckDao.find(attCheckId);

        student = new Student("Attends", "Student", "student_" + System.nanoTime() + "@email.com");
        studentDao = new StudentDao();
        studentDao.persist(student);
    }

    @AfterEach
    void cleanUp() {
        datasource.MariaDBJpaConnection.reset();
    }


    @Test
    @DisplayName("ChecksDAO persist(), find() and delete() test")
    void persistAndFindAndDelete() {
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        em.getTransaction().begin();
        em.flush();
        em.getTransaction().commit();

        System.out.println("Create and insert new checks data to the database.");
        ChecksDao checksDao = new ChecksDao();
        ChecksId checksId = checksDao.persist(attCheck.getId(), student.getId());

        System.out.println("Try to find the inserted data from database.");
        Checks found = checksDao.find(checksId.getAttendanceCheckId(), checksId.getStudentId());
        System.out.println("Find function returned: " + found);

        assertNotNull(found);
        assertEquals(attCheck.getId(), found.getAttendanceCheck().getId());
        assertEquals(student.getId(), found.getStudent().getId());

        System.out.println("Delete created checks data from the database.");
        checksDao.delete(checksId.getAttendanceCheckId(), checksId.getStudentId());

        // Clear the EntityManager so it reloads from DB (Had to add this so the test passes)
        datasource.MariaDBJpaConnection.getEntityManager().clear();

        Checks found2 = checksDao.find(checksId.getAttendanceCheckId(), checksId.getStudentId());
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
    @DisplayName("ChecksDAO findByCourse() test")
    void findByAttendanceCheck() {
        System.out.println("Create and insert new checks data to the database.");
        ChecksDao checksDao = new ChecksDao();
        ChecksId checksId = checksDao.persist(attCheck.getId(), student.getId());

        System.out.println("Try to find the checks data by the course.");
        List<Checks> found = checksDao.findByAttendanceCheck(attCheck.getId());
        System.out.println("Found checks data: " + found);

        assertNotNull(found);
        assertEquals(checksId.getAttendanceCheckId(), found.get(0).getAttendanceCheck().getId());
        assertEquals(checksId.getStudentId(), found.get(0).getStudent().getId());
        assertEquals(attCheck.getId(), found.get(0).getAttendanceCheck().getId());
        assertEquals(student.getId(), found.get(0).getStudent().getId());

        Student student2 = new Student("Test2", "Student2", "anotherEmail@email.com");
        Student student3 = new Student("Test3", "Student3", "email@email.com");
        studentDao.persist(student2);
        studentDao.persist(student3);

        System.out.println("Create and insert more checks to the database.");
        ChecksId checks2Id = checksDao.persist(attCheck.getId(), student2.getId());
        ChecksId checks3Id = checksDao.persist(attCheck.getId(), student3.getId());

        System.out.println("Try again to find the checks data by the course.");
        List<Checks> found2 = checksDao.findByAttendanceCheck(attCheck.getId());
        System.out.println("Found checks data: " + found2);

        assertNotNull(found2);
        assertEquals(3, found2.size());
        assertEquals(checksId.getAttendanceCheckId(), found2.get(0).getAttendanceCheck().getId());
        assertEquals(checksId.getStudentId(), found2.get(0).getStudent().getId());
        assertEquals(checks2Id.getAttendanceCheckId(), found2.get(1).getAttendanceCheck().getId());
        assertEquals(checks2Id.getStudentId(), found2.get(1).getStudent().getId());
        assertEquals(checks3Id.getAttendanceCheckId(), found2.get(2).getAttendanceCheck().getId());
        assertEquals(checks3Id.getStudentId(), found2.get(2).getStudent().getId());
    }

    @Test
    @DisplayName("ChecksDAO findByStudent() test")
    void findByStudent() {
        System.out.println("Create and insert new checks data to the database.");
        ChecksDao checksDao = new ChecksDao();
        ChecksId checksId = checksDao.persist(attCheck.getId(), student.getId());


        System.out.println("Try to find the checks data by the student.");
        List<Checks> found = checksDao.findByStudent(student.getId());
        System.out.println("Found checks data: " + found);

        assertNotNull(found);
        assertEquals(checksId.getAttendanceCheckId(), found.get(0).getAttendanceCheck().getId());
        assertEquals(checksId.getStudentId(), found.get(0).getStudent().getId());
        assertEquals(attCheck.getId(), found.get(0).getAttendanceCheck().getId());
        assertEquals(student.getId(), found.get(0).getStudent().getId());
    }

    @Test
    @DisplayName("ChecksDAO update() test")
    void updateStatusAndNotes() {
        int attendanceCheckId = attCheckDao.persist(courseId);

        Student student1 = new Student("Checks", "Student", "checksEmail@email.com");
        studentDao.persist(student1);

        System.out.println("Create and insert new checks data to the database.");
        ChecksDao checksDao = new ChecksDao();
        ChecksId checksId = checksDao.persist(attCheck.getId(), student.getId());
        Checks checks = checksDao.find(checksId.getAttendanceCheckId(), checksId.getStudentId());

        checks.setAttendanceStatus("TEST");
        checks.setNotes("This is a test");

        checksDao.update(checks);

        Checks found = checksDao.find(checks.getId().getAttendanceCheckId(), checks.getId().getStudentId());
        System.out.println("Found checks data: " + found);

        assertNotNull(found);
        assertEquals("TEST", found.getAttendanceStatus());
        assertEquals("This is a test", found.getNotes());
    }

}