-- Test: attends requires existing CourseID and StudentID; composite PK uniqueness; ON DELETE CASCADE from both sides
START TRANSACTION;
-- Teacher + Course
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Linus', 'Torvalds', 'linus@example.com', 'hash');
SET @tid := LAST_INSERT_ID();

INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Operating Systems', 'CS301', 'active', @tid);
SET @cid := LAST_INSERT_ID();

-- Student
INSERT INTO students (Firstname, Lastname, Email)
VALUES ('Ken', 'Thompson', 'ken@example.com');
SET @sid := LAST_INSERT_ID();

-- Valid enrolment
INSERT INTO attends (CourseID, StudentID) VALUES (@cid, @sid);
SELECT assert_true(ROW_COUNT() = 1, 'attends should accept valid pair');

-- Duplicate should fail due to composite PK
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO attends (CourseID, StudentID) VALUES (@cid, @sid);
END;
SELECT assert_true(@failed = 1, 'attends composite PK should prevent duplicates');

-- FK enforcement
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO attends (CourseID, StudentID) VALUES (999999, @sid);
END;
SELECT assert_true(@failed = 1, 'attends requires valid CourseID');

SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO attends (CourseID, StudentID) VALUES (@cid, 999999);
END;
SELECT assert_true(@failed = 1, 'attends requires valid StudentID');

-- ON DELETE CASCADE: delete student removes enrolment
DELETE FROM students WHERE StudentID=@sid;
SELECT assert_true((SELECT COUNT(*) FROM attends WHERE CourseID=@cid) = 0, 'Deleting student should cascade to attends');
ROLLBACK;
