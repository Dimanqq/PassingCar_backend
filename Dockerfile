FROM openjdk:latest
COPY ./target/PassingCar_backend-1.0-SNAPSHOT.jar ./app.jar
ENV POSTGRES_IP 172.17.0.1
ENV POSTGRES_PORT 5432
CMD ["java", "-jar", "app.jar"]
