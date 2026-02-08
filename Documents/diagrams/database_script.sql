-- Dumping database structure for attendance_database
DROP DATABASE IF EXISTS `attendance_database`;
CREATE DATABASE IF NOT EXISTS `attendance_database` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci */;
USE `attendance_database`;

-- Dumping structure for table attendance_database.teachers
DROP TABLE IF EXISTS `teachers`;
CREATE TABLE IF NOT EXISTS `teachers` (
  `TeacherID` int(11) NOT NULL AUTO_INCREMENT,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `Password` varchar(255) NOT NULL,
  PRIMARY KEY (`TeacherID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.students
DROP TABLE IF EXISTS `students`;
CREATE TABLE IF NOT EXISTS `students` (
  `StudentID` int(11) NOT NULL AUTO_INCREMENT,
  `Firstname` varchar(50) NOT NULL,
  `Lastname` varchar(50) NOT NULL,
  `Email` varchar(100) NOT NULL,
  PRIMARY KEY (`StudentID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.courses
DROP TABLE IF EXISTS `courses`;
CREATE TABLE IF NOT EXISTS `courses` (
  `CourseID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `Identifier` varchar(50) NOT NULL,
  `Status` varchar(20) NOT NULL,
  `Created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Archived` datetime DEFAULT NULL,
  `TeacherID` int(11) NOT NULL,
  PRIMARY KEY (`CourseID`),
  KEY `FK_courses_teachers` (`TeacherID`),
  CONSTRAINT `FK_courses_teachers` FOREIGN KEY (`TeacherID`) REFERENCES `teachers` (`TeacherID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.attendance_checks
DROP TABLE IF EXISTS `attendance_checks`;
CREATE TABLE IF NOT EXISTS `attendance_checks` (
  `CheckID` int(11) NOT NULL AUTO_INCREMENT,
  `Date` date NOT NULL,
  `Time` time NOT NULL,
  `CourseID` int(11) NOT NULL,
  PRIMARY KEY (`CheckID`),
  KEY `FK_attendance_checks_courses` (`CourseID`),
  CONSTRAINT `FK_attendance_checks_courses` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`CourseID`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.attends
DROP TABLE IF EXISTS `attends`;
CREATE TABLE IF NOT EXISTS `attends` (
  `CourseID` int(11) NOT NULL,
  `StudentID` int(11) NOT NULL,
  PRIMARY KEY (`CourseID`, `StudentID`),
  KEY `FK_attends_students` (`StudentID`),
  CONSTRAINT `FK_attends_courses` FOREIGN KEY (`CourseID`) REFERENCES `courses` (`CourseID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_attends_students` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.

-- Dumping structure for table attendance_database.checks
DROP TABLE IF EXISTS `checks`;
CREATE TABLE IF NOT EXISTS `checks` (
  `StudentID` int(11) NOT NULL,
  `CheckID` int(11) NOT NULL,
  `AttendanceStatus` varchar(20) NOT NULL,
  `Notes` varchar(800) DEFAULT '',
  PRIMARY KEY (`StudentID`, `CheckID`),
  KEY `FK_checks_attendance_checks` (`CheckID`),
  CONSTRAINT `FK_checks_students` FOREIGN KEY (`StudentID`) REFERENCES `students` (`StudentID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_checks_attendance_checks` FOREIGN KEY (`CheckID`) REFERENCES `attendance_checks` (`CheckID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Data exporting was unselected.
