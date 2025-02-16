# Build Frontend
FROM node:18 AS frontend-build
WORKDIR /app/frontend
COPY url-shortener-frontend/package*.json ./
RUN npm install
COPY url-shortener-frontend/ .
RUN npm run build

# Build Backend
FROM maven:3.9.6-amazoncorretto-17 AS backend-build
WORKDIR /app/backend
COPY url-shortener-backend/pom.xml .
COPY url-shortener-backend/src ./src
RUN mvn clean package -DskipTests

# Final image
FROM nginx:alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=frontend-build /app/frontend/dist/url-shortener-frontend/browser /usr/share/nginx/html
COPY --from=backend-build /app/backend/target/*.jar /app/app.jar

# Install OpenJDK
RUN apk add --no-cache openjdk17-jre

# Copy start script
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 80
CMD ["/start.sh"] 