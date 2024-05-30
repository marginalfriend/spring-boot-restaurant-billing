# Stage 1: Build the application
FROM maven:3.8.6-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17.0.1-slim-bullseye

# Set the working directory
WORKDIR /app

# Copy the jar file from the target directory into the container
COPY target/wmb.jar /app/wmb-0.jar

# Expose the port that the application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "wmb.jar"]
