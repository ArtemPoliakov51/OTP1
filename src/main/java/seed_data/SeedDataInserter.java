package seed_data;

import dao.*;
import entity.*;
import utils.PasswordHasher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SeedDataInserter {

    private SeedDataInserter() {
        throw new UnsupportedOperationException("Cannot create instance out of a utility class");
    }

    public static void main(String[] args) {

        TeacherDao teachDao = new TeacherDao();
        CourseDao courseDao = new CourseDao();
        StudentDao studentDao = new StudentDao();
        AttendanceCheckDao attCheckDao = new AttendanceCheckDao();
        AttendsDao attendsDao = new AttendsDao();
        ChecksDao checksDao = new ChecksDao();

        Teacher t1 = new Teacher("Freya", "Stephens", "freya.stephens@email.com", PasswordHasher.hashPassword("salasana"));
        Teacher t2 = new Teacher("Martin", "Ingram", "ingram.martin@email.com", PasswordHasher.hashPassword("verySecret"));
        Teacher t3 = new Teacher("Lila", "Donnelly", "donelly123@email.com", PasswordHasher.hashPassword("password"));

        teachDao.persist(t1);
        teachDao.persist(t2);
        teachDao.persist(t3);

        int c1id = courseDao.persist("Web Development", "WD-2025-F2", t2.getId());
        Course c1 = courseDao.find(c1id);

        int c2id = courseDao.persist("Graphic Design 101", "GD-2024-S4", t1.getId());
        Course c2 = courseDao.find(c2id);

        int c3id = courseDao.persist("Japanese For Beginners", "JP-2026-S2", t3.getId());
        Course c3 = courseDao.find(c3id);

        int c4id = courseDao.persist("Javascript Basics", "JB-2026-S1", t2.getId());
        Course c4 = courseDao.find(c4id);

        c2.setArchived(LocalDateTime.of(2024,5,11, 23,35,18));
        c2.setStatus("ARCHIVED");
        courseDao.update(c2);

        Student s1 = new Student("Finn", "Davis", "finn.davis@email.com");
        Student s2 = new Student("Evan", "Andrews", "eandrews100@email.com");
        Student s3 = new Student("Taylor", "Poole", "poole.taylor@email.com");
        Student s4 = new Student("Charlotte", "Holland", "holland333@email.com");
        Student s5 = new Student("Ellen", "Gill", "ellen21gill@email.com");
        Student s6 = new Student("Harley", "Saunders", "saunders.harley@email.com");
        Student s7 = new Student("Josh", "Dawson", "josh111@email.com");
        Student s8 = new Student("Alex", "Campbell", "alex.campbell9@email.com");
        Student s9 = new Student("Tyler", "Walker", "walker999tyler@email.com");
        Student s10 = new Student("Helena", "Lawson", "helena.lawson@email.com");
        Student s11 = new Student("Erika", "Knight", "knight.er321@email.com");

        studentDao.persist(s1);
        studentDao.persist(s2);
        studentDao.persist(s3);
        studentDao.persist(s4);
        studentDao.persist(s5);
        studentDao.persist(s6);
        studentDao.persist(s7);
        studentDao.persist(s8);
        studentDao.persist(s9);
        studentDao.persist(s10);
        studentDao.persist(s11);

        int a1Id = attCheckDao.persist(c2.getId());
        int a2Id = attCheckDao.persist(c2.getId());
        int a3Id = attCheckDao.persist(c2.getId());
        int a4Id = attCheckDao.persist(c1.getId());
        int a5Id = attCheckDao.persist(c1.getId());
        int a6Id = attCheckDao.persist(c1.getId());
        int a7Id = attCheckDao.persist(c3.getId());
        int a8Id = attCheckDao.persist(c3.getId());
        int a9Id = attCheckDao.persist(c4.getId());
        int a10Id = attCheckDao.persist(c4.getId());

        AttendanceCheck a1 = attCheckDao.find(a1Id);
        AttendanceCheck a2 = attCheckDao.find(a2Id);
        AttendanceCheck a3 = attCheckDao.find(a3Id);
        AttendanceCheck a4 = attCheckDao.find(a4Id);
        AttendanceCheck a5 = attCheckDao.find(a5Id);
        AttendanceCheck a6 = attCheckDao.find(a6Id);
        AttendanceCheck a7 = attCheckDao.find(a7Id);
        AttendanceCheck a8 = attCheckDao.find(a8Id);
        AttendanceCheck a9 = attCheckDao.find(a9Id);
        AttendanceCheck a10 = attCheckDao.find(a10Id);

        a1.setCheckDate(LocalDate.of(2024, 3,10));
        a1.setCheckTime(LocalTime.of(9,5,35));
        attCheckDao.update(a1);

        a2.setCheckDate(LocalDate.of(2024, 3,14));
        a2.setCheckTime(LocalTime.of(9,10,12));
        attCheckDao.update(a2);

        a3.setCheckDate(LocalDate.of(2024,3,19));
        a3.setCheckTime(LocalTime.of(9,7,56));
        attCheckDao.update(a3);

        a4.setCheckDate(LocalDate.of(2025,10,16));
        a4.setCheckTime(LocalTime.of(13,4,45));
        attCheckDao.update(a4);

        a5.setCheckDate(LocalDate.of(2025,10,22));
        a5.setCheckTime(LocalTime.of(13,9,11));
        attCheckDao.update(a5);

        a6.setCheckDate(LocalDate.of(2025,10,28));
        a6.setCheckTime(LocalTime.of(13,12,29));
        attCheckDao.update(a6);

        a7.setCheckDate(LocalDate.of(2026,1,20));
        a7.setCheckTime(LocalTime.of(10,8,49));
        attCheckDao.update(a7);

        a8.setCheckDate(LocalDate.of(2026,1,27));
        a8.setCheckTime(LocalTime.of(10,10,5));
        attCheckDao.update(a8);

        a9.setCheckDate(LocalDate.of(2026,1,18));
        a9.setCheckTime(LocalTime.of(14,10,43));
        attCheckDao.update(a9);

        a10.setCheckDate(LocalDate.of(2026,1,27));
        a10.setCheckTime(LocalTime.of(14,6,15));
        attCheckDao.update(a10);

        attendsDao.persist(c1.getId(), s1.getId());
        attendsDao.persist(c1.getId(), s2.getId());
        attendsDao.persist(c1.getId(), s4.getId());
        attendsDao.persist(c1.getId(), s6.getId());
        attendsDao.persist(c1.getId(), s10.getId());

        attendsDao.persist(c2.getId(), s1.getId());
        attendsDao.persist(c2.getId(), s4.getId());
        attendsDao.persist(c2.getId(), s5.getId());
        attendsDao.persist(c2.getId(), s8.getId());
        attendsDao.persist(c2.getId(), s9.getId());
        attendsDao.persist(c2.getId(), s11.getId());

        attendsDao.persist(c3.getId(), s3.getId());
        attendsDao.persist(c3.getId(), s7.getId());
        attendsDao.persist(c3.getId(), s8.getId());
        attendsDao.persist(c3.getId(), s9.getId());
        attendsDao.persist(c3.getId(), s10.getId());

        attendsDao.persist(c4.getId(), s1.getId());
        attendsDao.persist(c4.getId(), s2.getId());
        attendsDao.persist(c4.getId(), s3.getId());
        attendsDao.persist(c4.getId(), s4.getId());
        attendsDao.persist(c4.getId(), s6.getId());
        attendsDao.persist(c4.getId(), s11.getId());

        checksDao.persist(a1.getId(), s1.getId());
        checksDao.persist(a1.getId(), s2.getId());
        checksDao.persist(a1.getId(), s4.getId());
        checksDao.persist(a1.getId(), s6.getId());
        checksDao.persist(a1.getId(), s10.getId());
        checksDao.persist(a1.getId(), s11.getId());

        checksDao.persist(a2.getId(), s1.getId());
        checksDao.persist(a2.getId(), s2.getId());
        checksDao.persist(a2.getId(), s4.getId());
        checksDao.persist(a2.getId(), s6.getId());
        checksDao.persist(a2.getId(), s10.getId());
        checksDao.persist(a2.getId(), s11.getId());

        checksDao.persist(a3.getId(), s1.getId());
        checksDao.persist(a3.getId(), s2.getId());
        checksDao.persist(a3.getId(), s4.getId());
        checksDao.persist(a3.getId(), s6.getId());
        checksDao.persist(a3.getId(), s10.getId());
        checksDao.persist(a3.getId(), s11.getId());

        checksDao.persist(a4.getId(), s1.getId());
        checksDao.persist(a4.getId(), s4.getId());
        checksDao.persist(a4.getId(), s5.getId());
        checksDao.persist(a4.getId(), s8.getId());
        checksDao.persist(a4.getId(), s9.getId());
        checksDao.persist(a4.getId(), s11.getId());

        checksDao.persist(a5.getId(), s1.getId());
        checksDao.persist(a5.getId(), s4.getId());
        checksDao.persist(a5.getId(), s5.getId());
        checksDao.persist(a5.getId(), s8.getId());
        checksDao.persist(a5.getId(), s9.getId());
        checksDao.persist(a5.getId(), s11.getId());

        checksDao.persist(a6.getId(), s1.getId());
        checksDao.persist(a6.getId(), s4.getId());
        checksDao.persist(a6.getId(), s5.getId());
        checksDao.persist(a6.getId(), s8.getId());
        checksDao.persist(a6.getId(), s9.getId());
        checksDao.persist(a6.getId(), s11.getId());

        checksDao.persist(a7.getId(), s3.getId());
        checksDao.persist(a7.getId(), s7.getId());
        checksDao.persist(a7.getId(), s8.getId());
        checksDao.persist(a7.getId(), s9.getId());
        checksDao.persist(a7.getId(), s10.getId());

        checksDao.persist(a8.getId(), s3.getId());
        checksDao.persist(a8.getId(), s7.getId());
        checksDao.persist(a8.getId(), s8.getId());
        checksDao.persist(a8.getId(), s9.getId());
        checksDao.persist(a8.getId(), s10.getId());

        checksDao.persist(a9.getId(), s1.getId());
        checksDao.persist(a9.getId(), s2.getId());
        checksDao.persist(a9.getId(), s3.getId());
        checksDao.persist(a9.getId(), s4.getId());
        checksDao.persist(a9.getId(), s6.getId());
        checksDao.persist(a9.getId(), s11.getId());

        checksDao.persist(a10.getId(), s1.getId());
        checksDao.persist(a10.getId(), s2.getId());
        checksDao.persist(a10.getId(), s3.getId());
        checksDao.persist(a10.getId(), s4.getId());
        checksDao.persist(a10.getId(), s6.getId());
        checksDao.persist(a10.getId(), s11.getId());
    }
}
