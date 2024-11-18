# Use the official Python image
FROM python:3-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the requirements.txt file into the container
COPY requirements.txt /app/requirements.txt

# Install the necessary dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the entire application into the container
COPY . /app

# Expose the Flask app on port 5000
EXPOSE 5000

# Set the environment variable for Flask to run in production mode
ENV FLASK_ENV=development

# Run the Flask application
CMD ["flask", "run", "--host", "0.0.0.0", "--port", "5000"]
