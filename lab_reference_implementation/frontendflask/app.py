import os
from flask import Flask, render_template, request, redirect, url_for, flash
import requests
import logging

app = Flask(__name__)
app.secret_key = 'your_secret_key'  # Needed for flash messages

logging.basicConfig(level=logging.DEBUG)
logging.getLogger('requests').setLevel(logging.DEBUG)
logging.getLogger('urllib3').setLevel(logging.DEBUG)

# Get API URL from environment variable or use a default value
API_URL = os.getenv("API_URL", "http://localhost:8080/api/shopping") # 12-factor style external config

# Function to log requests with payload
def log_request(request,response):
    # Log the request URL and method
    logging.debug(f"Request: {request.method} {request.url}")
    
    # Log the request payload if present
    if request.method in ['POST', 'PUT', 'PATCH']:
        if request.body:
            logging.debug(f"Payload: {request.body.decode('utf-8')}")
        elif request.json:
            logging.debug(f"Payload (JSON): {request.json}")

        # Log the response status code and body
    logging.debug(f"Response Status: {response.status_code}")
    if response.text:
        logging.debug(f"Response Body: {response.text}")

@app.route('/')
def index():
    # Fetch all items from the REST API
    try:
        response = requests.get(API_URL)
        log_request(response.request, response)
        items = response.json() if response.status_code == 200 else []
        
    except requests.exceptions.RequestException:
        items = []
        flash("Error: Unable to fetch items.", "error")
    return render_template('index.html', items=items)

@app.route('/add', methods=['POST'])
def add_item():
    name = request.form.get('name')
    amount = request.form.get('amount')
    if name and amount:
        try:
            response = requests.post(API_URL, json={"name": name, "amount": int(amount)})
            log_request(response.request, response)
            if response.status_code == 201:
                flash("Item added successfully!", "success")
            else:
                flash("Error adding item.", "error")
        except requests.exceptions.RequestException:
            flash("Error: Unable to add item.", "error")
    return redirect(url_for('index'))

@app.route('/update', methods=['POST'])
def update_item():
    name = request.form.get('update_name')
    amount = request.form.get('update_amount')
    if name and amount:
        try:
            response = requests.put(f"{API_URL}/{name}", json={"amount": int(amount)})
            log_request(response.request, response)
            if response.status_code == 200:
                flash("Item updated successfully!", "success")
            else:
                flash("Error updating item.", "error")
        except requests.exceptions.RequestException:
            flash("Error: Unable to update item.", "error")
    return redirect(url_for('index'))

@app.route('/delete', methods=['POST'])
def delete_item():
    name = request.form.get('delete_name')
    if name:
        try:
            response = requests.delete(f"{API_URL}/{name}")
            log_request(response.request, response)
            if response.status_code == 204:
                flash("Item deleted successfully!", "success")
            else:
                flash("Error deleting item.", "error")
        except requests.exceptions.RequestException:
            flash("Error: Unable to delete item.", "error")
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)
