FROM  maven:latest AS build
LABEL authors="OTP1 Group 5"

WORKDIR /app

# Install GUI libraries
RUN apt-get update && apt-get install -y \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    && rm -rf /var/lib/apt/lists/*

# Download JavaFX SDK
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the shaded JAR
RUN mvn clean package -DskipTests

# Copy fat JAR
COPY target/attendance_checker.jar app.jar

# Set DISPLAY for Windows (Xming)
ENV DISPLAY=host.docker.internal:0.0

# Run the **shaded JAR** with JavaFX modules
CMD ["java", "--module-path", "/javafx-sdk/lib", "--add-modules", "javafx.controls", "-jar", "target/attendance_checker.jar"]