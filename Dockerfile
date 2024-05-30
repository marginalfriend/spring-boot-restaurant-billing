# Stage 1: Build the application
FROM ubuntu:22.04
FROM maven:3.9.7-amazoncorretto-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file from the target directory into the container
COPY --from=build /app/target/wmb-3.2.5.jar /app/wmb-3.2.5.jar

# Expose the port that the application runs on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "wmb-3.2.5.jar"]