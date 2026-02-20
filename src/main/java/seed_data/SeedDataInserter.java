package seed_data;

import dao.*;
import entity.*;
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

        Teacher t1 = new Teacher("Freya", "Stephens", "freya.stephens@email.com", "salasana");
        Teacher t2 = new Teacher("Martin", "Ingram", "ingram.martin@email.com", "verySecret");
        Teacher t3 = new Teacher("Lila", "Donnelly", "donelly123@email.com", "password");

        teachDao.persist(t1);
        teachDao.persist(t2);
        teachDao.persist(t3);

        Course c1 = new Course("Web Development", "WD-2025-F2", t2);
        Course c2 = new Course("Graphic Design 101", "GD-2024-S4", t1);
        Course c3 = new Course("Japanese For Beginners", "JP-2026-S2", t3);
        Course c4 = new Course("Javascript Basics", "JB-2026-S1", t2);

        courseDao.persist(c1);
        courseDao.persist(c2);
        courseDao.persist(c3);
        courseDao.persist(c4);

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

        AttendanceCheck a1 = new AttendanceCheck(c2);
        AttendanceCheck a2 = new AttendanceCheck(c2);
        AttendanceCheck a3 = new AttendanceCheck(c2);
        AttendanceCheck a4 = new AttendanceCheck(c1);
        AttendanceCheck a5 = new AttendanceCheck(c1);
        AttendanceCheck a6 = new AttendanceCheck(c1);
        AttendanceCheck a7 = new AttendanceCheck(c3);
        AttendanceCheck a8 = new AttendanceCheck(c3);
        AttendanceCheck a9 = new AttendanceCheck(c4);
        AttendanceCheck a10 = new AttendanceCheck(c4);

        attCheckDao.persist(a1);
        attCheckDao.persist(a2);
        attCheckDao.persist(a3);
        attCheckDao.persist(a4);
        attCheckDao.persist(a5);
        attCheckDao.persist(a6);
        attCheckDao.persist(a7);
        attCheckDao.persist(a8);
        attCheckDao.persist(a9);
        attCheckDao.persist(a10);

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

        Attends at1 = new Attends(c1, s1);
        Attends at2 = new Attends(c1, s2);
        Attends at3 = new Attends(c1, s4);
        Attends at4 = new Attends(c1, s6);
        Attends at5 = new Attends(c1, s10);
        Attends at6 = new Attends(c1, s11);

        Attends at7 = new Attends(c2, s1);
        Attends at8 = new Attends(c2, s4);
        Attends at9 = new Attends(c2, s5);
        Attends at10 = new Attends(c2, s8);
        Attends at11 = new Attends(c2, s9);
        Attends at12 = new Attends(c2, s11);

        Attends at13 = new Attends(c3, s3);
        Attends at14 = new Attends(c3, s7);
        Attends at15 = new Attends(c3, s8);
        Attends at16 = new Attends(c3, s9);
        Attends at17 = new Attends(c3, s10);

        Attends at18 = new Attends(c4, s1);
        Attends at19 = new Attends(c4, s2);
        Attends at20 = new Attends(c4, s3);
        Attends at21 = new Attends(c4, s4);
        Attends at22 = new Attends(c4, s6);
        Attends at23 = new Attends(c4, s11);

        attendsDao.persist(at1);
        attendsDao.persist(at2);
        attendsDao.persist(at3);
        attendsDao.persist(at4);
        attendsDao.persist(at5);
        attendsDao.persist(at6);
        attendsDao.persist(at7);
        attendsDao.persist(at8);
        attendsDao.persist(at9);
        attendsDao.persist(at10);
        attendsDao.persist(at11);
        attendsDao.persist(at12);
        attendsDao.persist(at13);
        attendsDao.persist(at14);
        attendsDao.persist(at15);
        attendsDao.persist(at16);
        attendsDao.persist(at17);
        attendsDao.persist(at18);
        attendsDao.persist(at19);
        attendsDao.persist(at20);
        attendsDao.persist(at21);
        attendsDao.persist(at22);
        attendsDao.persist(at23);

        Checks ch1 = new Checks(s1,a1);
        Checks ch2 = new Checks(s2,a1);
        Checks ch3 = new Checks(s4,a1);
        Checks ch4 = new Checks(s6,a1);
        Checks ch5 = new Checks(s10,a1);
        Checks ch6 = new Checks(s11,a1);

        Checks ch7 = new Checks(s1,a2);
        Checks ch8 = new Checks(s2,a2);
        Checks ch9 = new Checks(s4,a2);
        Checks ch10 = new Checks(s6,a2);
        Checks ch11 = new Checks(s10,a2);
        Checks ch12 = new Checks(s11,a2);

        Checks ch13 = new Checks(s1,a3);
        Checks ch14 = new Checks(s2,a3);
        Checks ch15 = new Checks(s4,a3);
        Checks ch16 = new Checks(s6,a3);
        Checks ch17 = new Checks(s10,a3);
        Checks ch18 = new Checks(s11,a3);

        Checks ch19 = new Checks(s1,a4);
        Checks ch20 = new Checks(s4,a4);
        Checks ch21 = new Checks(s5,a4);
        Checks ch22 = new Checks(s8,a4);
        Checks ch23 = new Checks(s9,a4);
        Checks ch24 = new Checks(s11,a4);

        Checks ch25 = new Checks(s1,a5);
        Checks ch26 = new Checks(s4,a5);
        Checks ch27 = new Checks(s5,a5);
        Checks ch28 = new Checks(s8,a5);
        Checks ch29 = new Checks(s9,a5);
        Checks ch30 = new Checks(s11,a5);

        Checks ch31 = new Checks(s1,a6);
        Checks ch32 = new Checks(s4,a6);
        Checks ch33 = new Checks(s5,a6);
        Checks ch34 = new Checks(s8,a6);
        Checks ch35 = new Checks(s9,a6);
        Checks ch36 = new Checks(s11,a6);

        Checks ch37 = new Checks(s3,a7);
        Checks ch38 = new Checks(s7,a7);
        Checks ch39 = new Checks(s8,a7);
        Checks ch40 = new Checks(s9,a7);
        Checks ch41 = new Checks(s10,a7);
        ch37.setAttendanceStatus("PRESENT");
        ch38.setAttendanceStatus("PRESENT");
        ch40.setAttendanceStatus("PRESENT");
        ch41.setAttendanceStatus("PRESENT");

        Checks ch42 = new Checks(s3,a8);
        Checks ch43 = new Checks(s7,a8);
        Checks ch44 = new Checks(s8,a8);
        Checks ch45 = new Checks(s9,a8);
        Checks ch46 = new Checks(s10,a8);
        ch42.setAttendanceStatus("PRESENT");
        ch43.setAttendanceStatus("PRESENT");
        ch45.setAttendanceStatus("PRESENT");
        ch46.setAttendanceStatus("PRESENT");

        Checks ch47 = new Checks(s1,a9);
        Checks ch48 = new Checks(s2,a9);
        Checks ch49 = new Checks(s3,a9);
        Checks ch50 = new Checks(s4,a9);
        Checks ch51 = new Checks(s6,a9);
        Checks ch52 = new Checks(s11,a9);

        Checks ch53 = new Checks(s1,a10);
        Checks ch54 = new Checks(s2,a10);
        Checks ch55 = new Checks(s3,a10);
        Checks ch56 = new Checks(s4,a10);
        Checks ch57 = new Checks(s6,a10);
        Checks ch58 = new Checks(s11,a10);

        checksDao.persist(ch1);
        checksDao.persist(ch2);
        checksDao.persist(ch3);
        checksDao.persist(ch4);
        checksDao.persist(ch5);
        checksDao.persist(ch6);
        checksDao.persist(ch7);
        checksDao.persist(ch8);
        checksDao.persist(ch9);
        checksDao.persist(ch10);
        checksDao.persist(ch11);
        checksDao.persist(ch12);
        checksDao.persist(ch13);
        checksDao.persist(ch14);
        checksDao.persist(ch15);
        checksDao.persist(ch16);
        checksDao.persist(ch17);
        checksDao.persist(ch18);
        checksDao.persist(ch19);
        checksDao.persist(ch20);
        checksDao.persist(ch21);
        checksDao.persist(ch22);
        checksDao.persist(ch23);
        checksDao.persist(ch24);
        checksDao.persist(ch25);
        checksDao.persist(ch26);
        checksDao.persist(ch27);
        checksDao.persist(ch28);
        checksDao.persist(ch29);
        checksDao.persist(ch30);
        checksDao.persist(ch31);
        checksDao.persist(ch32);
        checksDao.persist(ch33);
        checksDao.persist(ch34);
        checksDao.persist(ch35);
        checksDao.persist(ch36);
        checksDao.persist(ch37);
        checksDao.persist(ch38);
        checksDao.persist(ch39);
        checksDao.persist(ch40);
        checksDao.persist(ch41);
        checksDao.persist(ch42);
        checksDao.persist(ch43);
        checksDao.persist(ch44);
        checksDao.persist(ch45);
        checksDao.persist(ch46);
        checksDao.persist(ch47);
        checksDao.persist(ch48);
        checksDao.persist(ch49);
        checksDao.persist(ch50);
        checksDao.persist(ch51);
        checksDao.persist(ch52);
        checksDao.persist(ch53);
        checksDao.persist(ch54);
        checksDao.persist(ch55);
        checksDao.persist(ch56);
        checksDao.persist(ch57);
        checksDao.persist(ch58);


    }
}
