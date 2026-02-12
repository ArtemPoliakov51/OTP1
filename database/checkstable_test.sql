-- Test: checks requires valid StudentID and CheckID; composite PK; cascading deletes; Notes default ''
START TRANSACTION;
-- Teacher + Course
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Margaret', 'Hamilton', 'margaret@example.com', 'hash');
SET @tid := LAST_INSERT_ID();

INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Software Engineering', 'SE101', 'active', @tid);
SET @cid := LAST_INSERT_ID();

-- Student
INSERT INTO students (Firstname, Lastname, Email)
VALUES ('Dennis', 'Ritchie', 'dennis@example.com');
SET @sid := LAST_INSERT_ID();

-- Attendance check
INSERT INTO attendance_checks (`Date`, `Time`, CourseID)
VALUES (DATE('2025-10-01'), TIME('10:00:00'), @cid);
SET @chk := LAST_INSERT_ID();

-- Valid attendance entry; Notes should default to '' (empty string)
INSERT INTO checks (StudentID, CheckID, AttendanceStatus) VALUES (@sid, @chk, 'present');
SELECT assert_true(
               (SELECT Notes='' FROM checks WHERE StudentID=@sid AND CheckID=@chk),
               'checks.Notes should default to empty string'
       );

-- Duplicate composite PK should fail
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO checks (StudentID, CheckID, AttendanceStatus) VALUES (@sid, @chk, 'absent');
END;
SELECT assert_true(@failed = 1, 'checks composite PK prevents duplicates');

-- FK enforcement: bad StudentID
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO checks (StudentID, CheckID, AttendanceStatus) VALUES (999999, @chk, 'present');
END;
SELECT assert_true(@failed = 1, 'checks requires valid StudentID');

-- FK enforcement: bad CheckID
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO checks (StudentID, CheckID, AttendanceStatus) VALUES (@sid, 999999, 'present');
END;
SELECT assert_true(@failed = 1, 'checks requires valid CheckID');

-- ON DELETE CASCADE: delete attendance_check should remove checks
DELETE FROM attendance_checks WHERE CheckID=@chk;
SELECT assert_true((SELECT COUNT(*) FROM checks WHERE StudentID=@sid) = 0, 'Deleting attendance_check should cascade to checks');
ROLLBACK;
