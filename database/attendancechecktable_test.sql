-- Test: attendance_checks requires existing CourseID
START TRANSACTION;
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Tim', 'Berners-Lee', 'tim@example.com', 'hash');
SET @tid := LAST_INSERT_ID();

INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Web Tech', 'WEB101', 'active', @tid);
SET @cid := LAST_INSERT_ID();

-- Valid insert
INSERT INTO attendance_checks (`Date`, `Time`, CourseID)
VALUES (DATE('2025-09-01'), TIME('09:00:00'), @cid);
SET @check_id := LAST_INSERT_ID();
SELECT assert_true(@check_id IS NOT NULL, 'attendance_checks should insert with valid course');

-- Invalid course
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO attendance_checks (`Date`, `Time`, CourseID)
    VALUES (CURRENT_DATE(), CURRENT_TIME(), 999999);
END;
SELECT assert_true(@failed = 1, 'FK attendance_checks.CourseID should enforce existence');
ROLLBACK;
