CREATE DATABASE IF NOT EXISTS `attendance_database` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci */;
USE `attendance_database`;

-- Dumping structure for table attendance_database.teachers
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
    `teacher_id` int(11) NOT NULL AUTO_INCREMENT,
    `firstname` varchar(50) NOT NULL,
    `lastname` varchar(50) NOT NULL,
    `email` varchar(100) NOT NULL,
    `password` varchar(255) NOT NULL,
    PRIMARY KEY (`teacher_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.students
DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
    `student_id` int(11) NOT NULL AUTO_INCREMENT,
    `firstname` varchar(50) NOT NULL,
    `lastname` varchar(50) NOT NULL,
    `email` varchar(100) NOT NULL,
    PRIMARY KEY (`student_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.courses
DROP TABLE IF EXISTS `course`;
CREATE TABLE IF NOT EXISTS `course` (
    `course_id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    `identifier` varchar(50) UNIQUE NOT NULL,
    `status` varchar(20) NOT NULL,
    `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `archived` datetime DEFAULT NULL,
    `teacher_id` int(11) DEFAULT NULL,
    PRIMARY KEY (`course_id`),
    KEY `FK_courses_teachers` (`teacher_id`),
    CONSTRAINT `FK_courses_teachers` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`teacher_id`) ON DELETE SET NULL ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.attendance_checks
DROP TABLE IF EXISTS `attendance_check`;
CREATE TABLE IF NOT EXISTS `attendance_check` (
    `check_id` int(11) NOT NULL AUTO_INCREMENT,
    `check_date` date NOT NULL,
    `check_time` time NOT NULL,
    `course_id` int(11) NOT NULL,
    PRIMARY KEY (`check_id`),
    KEY `FK_attendance_checks_courses` (`course_id`),
    CONSTRAINT `FK_attendance_checks_courses` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE NO ACTION ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.attends
DROP TABLE IF EXISTS `attends`;
CREATE TABLE IF NOT EXISTS `attends` (
    `course_id` int(11) NOT NULL,
    `student_id` int(11) NOT NULL,
    PRIMARY KEY (`course_id`, `student_id`),
    KEY `FK_attends_students` (`student_id`),
    CONSTRAINT `FK_attends_courses` FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_attends_students` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.checks
DROP TABLE IF EXISTS `checks`;
CREATE TABLE IF NOT EXISTS `checks` (
    `student_id` int(11) NOT NULL,
    `attendance_check_id` int(11) NOT NULL,
    `attendance_status` varchar(20) NOT NULL,
    `notes` varchar(800) DEFAULT '',
    PRIMARY KEY (`student_id`, `attendance_check_id`),
    KEY `FK_checks_attendance_checks` (`attendance_check_id`),
    CONSTRAINT `FK_checks_students` FOREIGN KEY (`student_id`) REFERENCES `student` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_checks_attendance_checks` FOREIGN KEY (`attendance_check_id`) REFERENCES `attendance_check` (`check_id`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

INSERT INTO teacher (firstname, lastname, email, password)
VALUES ("Freya", "Stephens", "freya.stephens@email.com", "salasana"),
       ("Martin", "Ingram", "ingram.martin@email.com", "verySecret"),
       ("Lila", "Donnelly", "donelly123@email.com", "password"),
       ("Erik", "Osborne", "erik.osborne@email.com", "change000This"),
       ("Tiffany", "Banks", "tbanks13@email.com", "myPassword");

INSERT INTO course (name, identifier, created, archived, status, teacher_id)
VALUES ("Web Development", "WD-2025-F2", "2025-10-13 18:24:56", NULL, "ACTIVE", 2),
       ("Graphic Design 101", "GD-2024-S4", "2024-03-02 19:15:02", "2024-05-11 23:35:18", "ARCHIVED", 1),
       ("Japanese For Beginners", "JP-2026-S2", "2026-01-11 12:51:32", NULL, "ACTIVE", 3),
       ("Javascript Basics", "JB-2026-S1", "2026-01-11 09:22:47", NULL, "ACTIVE", 2);

INSERT INTO student (firstname, lastname, email)
VALUES ("Finn", "Davis", "finn.davis@email.com"),
       ("Evan", "Andrews", "eandrews100@email.com"),
       ("Taylor", "Poole", "poole.taylor@email.com"),
       ("Charlotte", "Holland", "holland333@email.com"),
       ("Ellen", "Gill", "ellen21gill@email.com"),
       ("Harley", "Saunders", "saunders.harley@email.com"),
       ("Josh", "Dawson", "josh111@email.com"),
       ("Alex", "Campbell", "alex.campbell9@email.com"),
       ("Tyler", "Walker", "walker999tyler@email.com"),
       ("Helena", "Lawson", "helena.lawson@email.com"),
       ("Erika", "Knight", "knight.er321@email.com");

INSERT INTO attendance_check (`check_date`, `check_time`, course_id)
VALUES ("2024-03-10", "09:05:35", 2),
       ("2024-03-14", "09:10:12", 2),
       ("2024-03-19", "09:07:56", 2),
       ("2025-10-16", "13:04:45", 1),
       ("2025-10-22", "13:09:11", 1),
       ("2025-10-28", "13:12:29", 1),
       ("2026-01-20", "10:08:49", 3),
       ("2026-01-27", "10:10:05", 3),
       ("2026-01-18", "14:10:43", 4),
       ("2026-01-27", "14:06:15", 4);

INSERT INTO attends (course_id, student_id)
VALUES (1, 1), (1, 2), (1, 4), (1, 6), (1, 10), (1, 11),
       (2, 1), (2, 4), (2, 5), (2, 8), (2, 9), (2, 11),
       (3, 3), (3, 7), (3, 8), (3, 9), (3, 10),
       (4, 1), (4, 2), (4, 3), (4, 4), (4, 6), (4, 11);

INSERT INTO checks (attendance_check_id, student_id, attendance_status)
VALUES
    (1, 1, "PRESENT"), (1, 4, "PRESENT"), (1, 5, "ABSENT"), (1, 8, "PRESENT"), (1, 9, "ABSENT"), (1, 11, "PRESENT"),
    (2, 1, "PRESENT"), (2, 4, "PRESENT"), (2, 5, "PRESENT"), (2, 8, "PRESENT"), (2, 9, "ABSENT"), (2, 11, "PRESENT"),
    (3, 1, "PRESENT"), (3, 4, "EXCUSED"), (3, 5, "PRESENT"), (3, 8, "ABSENT"), (3, 9, "PRESENT"), (3, 11, "PRESENT"),
    (4, 1, "ABSENT"), (4, 2, "PRESENT"), (4, 4, "PRESENT"), (4, 6, "ABSENT"), (4, 10, "PRESENT"), (4, 11, "PRESENT"),
    (5, 1, "PRESENT"), (5, 2, "PRESENT"), (5, 4, "PRESENT"), (5, 6, "ABSENT"), (5, 10, "PRESENT"), (5, 11, "PRESENT"),
    (6, 1, "PRESENT"), (6, 2, "PRESENT"), (6, 4, "PRESENT"), (6, 6, "ABSENT"), (6, 10, "PRESENT"), (6, 11, "EXCUSED"),
    (7, 3, "PRESENT"), (7, 7, "PRESENT"), (7, 8, "ABSENT"), (7, 9, "PRESENT"), (7, 10, "PRESENT"),
    (8, 3, "PRESENT"), (8, 7, "PRESENT"), (8, 8, "ABSENT"), (8, 9, "PRESENT"), (8, 10, "PRESENT"),
    (9, 1, "EXCUSED"), (9, 2, "PRESENT"), (9, 3, "PRESENT"), (9, 4, "EXCUSED"), (9, 6, "PRESENT"), (9, 11, "PRESENT"),
    (10, 1, "PRESENT"), (10, 2, "PRESENT"), (10, 3, "ABSENT"), (10, 4, "PRESENT"), (10, 6, "PRESENT"), (10, 11, "EXCUSED");