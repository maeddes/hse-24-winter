# Use the official OpenJDK base image
FROM eclipse-temurin:21-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory to the container
COPY target/backendspringboot-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot app will run
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
