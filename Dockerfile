# Stage 1: Build environment
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copiar solo los archivos necesarios para descargar dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -DskipTests

# Copiar el código fuente (esto invalida la cache si el código cambia)
COPY src/ src/

# Compilar y generar el .jar
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime environment
FROM eclipse-temurin:17-jdk AS final

WORKDIR /app
EXPOSE 8080

# Copiar el jar con nombre específico (CORREGIDO)
COPY --from=builder /app/target/*.jar app.jar

# Comando para ejecutar la app (CORREGIDO)
ENTRYPOINT ["java", "-jar", "app.jar"]


