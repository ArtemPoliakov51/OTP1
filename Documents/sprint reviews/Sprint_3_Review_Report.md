# SPRINT 3 REVIEW REPORT
## 12.2-5.3.2026

### Sprint Goal
Main goal of Sprint 3 was to extend the functional prototype and improve backend stability. During this sprint we focused on full integration between backend/database/JavaFX, CI/CD with Jenkins, automated tests & code coverage, and Docker image creation.

### Completed User Stories / Tasks
- Full integration between backend, database and JavaFX
  - **JavaFX UI calls Controllers, Controllers call DAO layer, DAO uses JPA EntityManager to query MariaDB.**
  - **The application flow starts from Main -> LoginView -> AllCoursesView and continues to course/student/attendance screens.**

- Jenkins CI/CD pipeline setup
  - **Jenkins pipeline exists and includes stages: Git checkout, Maven build, JaCoCo report, JUnit results publishing, coverage publishing, Docker build and push to Docker Hub.**

- Code coverage report generation (JaCoCo)
  - **JaCoCo configured in pom.xml and also triggered in Jenkins pipeline.**

- Docker image creation
  - **Dockerfile exists and can build/run the jar using Maven build inside the container.**

- Automated unit testing with code coverage
  - **JUnit dependency and Maven Surefire configuration exist.**

- Docker local testing
  - **Dockerfile exists and image builds.**

## Demo Summary
1. Application launch: Main -> Login screen
2. Login with teacher credentials
3. “My Courses” view: loading courses from database
4. Course view: attendance checks list, navigation
5. Student management: add/remove students and see DB updates
6. Reports: generate and save attendance report files
7. Jenkins pipeline: checkout -> build -> tests -> JaCoCo -> coverage -> docker build
8. Docker build: building container image from Dockerfile

## What Went Well
The tasks planned for this sprint were completed. In particular, it is worth highlighting that the end-to-end integration (JavaFX + DAO + JPA + MariaDB) is working. Jenkins pipeline includes coverage publishing and Docker deployment steps. Project structure is clear: Views/Controllers/DAO/Entities are separated.

## What Could Be Improved
Interaction within the team is still somewhat difficult. The demonstration of completed work also needs improvement. Deadlines are only partially met, so the organization of work should be improved in order to complete tasks within the set time limits.

## Next Sprint Focus
In Sprint 4 we will focus on finalizing the prototype, containerizing the full application with Docker, publishing the Docker image to Docker Hub, testing the deployed image.

## Team time spent 
- Riikka Kautonen (RiikkaKoo) 47h.
- Artem Poliakov (ArtemPoliakov51) 19h.
- Jere Pyörökivi (Jere-4) 43h.
