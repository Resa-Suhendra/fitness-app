# Use the official OpenJDK base image
FROM openjdk:17-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/fitness-app-1.0.jar app.jar

# Expose the port your Spring Boot app is running on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
