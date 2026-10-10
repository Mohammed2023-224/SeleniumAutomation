@echo off
setlocal

REM Use the passed-in start directory, or fall back to this script's folder
set "DIR=%~1"
if not defined DIR set "DIR=%~dp0"

:findRoot
if exist "%DIR%\pom.xml"        goto :found
if exist "%DIR%\build.gradle"   goto :found
if exist "%DIR%\settings.gradle" goto :found
if exist "%DIR%\.git"           goto :found

for %%I in ("%DIR%\..") do set "PARENT=%%~fI"

REM Stop if we hit the drive root
if /i "%PARENT%"=="%DIR%" (
    endlocal
    exit /b 1
)

set "DIR=%PARENT%"
goto :findRoot

:found
REM Return the project root to the calling script
endlocal & set "PROJECT_ROOT=%DIR%"

exit /b 0
