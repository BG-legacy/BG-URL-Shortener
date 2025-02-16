# Stage 1: Build the frontend
FROM node:20-alpine as frontend-build
WORKDIR /app/frontend
COPY url-shortener-frontend/package*.json ./
RUN npm install
COPY url-shortener-frontend/ .
RUN npm run build --configuration=production

# Stage 2: Build the backend
FROM maven:3.9.6-amazoncorretto-17 AS backend-build
WORKDIR /app/backend
COPY url-shortener-backend/pom.xml .
COPY url-shortener-backend/src ./src
RUN mvn clean package -DskipTests

# Stage 3: Final image with Nginx and Java
FROM nginx:alpine
# Install Java
RUN apk add --no-cache openjdk17-jre

# Copy frontend build
COPY --from=frontend-build /app/frontend/dist/url-shortener-frontend/browser /usr/share/nginx/html

# Copy nginx configuration
COPY url-shortener-frontend/nginx.conf /etc/nginx/conf.d/default.conf

# Copy backend jar
WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar app.jar

# Expose ports
EXPOSE 80 8080

# Start both Nginx and Java application
COPY start.sh /start.sh
RUN chmod +x /start.sh
CMD ["/start.sh"]