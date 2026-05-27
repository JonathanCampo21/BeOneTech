# Etapa 1: Construir o projeto (Usando o Java 17)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Rodar o projeto (Usando o Java 17 também!)
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/frontline_tech-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
