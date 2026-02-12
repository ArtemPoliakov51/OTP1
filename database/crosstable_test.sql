START TRANSACTION;
-- Teacher + Course
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Guido', 'van Rossum', 'guido@example.com', 'hash');
SET @tid := LAST_INSERT_ID();

INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Python Basics', 'PY101', 'active', @tid);
SET @cid := LAST_INSERT_ID();

-- Student A and B
INSERT INTO students (Firstname, Lastname, Email) VALUES ('Alice', 'Doe', 'alice@example.com');
SET @s1 := LAST_INSERT_ID();
INSERT INTO students (Firstname, Lastname, Email) VALUES ('Bob', 'Doe', 'bob@example.com');
SET @s2 := LAST_INSERT_ID();

-- Enrol both
INSERT INTO attends (CourseID, StudentID) VALUES (@cid, @s1), (@cid, @s2);

-- Two attendance checks
INSERT INTO attendance_checks (`Date`, `Time`, CourseID) VALUES (CURRENT_DATE(), '09:00:00', @cid);
SET @chk1 := LAST_INSERT_ID();
INSERT INTO attendance_checks (`Date`, `Time`, CourseID) VALUES (CURRENT_DATE(), '10:00:00', @cid);
SET @chk2 := LAST_INSERT_ID();

-- Checks for both students
INSERT INTO checks (StudentID, CheckID, AttendanceStatus) VALUES
                                                              (@s1, @chk1, 'present'),
                                                              (@s2, @chk1, 'absent'),
                                                              (@s1, @chk2, 'late'),
                                                              (@s2, @chk2, 'present');

-- Delete course: should cascade to attendance_checks and in turn to checks, and also to attends
DELETE FROM courses WHERE CourseID=@cid;

-- Verify cascades
SELECT assert_true((SELECT COUNT(*) FROM attendance_checks WHERE CourseID=@cid)=0, 'Delete course cascades to attendance_checks');
SELECT assert_true((SELECT COUNT(*) FROM checks WHERE CheckID IN (@chk1, @chk2))=0, 'Delete attendance_checks cascades to checks');
SELECT assert_true((SELECT COUNT(*) FROM attends WHERE CourseID=@cid)=0, 'Delete course cascades to attends');
ROLLBACK;
