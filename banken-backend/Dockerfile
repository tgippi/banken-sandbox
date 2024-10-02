# Create a new image for running the application
FROM amazoncorretto:21.0.4-alpine3.18

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the previous stage
COPY target/banken-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which the Spring Boot application will run
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
