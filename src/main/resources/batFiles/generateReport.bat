@echo off
setlocal

if not defined PROJECT_ROOT (
    call "%~dp0RootFinder.bat" "%~dp0"
    if errorlevel 1 (
        echo ERROR: Could not locate project root.
        exit /b 1
    )
)

echo Project root: %PROJECT_ROOT%

set "ALLURE_RESULTS=%PROJECT_ROOT%\allure-results"

if not exist "%ALLURE_RESULTS%" (
    echo ERROR: "%ALLURE_RESULTS%" not found. Run your tests first.
    pause
    exit /b 1
)

set "PATH=%PATH%;%CD%\src\main\java\internalPlugins\allure-2.35.1\bin"

allure generate --single-file allure-results --clean
pause
endlocal