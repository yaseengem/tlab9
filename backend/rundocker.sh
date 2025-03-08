#!/bin/bash

# Exit on error
set -e


# Export environment paths
export JAVA_HOME=/opt/jdk-23
export PATH=$JAVA_HOME/bin:$PATH
export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=$MAVEN_HOME/bin:$PATH


# Step 1: Get the current commit ID
COMMIT_ID=$(git rev-parse --short HEAD)

# Step 2: Build the Java application
echo "Building Java application..."
mvn clean package -DskipTests

# Step 3: Build the Docker image with commit ID
echo "Building Docker image..."
docker build -t track_app:$COMMIT_ID .

# Step 4: Stop the existing track_app container in the track_app_dev project
echo "Stopping the existing track_app container..."
docker compose -p track_app_dev stop track_app

# Step 5: Start the track_app container in the track_app_dev project
echo "Starting the track_app container..."
docker compose -p track_app_dev up -d --build track_app

echo "Deployment completed successfully!"