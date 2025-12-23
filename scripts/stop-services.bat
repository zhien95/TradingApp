@echo off
echo ========================================
echo    Stopping Trading System Services
echo ========================================
echo.

echo Stopping Docker containers...
docker-compose down

echo.
echo Killing Java processes...
taskkill /f /im "java.exe" >nul 2>&1

echo.
echo Stopping services with specific names...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8081') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8082') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8083') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8084') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8085') do taskkill /f /pid %%a >nul 2>&1
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8086') do taskkill /f /pid %%a >nul 2>&1

echo.
echo All services stopped.
echo ========================================
pause