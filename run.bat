@echo off
title Medical Appointment System
echo ========================================================
echo       Medical Appointment System - Launcher (JDK 25)
echo ========================================================
echo.

set JDK_PATH=%~dp0dao\oracleJdk-25\bin

if exist "%JDK_PATH%\javac.exe" (
    set JAVAC_CMD="%JDK_PATH%\javac.exe"
    set JAVA_CMD="%JDK_PATH%\java.exe"
    echo Using bundled JDK 25...
) else (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
    echo Using system Java...
)

echo.
echo [1/2] Compiling Java source files...
%JAVAC_CMD% -cp "lib\mysql-connector-j-9.2.0.jar" *.java db\*.java gui\*.java service\*.java model\*.java dao\*.java ui\*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [2/2] Launching Medical Appointment System GUI...
echo.
%JAVA_CMD% -cp "lib\mysql-connector-j-9.2.0.jar;." Main

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Application terminated. (Ensure MySQL service is running)
    pause
)
