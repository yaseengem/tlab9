


#!/bin/bash

# Run this file using "sudo ./run.sh"
# This script is used to build and run the application in a Docker container.

# remove all existing containers
# sudo docker stop $(sudo docker ps -q)
# sudo docker rm -f $(sudo docker ps -aq)

# Load environment variables from .env file
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

# Check if ENV variable is set
if [ -z "$ENV" ]; then
  echo "ENV variable is not set in the .env file. Defaulting to 'dev'."
  ENV="dev"
fi

echo "Environment: $ENV"


export JAVA_HOME=/opt/jdk-23
export PATH=$JAVA_HOME/bin:$PATH

export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=$MAVEN_HOME/bin:$PATH

# Remove the existing volume if in dev environment
if [ "$ENV" = "dev" ]; then
  echo "Stopping and removing containers..."
  docker compose down
  echo "Removing existing pgdata volume..."
  docker volume ls
  docker volume rm -f live_pgdata
  docker volume ls
fi


# rm -rf target/

mvn clean package -DskipTests
# mvn package -DskipTests

# Enable debug logging for Docker Compose
# export COMPOSE_LOG_LEVEL=DEBUG

# Build and start the Docker containers with the specified environment
# sudo docker-compose up --build
docker compose up --build