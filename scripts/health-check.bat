@echo off
echo ========================================
echo    Trading System Health Check
echo ========================================
echo.

echo Checking if services are running...

echo.
echo Checking Gateway Service (port 8080)...
netstat -an | findstr :8080 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Gateway Service is running
) else (
    echo [ERROR] Gateway Service is NOT running
)

echo.
echo Checking Order Service (port 8081)...
netstat -an | findstr :8081 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Order Service is running
) else (
    echo [ERROR] Order Service is NOT running
)

echo.
echo Checking Matching Engine (port 8082)...
netstat -an | findstr :8082 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Matching Engine is running
) else (
    echo [ERROR] Matching Engine is NOT running
)

echo.
echo Checking Account Service (port 8083)...
netstat -an | findstr :8083 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Account Service is running
) else (
    echo [ERROR] Account Service is NOT running
)

echo.
echo Checking Market Data Service (port 8084)...
netstat -an | findstr :8084 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Market Data Service is running
) else (
    echo [ERROR] Market Data Service is NOT running
)

echo.
echo Checking Risk Service (port 8085)...
netstat -an | findstr :8085 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Risk Service is running
) else (
    echo [ERROR] Risk Service is NOT running
)

echo.
echo Checking Notification Service (port 8086)...
netstat -an | findstr :8086 >nul
if %ERRORLEVEL% equ 0 (
    echo [OK] Notification Service is running
) else (
    echo [ERROR] Notification Service is NOT running
)

echo.
echo Checking Docker containers...
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" >nul 2>&1
if %ERRORLEVEL% equ 0 (
    echo.
    echo Docker containers status:
    docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
) else (
    echo [WARNING] Docker is not running or not installed
)

echo.
echo ========================================
echo    Health check completed
echo ========================================
pause