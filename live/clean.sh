


#!/bin/bash

# Run this file using "sudo ./run.sh"
# This script is used to build and run the application in a Docker container.

# remove all existing containers
sudo docker stop $(sudo docker ps -q)
sudo docker rm -f $(sudo docker ps -aq)

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



if [ "$ENV" = "dev" ]; then
  echo "Free disk space info before cleaning:"
  df -h --total | awk '/total/ {print "Total: " $2 ", Used: " $3 ", Free: " $4}'
  echo "Size of the parent folder:"
  du -sh ..

  echo "Stopping and removing containers..."
  docker compose down
  echo "Removing existing pgdata volume..."
  docker volume ls
  docker volume rm -f live_pgdata
  docker volume ls
  echo "Clear docker cache"
  docker system prune -a -f
  echo "Remove Unused docker images"
  docker image prune -a -f 
  echo "Remove Unused docker containers, volumes, networks"
  docker container prune -f
  docker volume prune -f
  docker network prune -f
  docker system prune -a --volumes -f
  echo "Free disk space info after cleaning:"
  df -h --total | awk '/total/ {print "Total: " $2 ", Used: " $3 ", Free: " $4}'
  echo "Size of the parent folder:"
  du -sh ..
fi


rm -rf target/