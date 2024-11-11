


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


if [ "$ENV" = "dev" ]; then
  echo "Free disk space info before cleaning:"
  df -h --total | awk '/total/ {print "Total: " $2 ", Used: " $3 ", Free: " $4}'
  echo "Stopping and removing containers..."
  docker compose down
  echo "Removing existing pgdata volume..."
  docker volume ls
  docker volume rm -f live_pgdata
  docker volume ls
#   echo "Clear docker cache"
#   docker system prune -a -f
#   docker system prune -a --volumes -f
  # echo "Remove Unused docker images"
  # docker image prune -a -f 
  # echo "Remove Unused docker containers, volumes, networks"
  # docker container prune -f
  docker volume prune -f
  docker network prune -f
  echo "Free disk space info after cleaning:"
  df -h --total | awk '/total/ {print "Total: " $2 ", Used: " $3 ", Free: " $4}'
fi


# rm -rf target/

mvn clean package -DskipTests
# mvn package -DskipTests

# Enable debug logging for Docker Compose
# export COMPOSE_LOG_LEVEL=DEBUG

# Build and start the Docker containers with the specified environment
# sudo docker-compose up --build
docker compose up --build