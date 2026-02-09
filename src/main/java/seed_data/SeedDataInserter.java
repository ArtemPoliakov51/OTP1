package seed_data;

import dao.CourseDao;
import dao.TeacherDao;
import entity.Course;
import entity.Teacher;

public class SeedDataInserter {

    private SeedDataInserter() {
        throw new UnsupportedOperationException("Cannot create instance out of a utility class");
    }

    public static void main(String[] args) {

        TeacherDao teachDao = new TeacherDao();
        CourseDao courseDao = new CourseDao();

        Teacher t1 = new Teacher("Freya", "Stephens", "freya.stephens@email.com", "salasana");
        Teacher t2 = new Teacher("Martin", "Ingram", "ingram.martin@email.com", "verySecret");

        teachDao.persist(t1);
        teachDao.persist(t2);

        courseDao.persist(new Course("Web Development", "WD-2025-F2", t2));
        courseDao.persist(new Course("Graphic Design 101", "GD-2024-S4",  t1));
    }
}
