@echo off

if not defined PROJECT_ROOT (
    call "%~dp0RootFinder.bat" "%~dp0"
    if errorlevel 1 (
        echo ERROR: Could not locate project root.
        exit /b 1
    )
)

cd %PROJECT_ROOT%
appium
pause