@echo off
echo Deploying Trading System to Docker...

echo Stopping existing containers...
docker-compose -f docker-compose-prod.yml down

echo Building application JARs...
mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Maven build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Building Docker images...
call build-docker-images.bat

echo Starting services with Docker Compose...
docker-compose -f docker-compose-prod.yml up -d

echo Waiting for services to start...
timeout /t 30 /nobreak

echo Checking running containers...
docker ps

echo.
echo Deployment completed!
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