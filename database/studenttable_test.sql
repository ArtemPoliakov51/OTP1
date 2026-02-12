-- Test: Insert valid student
START TRANSACTION;
INSERT INTO students (Firstname, Lastname, Email)
VALUES ('Grace', 'Hopper', 'grace@example.com');
SELECT assert_true(ROW_COUNT() = 1, 'Should insert a valid student');

-- Not null checks
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO students (Firstname, Lastname, Email)
    VALUES (NULL, 'Curie', 'marie@example.com');
END;
SELECT assert_true(@failed = 1, 'Students.Firstname should be NOT NULL');
ROLLBACK;
