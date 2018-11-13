FROM openjdk:10-jre-slim
RUN mkdir -p /usr/build/product
COPY ./build/libs/product-service-1.0-SNAPSHOT.jar /usr/build/product
WORKDIR /usr/build/product
EXPOSE 8083
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "product-service-1.0-SNAPSHOT.jar"]