# Use an official Node.js runtime as a parent image
FROM node:18-alpine


# Set the working directory in the container
WORKDIR /app

# Copy the package.json and package-lock.json files
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install -g @angular/cli && npm install

# Copy the rest of the application code
COPY . .

# Expose the port the app runs on
EXPOSE 4200

# Start the application
# CMD ["npm", "start"]
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll=2000"]
