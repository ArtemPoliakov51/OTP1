-- Test: Course requires existing TeacherID; Created has default; Archived can be NULL
START TRANSACTION;
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Barbara', 'Liskov', 'barbara@example.com', 'hash');
SET @teacher_id := LAST_INSERT_ID();

-- Valid course
INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Programming 101', 'CS101', 'active', @teacher_id);
SET @course_id := LAST_INSERT_ID();
SELECT assert_true(@course_id IS NOT NULL, 'Course should insert');

-- Default Created populated; Archived default NULL
SELECT assert_true((SELECT Created IS NOT NULL FROM courses WHERE CourseID=@course_id), 'courses.Created should auto-populate');
SELECT assert_true((SELECT Archived IS NULL FROM courses WHERE CourseID=@course_id), 'courses.Archived should default to NULL');

-- Invalid: TeacherID must exist
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO courses (Name, Identifier, Status, TeacherID)
    VALUES ('Ghost Course', 'GHOST', 'active', 999999);
END;
SELECT assert_true(@failed = 1, 'FK courses.TeacherID should enforce existence');
ROLLBACK;

-- Test: ON UPDATE CASCADE of TeacherID to courses
START TRANSACTION;
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Edsger', 'Dijkstra', 'edsger@example.com', 'hash');
SET @t1 := LAST_INSERT_ID();
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Donald', 'Knuth', 'donald@example.com', 'hash');
SET @t2 := LAST_INSERT_ID();

INSERT INTO courses (Name, Identifier, Status, TeacherID)
VALUES ('Algorithms', 'CS201', 'active', @t1);
SET @c1 := LAST_INSERT_ID();

-- Update @t1’s TeacherID via surrogate trick (requires direct PK update; allowed by schema with ON UPDATE CASCADE)
-- MySQL allows updating PK; CASCADE should propagate.
UPDATE teachers SET TeacherID = @t2 + 100 WHERE TeacherID = @t1;
SET @new_tid := @t2 + 100;

SELECT assert_true(
               (SELECT TeacherID = @new_tid FROM courses WHERE CourseID=@c1),
               'TeacherID update should cascade to courses'
       );
ROLLBACK;
