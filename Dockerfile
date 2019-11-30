FROM openjdk:latest
COPY ./target/PassingCar_backend-1.0-SNAPSHOT.jar ./app.jar
CMD ["java", "-jar", "app.jar"]
