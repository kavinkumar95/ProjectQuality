# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install -y curl

# Set the working directory in the container
WORKDIR /usr/src/app

# Copy the application JAR file into the container at the working directory
COPY ProjectQuality-0.0.1-SNAPSHOT.jar .

# Specify the command to run on container start
CMD ["java", "-jar", "ProjectQuality-0.0.1-SNAPSHOT.jar"]