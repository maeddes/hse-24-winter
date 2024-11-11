import os
from flask import Flask, render_template, request, redirect, url_for, flash
import requests

app = Flask(__name__)
app.secret_key = 'your_secret_key'  # Needed for flash messages

# Get API URL from environment variable or use a default value
API_URL = os.getenv("API_URL", "http://localhost:8080/api/shopping") # 12-factor style external config

@app.route('/')
def index():
    # Fetch all items from the REST API
    try:
        response = requests.get(API_URL)
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
            if response.status_code == 201:
                flash("Item added successfully!", "success")
            elif response.status_code == 200:
                flash("Item updated successfully!", "success")
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
            response = requests.put(f"{API_URL}/{name}", json={"name": name, "amount": int(amount)})
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
            if response.status_code == 204:
                flash("Item deleted successfully!", "success")
            else:
                flash("Error deleting item.", "error")
        except requests.exceptions.RequestException:
            flash("Error: Unable to delete item.", "error")
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)
