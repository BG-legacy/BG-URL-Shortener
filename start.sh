#!/bin/sh
# Start the Java backend application
java -jar /app/app.jar &

# Start nginx
nginx -g 'daemon off;' 