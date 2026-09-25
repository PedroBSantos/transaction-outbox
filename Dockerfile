FROM maven:3.9.16-amazoncorretto-25 AS build
WORKDIR /app/
COPY . .
RUN sed -i 's/^spring\.profiles\.active=.*/spring.profiles.active=docker/' src/main/resources/application.properties
RUN mvn dependency:list install -DskipTests && mvn clean package -DskipTests

FROM amazoncorretto:25-alpine AS final
WORKDIR /app/
COPY --from=build /app/target/transaction-outbox-0.0.1-SNAPSHOT.jar webapi.jar
ENTRYPOINT exec java -jar ./webapi.jar
