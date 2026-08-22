@echo off
setlocal

cd /d "%~dp0..\..\.."

echo Current allure directory:
cd

set "PATH=%PATH%;%CD%\src\main\java\internalPlugins\allure-2.35.1\bin"

allure generate --single-file allure-results --clean
pause
endlocal