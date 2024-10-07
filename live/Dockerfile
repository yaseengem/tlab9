FROM openjdk:17-jdk-alpine


# Create upload directory
RUN mkdir -p /files

# Copy the keystore file
COPY /cert/keystore.p12 keystore.p12

COPY target/live-0.0.1-SNAPSHOT.jar app-1.0.0.jar

ENTRYPOINT [ "java", "-jar", "app-1.0.0.jar" ]
# ENTRYPOINT [ "java", "-jar", "app-1.0.0.jar" , "--debug"]