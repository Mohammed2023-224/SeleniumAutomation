@echo off
if not defined PROJECT_ROOT (
    call "%~dp0RootFinder.bat" "%~dp0"
    if errorlevel 1 (
        echo ERROR: Could not locate project root.
        exit /b 1
    )
)

cd %PROJECT_ROOT%
emulator -avd pixel_9a -no-window -no-boot-anim -port 5556
pause