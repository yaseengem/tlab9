AI Code assistant has to use the infomratoin in this file whenever providing suggestions on the code.


Project Description:

This project is to provide a backend for a online courses. 
The hierarchy is as below
- Courses are at the top level
- Each course has multiple subject which are organised in sequence
- Each subject has multiple modules which are organised in sequence
- Each module has multiple units which are organised in sequence
- Each unit has multiple topics which are organised in sequence
- Each topic has multiple sub-topics which are organised in sequence

_______________________________
When you take new machine do these to make the system ready


cd \
sudo wget https://download.java.net/java/GA/jdk23/3c5b90190c68498b986a97f276efd28a/37/GPL/openjdk-23_linux-x64_bin.tar.gz
sudo tar -xvf  openjdk-23_linux-x64_bin.tar.gz 
sudo mv jdk-23 /opt/


JAVA_HOME='/opt/jdk-23'
PATH="$JAVA_HOME/bin:$PATH"
export PATH
java --version

cd \
sudo wget https://dlcdn.apache.org/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.tar.gz
sudo tar -xvf  apache-maven-3.9.9-bin.tar.gz 
sudo mv apache-maven-3.9.9 /opt/



M2_HOME='/opt/apache-maven-3.9.9'
PATH="$M2_HOME/bin:$PATH"
export PATH
sudo mvn --version


Install docker from this
https://docs.docker.com/engine/install/ubuntu/
