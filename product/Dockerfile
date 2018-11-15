FROM gradle:jdk10 as builder
COPY --chown=gradle:gradle . /home/gradle/product
WORKDIR /home/gradle/product
RUN gradle build

FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/product
COPY --from=builder /home/gradle/product/build/libs/product-service-1.0-SNAPSHOT.jar /usr/build/product
WORKDIR /usr/build/product
EXPOSE 8083
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "product-service-1.0-SNAPSHOT.jar"]