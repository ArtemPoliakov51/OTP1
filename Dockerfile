FROM  maven:latest AS build
LABEL authors="OTP1 Group 5"

WORKDIR /app

COPY pom.xml .

COPY . /app

RUN mvn package

CMD ["java", "-jar", "target/attendance_checker.jar"]