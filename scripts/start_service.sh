#!/bin/bash

# Set the path to your docker-compose.yml file
docker_compose_file="docker-compose.yml"


# Start the Docker containers using Docker Compose
docker-compose up -d

# Check if the containers are running
if [ $? -eq 0 ]; then
    echo "Docker containers started successfully!"
else
    echo "Failed to start Docker containers. Please check the Docker Compose configuration and try again."
fi
