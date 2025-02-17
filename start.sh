#!/bin/sh
# Start Java application in the background
java -jar /app/app.jar &
# Start Nginx in the foreground
nginx -g 'daemon off;' 