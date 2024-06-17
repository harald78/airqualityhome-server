# Stage 1: Maven build
FROM openjdk:17-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]