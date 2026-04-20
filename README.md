# ATTENDANCE CHECKER

This is the repository for the Attendance Checker app developed in a Software Engineering Project course.</br>

The Attendance Checker app is designed for teachers at every education level, from primary school to university, as well as for people who run and operate independent courses and classes, to efficiently keep track of student attendance in these courses.
</br>
</br>

<b>Key Features and Functionality:</b>
- Record attendance for each student or course.
- Generate attendance reports for individual students and courses.
- Option to mark students present, absent or excused for attendance checks.
- Add notes to attendance records for students in individual attendance checks.

</br>
<b>Localization:</b>
</br>
</br>

The application supports four different languages: English, Finnish, Japanese and Greek. Localization can be changed at any point when using the app. 
Changing the localization changes the UI texts to the selected language and transforms the dates and times to the format used in the selected localization country.
Localization can be changed by pressing the <i>Language</i> button in the side panel. This opens a modal window where all the supported languages are displayed.
</br>

Notice: User inputed texts may be lost if localization changed while having unsaved changes on the main window. 
Save and move to a more stable stage when changing the localization to ensure that data is not lost.

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
- <b>Logback</b> for logging Hibernate messages on to the console
- <b>JUnit5</b> unit-testing framework
- <b>H2 Database Engine</b> for running the unit-tests in to a temporary database
- <b>BCrypt</b> for password hashing and comparison
- <b>ResourceBundle</b> files for storing the locale specific UI texts.
- <b>Google Translate</b> and <b>Chat GPT</b> for Japanese and Greek translations.

## Architecture Design

### Use Case Diagram
The Use Case diagram demonstrates the different ways users (actors) may intreact with the system.

<img width="708" height="864" alt="use_case_diagram_v2" src="https://github.com/user-attachments/assets/f551c068-a035-4051-ad2e-30b4a6dac2c1" />

The Attendance Checker system has two actors, Teacher and Admin, but this project focuses only on the Teacher actor and their use cases or ways to interact with the system. The Teacher has seventeen different use cases.
Teacher can for example login and logout of the system, and they can mark students that are attending their courses present, absent or exused during different lessosns.


### ER Diagram
The Entity Relationship (ER) diagram illustrates the relational database entities and their relationships.

<img width="3506" height="2104" alt="attendance_checker_ER_diagram_v2_3_cropped" src="https://github.com/user-attachments/assets/e720d61e-6359-4cb5-b33e-59285d0d1c23" />

The ER diagram has four entities: Teacher, Student, Course and AttendanceCheck.</br>
All entities have unique IDs and other attributes.</br>

The ER diagram also has four relationships between the entities:
- The One-to-Many relationship between Teacher and Course entities is named Owns.
- The Many-to-Many relationship between Student and Course entities is named Attends.
- The One-to-Many relationship between Course and AttendanceCheck entities is named Contains.
- The Many-to-Many relationship between AttendanceCheck and Student entities is named Checks.

## Project Documentation

Sprint planning reports: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint_planning](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint_planning) </br>
Sprint review reports: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint%20reviews](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint%20reviews) </br>
Product documents: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/product_documents](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/product_documents) </br>

## Application setup
Clone the project to open it in your IDE:
```
git clone https://github.com/ArtemPoliakov51/OTP1.git
```
Create an empty database in MariaDB with the [database_user.sql script](https://github.com/ArtemPoliakov51/OTP1/blob/Docker-Implementation/sql/database_user.sql) in the sql folder.

## Run the App with an IDE

Run the Main class , found in src/main/java, to start the app.

You should be able to login as these users (email and password combo) after running the SeedDataInserter:

- Email: freya.stephens@email.com, Password: salasana
- Email: ingram.martin@email.com, Password: verySecret
- Email: donelly123@email.com, Password: password

## Run the App with a Docker Container

Create an .env file to the root of the project and add these environmental variables:
```
DB_USER=attendance_user
DB_PASSWORD=attendance_password
DB_URL=jdbc:mariadb://host.docker.internal:3306/attendance_database
```

If you would like to save attendance reports to your computer while running the app in a container, add these to the .env file:
```
EXPORT_DIR=/data
HOST_EXPORT_DIR=folderpath
```

Replace the 'folderpath' with a path to a folder where you want to save the reports to. The app will open this folder.
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

## Jenkins Pipeline Setup

To use the Jenkins pipeline to build the project, you need to have Jenkins installed. 
You also need to have Maven, Java, Docker and SonarQube and SonarScanner installed and added to the environmental variables on your computer.

### Jenkins Configuration

1. Install all necessary plugins:
   - Docker (Docker, Docker Pipeline)
   - Coverage Tool
   - SonarQube Scanner for Jenkins
2. The SonarQube Analysis stage of the pipeline uses credentials:
   - Generate a token in SonarQube
   - Add Jenkins credentials for SonarQube: Settings --> Credentials --> Add Credentials --> Secret text 
   ```
      Secret = the SonarQube token you generated
      ID = SonarQube or something similar
   ```
   - Configure SonarQube server: System --> SonarQube servers
   ```
      Name = SonarQubeServer
      Select the SonarQube token in the Server authentication token section for your installed SonarQubeServer.
   ```
3. Configure Jenkins Tools to use correct paths (if you have not already):
   - Maven
   - Java
   - Docker
   - SonarQube Scanner

### Jenkins Pipeline Creation

1. Click "New Item"
2. Enter a name and select "Pipeline"
3. Click "Ok"
4. Scroll down to "Pipeline" and select "Pipeline script from SCM"
5. Change SCM to Git
6. Repository URL is ```https://github.com/ArtemPoliakov51/OTP1.git```
7. Click "Save"

To successfully execute the pipeline, make sure you have SonarQube running. Run the SonarStart.bat:
```
C:\SonarQube\sonarqube-26.3.0.120487\bin\windows-x86-64\SonarStart.bat
```
You may need to change the path if you have saved the file to a different location.
Make sure your Docker Engine is also running.
Build project from the side panel of the pipeline project. 