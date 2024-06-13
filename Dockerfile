# Stage 1: Maven build
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /home/app
COPY . .
RUN mvn clean package

# Stage 2: Run
FROM openjdk:17-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]