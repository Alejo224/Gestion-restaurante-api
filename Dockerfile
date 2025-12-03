# Etapa 1: Construcción
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copiar solo archivos necesarios para descargar dependencias
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw

# Descargar dependencias (cache)
RUN ./mvnw dependency:go-offline -DskipTests

# Copiar resto del código fuente
COPY src ./src

# Construir el JAR
RUN ./mvnw -DskipTests package

# Etapa 2: Imagen final liviana
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# Comando de inicio
CMD ["java", "-jar", "app.jar"]
