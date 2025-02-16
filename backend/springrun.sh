#!/bin/bash

# Run this file using "sudo ./springrun.sh"
# This script is used to build and run the application in development mode without Docker containers.

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

# Clean and package the application
mvn clean package -DskipTests

# Run the Spring Boot application in development mode
mvn spring-boot:run -Dspring-boot.run.profiles=$ENV