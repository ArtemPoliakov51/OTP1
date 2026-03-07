# ATTENDANCE CHECKER - DOCKER COMPATIBLE VERSION

This is the repository for the Attendance Checker app developed in a Software Engineering Project course.</br>

The Attendance Checker app is designed for teachers at every education level, from primary school to university, as well as for people who run and operate independent courses and classes, to efficiently keep track of student attendance in these courses.
</br>
</br>

<b>Key Features and Functionality:</b>
- Create courses and record attendance for each student or course.
- Generate attendance reports for individual students and courses.
- Option to mark students present, absent or excused for attendance checks.
- Add notes to attendance records for students in individual attendance checks.

## Techonologies
The following technologies are used to build and run the application.
- <b>Java</b> programming language
- <b>JavaFX</b> application platform for the GUI
- <b>CSS</b> language for styling the user interface
- <b>Maven</b> build automation tool
- <b>MariaDB</b> relational database server
- <b>Docker</b> for containerization of the project
- <b>Jenkins</b> for CI/CD pipeline automation
- <b>Jakarta Persistence API (JPA)</b> for object-relational mapping
- <b>Hibernate</b> framework as the JPA implementation
- <b>JUnit5</b> unit-testing framework
- <b>H2 Database Engine</b> for running the unit-tests in to a temporary database

## Run the App with a Docker Container (work-in-progress)

Clone the project to open it in your IDE:
```
git clone https://github.com/ArtemPoliakov51/OTP1.git
```
</br>

Create an empty database and a user in MariaDB with the [database_user.sql script](https://github.com/ArtemPoliakov51/OTP1/blob/Docker-Implementation/sql/database_user.sql) in the sql folder.
<br></br>

Create an .env file to the root of the project and add these environmental variables:
```
DB_USER=attendance_user
DB_PASSWORD=attendance_password
DB_URL=jdbc:mariadb://host.docker.internal:3306/attendance_database
```
</br>

Build the Docker image:
```
docker build -t riikkakoo/attendance-checker:latest .
```
___
<b>OPTIONAL:</b></br>

Push the image to the Docker Hub if you want:
```
docker push riikkakoo/attendance-checker:latest
```
Pull the image from the Docker Hub if you want:
```
docker pull riikkakoo/attendance-checker:latest
```
___
<b>WINDOWS OS</b></br> 

To run the Docker container, make sure you have Xming installed and running.
<br></br>
Run the Docker container with Docker Compose:
```
docker compose up --build
```

You should be able to login as these users (email and password combo):

- Email: freya.stephens@email.com, Password: salasana
- Email: ingram.martin@email.com, Password: verySecret
- Email: donelly123@email.com, Password: password
