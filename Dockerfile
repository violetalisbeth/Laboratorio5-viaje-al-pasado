# ---------------- Etapa 1: build ----------------
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar primero Maven Wrapper y pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Dar permisos al wrapper y descargar dependencias
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar y empaquetar sin volver a ejecutar las pruebas
RUN ./mvnw clean package -DskipTests -B


# ---------------- Etapa 2: runtime ----------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear un usuario sin privilegios
RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

# Copiar el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]