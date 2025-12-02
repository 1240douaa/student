# Step 1: Build the app using Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Create a lightweight runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Dserver.port=8081", "app.jar"]