# Build: használjunk globálisan telepített Maven-t
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml . 
COPY src ./src 
RUN mvn clean package -DskipTests
# COPY . .
# RUN mvn package -DskipTests

# Futás: csak a lefordított JAR kell
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
