# Serve stage
FROM nginx:1.25.1

# Copy the built React app from the build stage to the nginx container
COPY /build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
