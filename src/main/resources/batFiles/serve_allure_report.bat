@echo off
setlocal

cd /d "%~dp0..\..\.."

echo Current directory:
cd

set "PATH=%PATH%;%CD%\src\main\java\internalPlugins\allure-2.35.1\bin"

allure serve "%CD%\allure-results"

pause
endlocal