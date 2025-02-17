# Build stage
FROM maven:3.9.6-amazoncorretto-17 AS build
WORKDIR /app
COPY pom.xml .
# Cache dependencies
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Add healthcheck with retry
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
# Add environment variable for MongoDB URI
ENV MONGODB_URI=mongodb://urlapp:Berny1232@localhost:27017/urlshortener
# Add Java options for better logging and debugging
ENV JAVA_OPTS="-Dlogging.level.root=INFO -Dlogging.level.org.springframework=INFO"
# Use exec form of CMD with added Java options
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 