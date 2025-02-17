#!/bin/sh
# Start Java application in the background
java -jar /app/app.jar &
# Wait for the application to start
sleep 10
# Start Nginx in the foreground
nginx -g 'daemon off;' 