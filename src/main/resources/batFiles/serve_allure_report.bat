@echo off
setlocal
cd ../../../../
set "PATH=%PATH%;src/main/java/internalPlugins/allure-2.35.1/bin;
allure serve

pause
endlocal