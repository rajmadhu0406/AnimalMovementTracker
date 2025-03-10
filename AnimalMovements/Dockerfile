# Use an official OpenJDK runtime as base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy and install dependencies separately for better caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and install dependencies
RUN chmod +x mvnw && \
    ./mvnw dependency:go-offline

# Copy the entire project
COPY src ./src

# Expose the port that the application runs on
EXPOSE 8080


# # Run the Spring Boot application in debug mode on port 5005
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8080"]
