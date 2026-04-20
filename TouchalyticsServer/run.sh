#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

echo "Setting up TouchalyticsServer..."

# Check for firebase service key
if [ ! -f "firebase_service_key.json" ]; then
    echo "======================================================================"
    echo "Error: firebase_service_key.json not found."
    echo "Please place your Firebase service account key in the project root"
    echo "directory and ensure it is named 'firebase_service_key.json'."
    echo "======================================================================"
    exit 1
fi

# Check if python3 is installed
if ! command -v python3 &> /dev/null
then
    echo "Error: Python 3 could not be found. Please install Python 3."
    exit 1
fi

# Directory for the virtual environment
VENV_DIR=".venv"

# Check if the virtual environment exists, create it if it doesn't
if [ ! -d "$VENV_DIR" ]; then
    echo "Creating virtual environment in $VENV_DIR..."
    python3 -m venv "$VENV_DIR"
else
    echo "Virtual environment already exists."
fi

# Activate the virtual environment
echo "Activating virtual environment..."
source "$VENV_DIR/bin/activate"

# Update pip
echo "Upgrading pip..."
pip install --upgrade pip

# Install requirements
echo "Installing dependencies..."
pip install -r requirements.txt

# Run the app
echo "Starting the application..."
python3 app.py
