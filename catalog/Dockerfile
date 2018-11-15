FROM gradle:jdk10 as builder
COPY --chown=gradle:gradle . /home/gradle/catalog
WORKDIR /home/gradle/catalog
RUN gradle build -x test

FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/catalog
COPY --from=builder /home/gradle/catalog/build/libs/catalog-service-1.0-SNAPSHOT.jar /usr/build/catalog/
WORKDIR /usr/build/catalog
EXPOSE 8081
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "catalog-service-1.0-SNAPSHOT.jar"]