#Use Tomcat 8.0.xx
http://tomcat.apache.org/download-80.cgi

#To prepare war on tomcat 8
mvn clean package

#To run from Intellij with embedded TOMCAT
mvn springboot:run

#Prepare schema and initial records
mvn flyway:clean flyway:migrate

#To override the default application.properties by the external properties file run by tomcat
set JAVA_OPTS=-Dspring.config.location=file:/C:/config/application.properties