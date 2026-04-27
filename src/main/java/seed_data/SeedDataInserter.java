package seed_data;

import dao.*;
import entity.*;
import jakarta.persistence.EntityManager;
import utils.PasswordHasher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Provides utility methods for checking the status of the seed data and inserting seed data.
 *
 * <p>This class checks whether the database contains any data.
 * If it does not contain anything, seed data is inserted.</p>
 *
 * <p>The class is designed as a static utility and cannot be instantiated.</p>
 */
public class SeedDataInserter {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SeedDataInserter() {
        throw new UnsupportedOperationException("Cannot create instance out of a utility class");
    }

    /**
     * Checks whether Teacher table in database contains any data. If it is empty, runs the insertData method.
     */
    public static void runIfNeeded() {

        // Check if there is any data in the database
        EntityManager em = datasource.MariaDBJpaConnection.getEntityManager();
        Long count = em.createQuery(
                "SELECT COUNT(*) FROM Teacher t", Long.class
        ).getSingleResult();

        // If there is no data, insert the seed data
        if (count == 0) {
            insertData();
        }
    }

    /**
     * Creates database tables if they do not exist and inserts seed data to all of them.
     */
    private static void insertData() {
        try {
            TeacherDao teachDao = new TeacherDao();
            CourseDao courseDao = new CourseDao();
            StudentDao studentDao = new StudentDao();
            AttendanceCheckDao attCheckDao = new AttendanceCheckDao();
            AttendsDao attendsDao = new AttendsDao();
            ChecksDao checksDao = new ChecksDao();

            Teacher t1 = new Teacher("Freya", "Freya", "フレイヤ", "Φρέγια",
                    "Stephens", "Stephens", "スティーブンス", "Στέφενς",
                    "freya.stephens@email.com", PasswordHasher.hashPassword("salasana"));
            Teacher t2 = new Teacher("Martin", "Martin", "マーティン", "Μάρτιν",
                    "Ingram", "Ingram", "イングラム", "Ίνγκραμ",
                    "ingram.martin@email.com", PasswordHasher.hashPassword("verySecret"));
            Teacher t3 = new Teacher("Lila", "Lila", "リラ", "Λίλα",
                    "Donnelly", "Donnelly", "ドネリー", "Ντόνελι",
                    "donelly123@email.com", PasswordHasher.hashPassword("password"));

            teachDao.persist(t1);
            teachDao.persist(t2);
            teachDao.persist(t3);

            int c1id = courseDao.persist("Web Development", "Web-kehitys", "ウェブ開発", "Ανάπτυξη Ιστοσελίδων",
                    "WD-2025-F2", t2.getId());
            Course c1 = courseDao.find(c1id);

            int c2id = courseDao.persist("Graphic Design 101", "Graafisen suunnittelun perusteet", "グラフィックデザイン入門", "Γραφιστική Σχεδίαση 101",
                    "GD-2024-S4", t1.getId());
            Course c2 = courseDao.find(c2id);

            int c3id = courseDao.persist("Japanese For Beginners", "Japanin alkeet", "初級日本語", "Ιαπωνικά για Αρχάριους",
                    "JP-2026-S2", t3.getId());
            Course c3 = courseDao.find(c3id);

            int c4id = courseDao.persist("Javascript Basics", "JavasScriptin perusteet", "JavaScript基礎", "Βασικά JavaScript",
                    "JB-2026-S1", t2.getId());
            Course c4 = courseDao.find(c4id);

            c2.setArchived(LocalDateTime.of(2024, 5, 11, 23, 35, 18));
            c2.setStatus("ARCHIVED");
            courseDao.update(c2);

            Student s1 = new Student("Finn", "Finn", "フィン", "Φιν",
                    "Davis", "Davis", "デイビス", "Ντέιβις","finn.davis@email.com");
            Student s2 = new Student("Evan", "Evan", "エヴァン", "Έβαν",
                    "Andrews", "Andrews", "アンドリュース", "Άντριους","eandrews100@email.com");
            Student s3 = new Student("Taylor", "Taylor", "テイラー", "Τέιλορ",
                    "Poole", "Poole", "プール", "Πουλ","poole.taylor@email.com");
            Student s4 = new Student("Charlotte", "Charlotte", "シャーロット", "Σάρλοτ",
                    "Holland", "Holland", "ホランド", "Χόλαντ","holland333@email.com");
            Student s5 = new Student("Ellen", "Ellen", "エレン", "Έλεν",
                    "Gill", "Gill", "ギル", "Γκιλ","ellen21gill@email.com");
            Student s6 = new Student("Harley", "Harley", "ハーリー", "Χάρλεϊ",
                    "Saunders", "Saunders", "ソーンダース", "Σόντερς","saunders.harley@email.com");
            Student s7 = new Student("Josh", "Josh", "ジョシュ", "Τζος",
                    "Dawson", "Dawson", "ドーソン", "Ντόσον","josh111@email.com");
            Student s8 = new Student("Alex", "Alex", "アレックス", "Άλεξ",
                    "Campbell", "Campbell", "キャンベル", "Κάμπελ", "alex.campbell9@email.com");
            Student s9 = new Student("Tyler", "Tyler", "タイラー", "Τάιλερ",
                    "Walker", "Walker", "ウォーカー", "Γουόκερ", "walker999tyler@email.com");
            Student s10 = new Student("Helena", "Helena", "ヘレナ", "Έλενα",
                    "Lawson", "Lawson", "ローソン", "Λόσον","helena.lawson@email.com");
            Student s11 = new Student("Erika", "Erika", "エリカ", "Έρικα", "Knight", "Knight", "ナイト", "Νάιτ","knight.er321@email.com");

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

            a1.setCheckDate(LocalDate.of(2024, 3, 10));
            a1.setCheckTime(LocalTime.of(9, 5, 35));
            attCheckDao.update(a1);

            a2.setCheckDate(LocalDate.of(2024, 3, 14));
            a2.setCheckTime(LocalTime.of(9, 10, 12));
            attCheckDao.update(a2);

            a3.setCheckDate(LocalDate.of(2024, 3, 19));
            a3.setCheckTime(LocalTime.of(9, 7, 56));
            attCheckDao.update(a3);

            a4.setCheckDate(LocalDate.of(2025, 10, 16));
            a4.setCheckTime(LocalTime.of(13, 4, 45));
            attCheckDao.update(a4);

            a5.setCheckDate(LocalDate.of(2025, 10, 22));
            a5.setCheckTime(LocalTime.of(13, 9, 11));
            attCheckDao.update(a5);

            a6.setCheckDate(LocalDate.of(2025, 10, 28));
            a6.setCheckTime(LocalTime.of(13, 12, 29));
            attCheckDao.update(a6);

            a7.setCheckDate(LocalDate.of(2026, 1, 20));
            a7.setCheckTime(LocalTime.of(10, 8, 49));
            attCheckDao.update(a7);

            a8.setCheckDate(LocalDate.of(2026, 1, 27));
            a8.setCheckTime(LocalTime.of(10, 10, 5));
            attCheckDao.update(a8);

            a9.setCheckDate(LocalDate.of(2026, 1, 18));
            a9.setCheckTime(LocalTime.of(14, 10, 43));
            attCheckDao.update(a9);

            a10.setCheckDate(LocalDate.of(2026, 1, 27));
            a10.setCheckTime(LocalTime.of(14, 6, 15));
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
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error while inserting test data: " + e.getMessage());
        }
    }
}
