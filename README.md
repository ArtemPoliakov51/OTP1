# ATTENDANCE CHECKER

This is the repository for the Attendance Checker app developed in a Software Engineering Project course. The project lasted from 15.1.2026 to 6.5.2026.

The pupose of the project was to learn and apply modern software engineering practices, especially ones in the DevOps methodology. This repository is intended for anyone who wants to learn about the software engineering process and to those who wish to try out the application produced during the project.

## Product Vision

The Attendance Checker app is designed for teachers at every education level, from primary school to university, as well as for people who run and operate independent courses and classes, to efficiently keep track of student attendance in these courses.
</br>

### Key Features and Functionality
- Record attendance for each student or course.
- Generate attendance reports for individual students and courses.
- Option to mark students present, absent or excused for attendance checks.
- Add notes to attendance records for students in individual attendance checks.

### Localization

The application supports four different languages: English, Finnish, Japanese and Greek. Localization can be changed at any point when using the app. 
Changing the localization changes the UI texts to the selected language and transforms the dates and times to the format used in the selected localization country.
Localization can be changed by pressing the <i>Language</i> button in the side panel. This opens a modal window where all the supported languages are displayed.
</br>

Notice: User inputed texts may be lost if localization changed while having unsaved changes on the main window. 
Save and move to a more stable stage when changing the localization to ensure that data is not lost.

### Techonologies
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
- <b>SonarQube</b> and <b>CheckStyle</b> for code quality and security analysis.

## Project Plan and Sprint Structure

This project followed Agile project development approach.</br>

Scrum was selected as the agile framework for the project. Sprint length was two weeks and there were eight sprints in total.

### Sprint 1 - Project Planning & Vision

Sprint 1 focused on planning the project and the product to be developed. 
A project plan documentation was created and product vision was written. User stories were written for the product.

Sprint 1 Review report can be read here: [Sprint 1 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_1_Review_Report.pdf) </br>

#### Project Plan Summary

The project plan focused on the actual project and its topics that needed to be covered instead of the product to be developed. The project plan included the following topics:
- Overview - Problem summary, Intended audience and Main features
- Objectives - Learning goals
- Scope and Deliverables - Application, Documentation
- Project Timeline - Sprints
- Resource Allocation - Team, Software, Hardware and Tools
- Risk Management - Risks, Mitigation
- Testing and Quality Assurance - Testing types, Criteria, Tools
- Documentation and Reporting - Documentation forms

The full project plan can be read here: [Attendance Checker Project Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/OTP1_Project_Plan.pdf)

#### Product Vision

The Product Vision describes the product and its key features and target audience. The Product Vision contains a statement about the problem the product will solve and what value it will provide. The Attendance Checker application is for teachers who need an easier and more efficient way to keep track of student attendance.

The full Product Vision can be reviewed here: [Attendance Checker Product Vision](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/Product%20vision_Attendance_checker.pdf)

#### User Stories

User stories were developed based on the Product Vision and other ideas. The user stories make up funtional requirements and backlog for the application. 

The user stories can be read here: [Attendance Checker User Stories](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/Attendance%20Checker%20-%20User%20Stories_updated.pdf)

### Sprint 2 - Requirements & Database

Sprint 2 focused on database design and implementation along with defining the functional requirements of the appliaction based on the user strories. The UI development and unit testing also began during this sprint.

Sprint 2 Planning report can be read here: [Sprint 2 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_2_planning_report.md) </br>
Sprint 2 Review report can be read here: [Sprint 2 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_2_Review_Report.md) </br>

#### Functional Requirements

User stories written in Sprint 1 were used as the base for the functional requirements. 
A Use Case diagram was created showing the main actor, Teacher, and the use cases they can performe. 
Some of the main functionalitites are:
- Creating new attendance checks for a course
- Marking stusdents as absent, present or excused for an attendance check
- Reviewing course and student attendance reports

All use cases and functionalities can be seen in the Use Case diagram: [Attendance Checker Use Case Diagram](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/use_case_diagram_v2.png) </br>
Note that this diagram contains another actor that was not part of the project this time.

#### Database Design and Implementation

MariaDB was selected as the database server for the application. The database is a local database running on the users computer. Database was designed based on the requirments and functionalities that were defined. An ER-diagram and a relational schema were produced.

The original ER-diagram can be seen here: [Attendance Checker ER Diagram](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/attendance_checker_ER_diagram_v2_3.png) </br>
The original Relational schema can be seen here: [Attendance Checker Relational Schema](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/attendance_checker_relational_schema_v2_3.png) </br>

The database is created with a script but the software uses Jakarta Persistance API (JPA), so the database tables are created with entity classes when entities are firts persisted in to the database. All the tables are created when the seed data is inserted in to the database.

#### Unit Testing

Unit test were written for the DAO (Data Access Object) classes to test CRUD operations on the database. CRUD operations test that data is created, read, updated and deleted correctly. JUnit5 was used to write the test classes and functions. JaCoCo was used to generate code coverage reports. 

#### UI Design

UI design included coming up with and deciding on the different UI views the application needs. 
Figma was used to create a wireframe for the UI. The wireframe includes views for:
- Login
- All Courses/ My Courses
- Selected Course
- Selected Course Student List
- Selected Course Add Students
- Selected Attendance Check
- Course Attendance Report
- Student Attendance Report
- Course Creation (this has been disabled in the final version due to database localization)

Some of the screens were coded and implemented using JavaFX during this sprint. </br>
The wireframe can be seen here: [Attendance Checker UI Wireframe](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/Attendance_checker_wireframe.pdf) </br>

### Sprint 3 - UI Implementation & CI

Sprint 3 focused on setting up the CI/CD pipeline with Jenkins and implementing the UI. 
Frontend and backend coding was carried out to implement the functionalities required by the UI.

Sprint 3 Planning report can be read here: [Sprint 3 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_3_planning_report.md) </br>
Sprint 3 Review report can be read here: [Sprint 3 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_3_Review_Report.md) </br>

#### UI Implementation

Rest of the views or screens designed were coded using JavaFX. Most of the functionalities defined earlier were implemented.

#### Jenkins Pipeline

Jenkins pipeline was set up during this sprint to enforce CI/CD. The pipeline was implemented using the Jenkinsfile. 
The pipeline includes, at this point of the project, the following stages:
- Git checkout
- Maven build
- JaCoCo report
- JUnit results publishing
- Coverage publishing
- Docker image build
- Push to Docker Hub

Running the Docker image was not fully functional during this sprint since database connection from the container and JavaFX needed to be figured out.

### Sprint 4 - Docker Containerization

Sprint 4 focused on Docker containerization and finalizing the functional prototype of the application. 
The project was also prsented to the other groups in the course and for this a presentation was prepared.

Sprint 4 Planning report can be read here: [Sprint 4 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_4_Planning_Report.md) </br>
Sprint 4 Review report can be read here: [Sprint 4 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_4_Review_Report.md) </br>

#### Docker Containerization

Docker images are run in containers. Containers make it possible to run the software easily in different environments. Containers contain all the necessary libraries, tools and code for the application to run. Containers make applications portable.

During this sprint the Docker implementation was updated to support JavaFX GUI and connetion to a database that is on a server outside of the container. Tha application works through Xming and database connection is formed based on the variables defined in the .env file when Docker container is started with Docker Compose.

#### Prototype Presentation

A presentation was prepared to present the software and the development steps and phases to the other groups on the course. Presentation also included a video demonstration of the application and its functionalities deployed in a Docker container.

### Sprint 5 - UI Localization

Sprint 5 focused on the localization of the UI. The UI supports four different languages: English, Finnish, Japanese and Greek. All the static UI texts were translated to these languages and are dynamically changed when a language is selected

Sprint 5 Planning report can be read here: [Sprint 5 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_5_Planning-Report.md) </br>
Sprint 5 Review report can be read here: [Sprint 5 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_5_Review_Report.pdf) </br>

#### Localization Implementation

The UI localization works through resource bundle files located in the resources directory. The files contain key-value pairs and UI texts are gotten from the files based on the key. There are four different files for the four locales:
- UITextBundle_el_GR.properties (Greek, Greece)
- UITextBundle_en_US.properties (English, United States)
- UITextBundle_fi_FI.properties (Finnish, Finland)
- UITextBundle_ja_JP.properties (Japanese, Japan)

Dates and times are also updated based on the selected locale to match the country of the locale. A button was added to the UI screen allowing user to change the language while using the application.

### Sprint 6 - Database Localization

Sprint 6 focused on the localization of the database for the four supported languages. Database localization included identifiying the data to be localized which was teacher and student names as well as course names. Dates, times and course codes were not localized for the database since they are needed for the application logic and are localized in the code. For more brainstorming and exaplanations behind the choices refer to this notes file: [Attendance Checker DB Localization Brainstorming](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/attendance_checker_database_localization_notes.pdf)

Sprint 6 Planning report can be read here: [Sprint 6 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_6_Planning-Report.md) </br>
Sprint 6 Review report can be read here: [Sprint 6 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_6_Review_Report.md) </br>

#### DB Localization Implementation

Database was localized using field localization method. With this method every data column, or field, that needed to be localized was copied and given a different field name based on the language of the data in the column. For example, instead of the single "firstname" field, the database now has "firstname_en", "firstname_fi", "firtsname_ja" and "firstname_el" fields that contain the translated firstnames.

The ER-diagram was also updated for the field localization: [Localized ER Diagram](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/ER_diagram_localized.png) </br>
The relational schema was also updared for the field localization: [Localized Relationa Schema](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/relational_schema_localized.png) </br>

#### Code Clean Up

The codebase was analysed using CheckStyle and SonarQube analysing tools. These analyses pointed out issues related to the style and function of the code. CheckStyle analysis included issues related to missing Javadoc comments, long code lines and naming conventions among many others. SonarQube flagged some possible exception issues and duplicate code. 

For a more thorough review of the analysises refer to this report: [Statistical Code Review Report](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/attendance_checker_statistical_code_review_report.pdf) </br>

Some of the major issues were fixed in this sprint but most of the issues were postponed to the next sprint.

### Sprint 7 - Quality Assurance

Sprint 7 focused on quality assurance by continuing the code clean up and testing the application through User Acceptance Testing and evaluating the application with a heuristic evaluation.

Sprint 7 Planning report can be read here: [Sprint 7 Plan](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint_planning/Sprint_7_planning_report.md) </br>
Sprint 7 Review report can be read here: [Sprint 7 Review](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/sprint%20reviews/Sprint_7_review_report.pdf) </br>

#### User Acceptance Testing (UAT)

Ten test cases were prepared to perform user acceptance testing on the application. The test cases were created based on the functional requirements defined at the begining of the project. The goal was to ensure that all the functionalities work as intended. All tests were passed successfully by all the testers meaning that the application passed the acceptance testing.

For the full review of the test cases and test runs refer to this report: [User Acceptance Testing Report](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/attendance_checker_user_acceptance_testing_report.pdf) </br>

#### Heuristic Evaluation

The application was evaluated based on the 10 heuristics by Jakob Nielsen. Heuristic evaluation analyses and tests the application focusing on the visuals and the functionality of the UI. The application has some cosmetic issues as well as minor and medium usability issues. These issues include:
- Bulky Finnish translations
- Lack of feedback and confirmation messages
- Lack of keyboard shortcuts and navigation options

For the full review of the evaluation and issues refer to this report: [Heuristic Evaluation Report](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/product_documents/attendance_checker_heuristic_evalutation.pdf) </br>

#### Jenkins Pipeline & SonarQube

A SonarQube Analsysis stage was added to the Jenkins pipeline. The pipeline performs the SonarQube analysis on the codebase when the project is built from Jenkins.

#### Code Clean Up & Refactoring

More clean up was done to the code to remove the security issues highlighted in SonarQube. Some of the CheckStyle code styling issues were also fixed. Code refactoring was done to decrease duplicate code. Refactoring included creating a utils class containing methods for calculating aatendance percentages for courses and attendance checks, and creating a UIComponent class that offers the top and side panel components for the view classes. These dropped the duplication percentage by around 12%.

### Sprint 8 - Documentation & Finalization

Sprint 8 focused on documenting the project and cleaning up the repository. Final clean up was also done for the codebase.

#### Use Case Diagram
[![Attendance Checker Use Case Diagram](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/use_case_diagram_v2.png)](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/use_case_diagram_v2.png)

#### ER Diagram
[![Attendance Checker ER Diagram](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/ER_diagram_localized.png)](https://github.com/ArtemPoliakov51/OTP1/blob/main/Documents/diagrams/ER_diagram_localized.png)

## Project Documentation

All project documentation including diagrams and sprint plans and reviews can be found from the following links:

Sprint planning reports: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint_planning](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint_planning) </br>
Sprint review reports: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint%20reviews](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/sprint%20reviews) </br>
Product documents: [https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/product_documents](https://github.com/ArtemPoliakov51/OTP1/tree/main/Documents/product_documents) </br>

## Application setup

### Prerequisites

These are needed to run the application:
- JDK 21 (Java)
- Maven 3.8 or higher (tested with 3.9)
- MariaDB
- IDE (tested with IntelliJ IDEA)

These are needed if you want to use Docker container or CI/CD pipeline:
- Docker
- Xming (Windows)(for the JavaFX GUI)
- Jenkins
- SonarQube (for codebase analysis)

### Setup

Clone the project to open it in your IDE:
```
git clone https://github.com/ArtemPoliakov51/OTP1.git
```
Create an empty database in MariaDB with the [database_user.sql script](https://github.com/ArtemPoliakov51/OTP1/blob/Docker-Implementation/sql/database_user.sql) in the sql folder.

### Run the App with an IDE

Run the Main class , found in src/main/java, to start the app.

You should be able to login as these users (email and password combo) after running the SeedDataInserter:

- Email: freya.stephens@email.com, Password: salasana
- Email: ingram.martin@email.com, Password: verySecret
- Email: donelly123@email.com, Password: password

### Run the App with a Docker Container

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
#### Optional

Push the image to the Docker Hub if you want:
```
docker push riikkakoo/attendance-checker:latest
```
Pull the image from the Docker Hub if you want:
```
docker pull riikkakoo/attendance-checker:latest
```
___
#### Windows

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

## Testing Instructions

The project has unit tests to make sure the DAO classes and databasa operations work correctly.

### Run Tests

Tests can be run from the terminal with Maven:

```
mvn test
```

### Code Coverage Report

Running the tests generates a JaCoCo Code Coverage report. The report can be viewed in a browser.

The html-file for the report can be found in target/site/jacoco directory. The file is named index.html. Run the file to open it in a browser.

## CI/CD - Jenkins Pipeline Setup

To use the Jenkins pipeline to build the project, you need to have Jenkins installed. <br></br>
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
      Select the SonarQube credentials token in the Server authentication token section.
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
You may need to change the path if you have saved the file to a different location.<br></br>
Make sure your Docker Engine is also running.<br></br>
Build project from the side panel of the pipeline project. 

## Repository Structure

<b>/Documents</b> - Contains documentation files directories as well as diagrams realted to project and product development</br>
<b>/sql</b> - Contains SQL scipts for the MariaDB database</br>
<b>/src</b> - Contains the code for the software</br>

Root directory has configurations files for Jenkins pipeline, Docker and SonarQube.

## Authors

<b>Riikka Kautonen</b> - Developer</br>
<b>Artem Poliakov</b> - Developer</br>
<b>Jere Pyrörökivi</b> - Developer</br>
<b>Zachris Zweygberg</b> - Developer</br>

Metropolia, Software Engineering Project Course One and Two, Spring 2026
