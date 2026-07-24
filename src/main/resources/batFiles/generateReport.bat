@echo off
setlocal
CD ../../../../
set "PATH=%PATH%;src/main/java/internalPlugins/allure-2.35.1/bin;
allure generate --single-file allure-results --clean
pause
endlocal