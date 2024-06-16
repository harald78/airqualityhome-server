# Stage 1: Maven build
FROM openjdk:23-ea-25-oraclelinux8
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
COPY /home/server/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]