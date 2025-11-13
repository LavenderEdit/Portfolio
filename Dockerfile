# --- Etapa 1: BUILD ---
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Optimización de Capa de Caché de Docker:
COPY pom.xml .

# 2. Descargamos todas las dependencias.
RUN mvn dependency:go-offline

# 3. Copiamos el código fuente de la aplicación.
COPY src ./src

# 4. Compilamos la aplicación y creamos el .jar.
RUN mvn package -DskipTests

# --- Etapa 2: PRODUCTION ---
# Empezamos desde una imagen JRE mínima. Esta imagen no contiene Maven
# ni el JDK, reduciendo drásticamente el tamaño y la superficie de ataque.
FROM eclipse-temurin:21-jre-jammy AS production
WORKDIR /app

# Definimos el puerto de la aplicación como un argumento.
ARG APP_PORT=8080
ENV PORT=${APP_PORT}

# Expone el puerto 8080 del contenedor a la red interna de Docker.
EXPOSE ${APP_PORT}

# 1. Mejor Práctica de Seguridad: Crear un usuario no privilegiado.
RUN addgroup --system spring && adduser --system --ingroup spring springuser
USER springuser

# 2. Copiamos el artefacto.jar compilado desde la etapa 'build'.
COPY --from=build /app/target/*.jar app.jar

# 3. ENTRYPOINT (Forma Exec):
# Este es el comando que se ejecutará cuando el contenedor inicie.
# Usar esta sintaxis permite que los argumentos pasados (desde docker-compose)
# se añadan correctamente, como por ejemplo --spring.profiles.active=prod.
ENTRYPOINT ["java", "-jar", "app.jar"]