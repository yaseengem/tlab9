#!/bin/bash

# Exit on error
set -e

# Load environment variables from .env file
export $(grep -v '^#' .env | xargs)

# Export environment paths
export JAVA_HOME=/opt/jdk-23
export PATH=$JAVA_HOME/bin:$PATH
export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=$MAVEN_HOME/bin:$PATH

# Step 1: Get the current commit ID
COMMIT_ID=$(git rev-parse --short HEAD)

# Step 2: Determine the image tag based on the environment
if [ "$ENV" == "dev" ]; then
  IMAGE_TAG="dev"
else
  IMAGE_TAG=$COMMIT_ID
fi

# Step 3: Echo the image name
echo "Docker image name: track_app:$IMAGE_TAG"

# Step 4: Build the Java application
echo "Building Java application..."
mvn clean package -DskipTests

# Step 5: Build the Docker image with the appropriate tag
echo "Building Docker image..."
docker build -t track_app:$IMAGE_TAG .

# Step 6: Stop the existing track_app container in the track_app_dev project
echo "Stopping the existing track_app container..."
docker compose -f docker-compose.track_app.yml stop track_app

# Step 7: Start the track_app container in the track_app_dev project
echo "Starting the track_app container..."
docker compose -f docker-compose.track_app.yml up -d --build track_app

echo "Deployment completed successfully!"