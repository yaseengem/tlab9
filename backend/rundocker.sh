#!/bin/bash

# Exit on error
set -e

# Step 1: Build the Java application
echo "Building Java application..."
mvn clean package -DskipTests

# Step 2: Build the Docker image
echo "Building Docker image..."
docker build -t track_app .

# Step 3: Stop and remove existing containers
echo "Stopping and removing existing containers..."
docker compose down

# Step 4: Start new containers
echo "Starting new containers..."
docker compose up -d --build

echo "Deployment completed successfully!"