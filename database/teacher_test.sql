-- Test: Insert valid teacher
START TRANSACTION;
INSERT INTO teachers (Firstname, Lastname, Email, Password)
VALUES ('Ada', 'Lovelace', 'ada@example.com', 'hashed_pw');
SELECT assert_true(ROW_COUNT() = 1, 'Should insert a valid teacher');

-- Not null checks
-- Expect failure: Email NULL
SET @failed := 0;
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION SET @failed := 1;
    INSERT INTO teachers (Firstname, Lastname, Email, Password)
    VALUES ('Alan', 'Turing', NULL, 'hash');
END;
SELECT assert_true(@failed = 1, 'Teachers.Email should be NOT NULL');
ROLLBACK;
