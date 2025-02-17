#!/bin/sh
# Start Java application in the background
java -jar /app/app.jar &

# Wait for Spring Boot to start
echo "Waiting for Spring Boot to start..."
until curl -s http://localhost:8080/actuator/health > /dev/null; do
    echo "Waiting for backend to start..."
    sleep 5
done
echo "Spring Boot started successfully"

# Configure Nginx to proxy requests to the backend
cat > /etc/nginx/conf.d/default.conf << EOF
server {
    listen 80;
    server_name localhost;

    # Frontend static files
    location / {
        root /usr/share/nginx/html;
        try_files \$uri \$uri/ /index.html;
    }

    # Backend API proxy
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    # Actuator endpoints proxy
    location /actuator/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

# Start Nginx in the foreground
exec nginx -g 'daemon off;' 