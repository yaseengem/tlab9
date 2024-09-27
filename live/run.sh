# run this file using "sudo ./run.sh"

export JAVA_HOME=/opt/jdk-23
export PATH=$JAVA_HOME/bin:$PATH

export MAVEN_HOME=/opt/apache-maven-3.9.9
export PATH=$MAVEN_HOME/bin:$PATH


# rm -rf target/

mvn clean package -DskipTests
# mvn package -DskipTests

# sudo docker-compose up --build
docker compose up --build


