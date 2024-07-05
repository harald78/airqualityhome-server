# Stage 1: Maven build
FROM eclipse-temurin:17-jdk-alpine
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY app.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]