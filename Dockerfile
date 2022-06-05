FROM openjdk:17.0.2-oraclelinux8

EXPOSE 8080
COPY target/cloud_storage_backend-0.0.1-SNAPSHOT.jar cloudStorage.jar
CMD ["java", "-jar", "/cloudStorage.jar"]