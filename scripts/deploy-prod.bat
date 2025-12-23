@echo off
echo ========================================
echo    Deploying Trading System to Production
echo ========================================
echo.

REM 检查Docker是否安装并运行
docker version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Docker is not installed or not running
    pause
    exit /b 1
)

echo Stopping existing containers...
docker-compose -f docker-compose-prod.yml down --remove-orphans

echo.
echo Building application JARs...
mvn clean package -DskipTests -q

if %ERRORLEVEL% neq 0 (
    echo Maven build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Docker images...
call build-images.bat

echo.
echo Starting services with Docker Compose...
docker-compose -f docker-compose-prod.yml up -d

if %ERRORLEVEL% neq 0 (
    echo Docker Compose failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Waiting for services to start...
timeout /t 30 /nobreak

echo.
echo Checking running containers...
docker ps

echo.
echo ========================================
echo    Deployment completed successfully!
echo ========================================
echo.
echo Services are running:
echo - Gateway: http://localhost:8080
echo - Account Service: http://localhost:8083
echo - Order Service: http://localhost:8081
echo - Matching Engine: http://localhost:8082
echo - Market Data Service: http://localhost:8084
echo - Risk Service: http://localhost:8085
echo - Notification Service: http://localhost:8086
echo.
echo To view logs: docker-compose -f docker-compose-prod.yml logs -f
echo To stop services: docker-compose -f docker-compose-prod.yml down
echo.
pause