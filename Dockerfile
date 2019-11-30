FROM openjdk:latest
COPY ./target/* ./
CMD ["java", "-jar", "PassingCar_backend-1.0-SNAPSHOT.jar", "-cp", "dependency"]
