-- Create a user and grant privileges
CREATE USER 'attendance_user'@'localhost' IDENTIFIED BY 'attendance_password';

-- Grant all necessary privileges on the database to the user
GRANT ALL PRIVILEGES ON attendance_database.* TO 'attendance_user'@'localhost';

-- Apply changes
FLUSH PRIVILEGES;