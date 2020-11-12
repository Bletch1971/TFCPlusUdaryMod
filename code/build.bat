@echo off

set WORKDIR=%CD%

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.

cd /d "%DIRNAME%"

del build\classes /F /S /Q
del build\sources /F /S /Q
del build\resources /F /S /Q

call gradlew.bat build

cd /d "%WORKDIR%"

pause