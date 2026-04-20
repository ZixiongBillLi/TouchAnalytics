@echo off
setlocal

echo Setting up TouchalyticsServer...

rem Check for firebase service key
if not exist "firebase_service_key.json" (
    echo ======================================================================
    echo Error: firebase_service_key.json not found.
    echo Please place your Firebase service account key in the project root
    echo directory and ensure it is named 'firebase_service_key.json'.
    echo ======================================================================
    exit /b 1
)

rem Check if python is installed
where python >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Python could not be found. Please install Python 3.
    exit /b 1
)

rem Directory for the virtual environment
set VENV_DIR=.venv

rem Check if the virtual environment exists, create it if it doesn't
if not exist "%VENV_DIR%\Scripts\activate.bat" (
    echo Creating virtual environment in %VENV_DIR%...
    python -m venv "%VENV_DIR%"
) else (
    echo Virtual environment already exists.
)

rem Activate the virtual environment
echo Activating virtual environment...
call "%VENV_DIR%\Scripts\activate.bat"

rem Update pip
echo Upgrading pip...
python -m pip install --upgrade pip

rem Install requirements
echo Installing dependencies...
pip install -r requirements.txt

rem Run the app
echo Starting the application...
python app.py

endlocal
