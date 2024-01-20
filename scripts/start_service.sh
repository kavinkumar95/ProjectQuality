#!/bin/bash

# Start the Docker containers using Docker Compose

docker-compose -f docker-compose.yaml up -d

# Check if the containers are running
if [ $? -eq 0 ]; then
    echo "Docker containers started successfully!"
    sleep 10
    bash create_excel.sh
else
    echo "Failed to start Docker containers. Please check the Docker Compose configuration and try again."
fi
