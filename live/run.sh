
export JAVA_HOME=/opt/jdk-23
export PATH=$JAVA_HOME/bin:$PATH

export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=$MAVEN_HOME/bin:$PATH


mvn clean package -DskipTests
# mvn package -DskipTests

# Enable debug logging for Docker Compose
# export COMPOSE_LOG_LEVEL=DEBUG

# Build and start the Docker containers with the specified environment
# sudo docker-compose up --build
docker compose up --build