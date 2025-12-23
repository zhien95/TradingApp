@echo off
echo ========================================
echo    Trading System - Development Mode
echo ========================================
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Java is not installed or not in PATH
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Maven is not installed or not in PATH
    pause
    exit /b 1
)

echo Starting infrastructure services...
docker-compose up -d --quiet-pull

echo Waiting for infrastructure to start...
timeout /t 15 /nobreak

echo.
echo Starting Gateway Service...
start "Gateway Service" cmd /c "cd gateway && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Gateway service stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Account Service...
start "Account Service" cmd /c "cd account-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Account service stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Order Service...
start "Order Service" cmd /c "cd order-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Order service stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Matching Engine...
start "Matching Engine" cmd /c "cd matching-engine && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Matching Engine stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Market Data Service...
start "Market Data Service" cmd /c "cd market-data-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Market Data service stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Risk Service...
start "Risk Service" cmd /c "cd risk-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Risk service stopped. Press any key to exit... && pause > nul"

timeout /t 8 /nobreak

echo.
echo Starting Notification Service...
start "Notification Service" cmd /c "cd notification-service && mvn spring-boot:run -Dspring-boot.run.profiles=dev && echo Notification service stopped. Press any key to exit... && pause > nul"

echo.
echo ========================================
echo    All services started successfully!
echo ========================================
echo.
echo Services are running on:
echo - Gateway: http://localhost:8080
echo - Account Service: http://localhost:8083
echo - Order Service: http://localhost:8081
echo - Matching Engine: http://localhost:8082
echo - Market Data Service: http://localhost:8084
echo - Risk Service: http://localhost:8085
echo - Notification Service: http://localhost:8086
echo.
echo To stop services, close each service window or run stop-services.bat
echo.
pause