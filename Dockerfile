FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar archivos de Maven Wrapper y pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -DskipTests

# El código fuente se montará por volumen, así que no se copia aquí

EXPOSE 8080

# No definas ENTRYPOINT ni CMD aquí, se usará el comando del compose
