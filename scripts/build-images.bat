@echo off
echo ========================================
echo    Building Trading System Docker Images
echo ========================================
echo.

REM 检查Docker是否安装并运行
docker version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo Error: Docker is not installed or not running
    pause
    exit /b 1
)

echo Building application JARs...
mvn clean package -DskipTests -q

if %ERRORLEVEL% neq 0 (
    echo Maven build failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Gateway Service Docker image...
docker build -t trading-system-gateway:latest ./gateway
if %ERRORLEVEL% neq 0 (
    echo Error building Gateway Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Order Service Docker image...
docker build -t trading-system-order-service:latest ./order-service
if %ERRORLEVEL% neq 0 (
    echo Error building Order Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Matching Engine Docker image...
docker build -t trading-system-matching-engine:latest ./matching-engine
if %ERRORLEVEL% neq 0 (
    echo Error building Matching Engine image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Account Service Docker image...
docker build -t trading-system-account-service:latest ./account-service
if %ERRORLEVEL% neq 0 (
    echo Error building Account Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Market Data Service Docker image...
docker build -t trading-system-market-data-service:latest ./market-data-service
if %ERRORLEVEL% neq 0 (
    echo Error building Market Data Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Risk Service Docker image...
docker build -t trading-system-risk-service:latest ./risk-service
if %ERRORLEVEL% neq 0 (
    echo Error building Risk Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo Building Notification Service Docker image...
docker build -t trading-system-notification-service:latest ./notification-service
if %ERRORLEVEL% neq 0 (
    echo Error building Notification Service image
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ========================================
echo    All Docker images built successfully!
echo ========================================
echo.
echo Built images:
echo - trading-system-gateway:latest
echo - trading-system-order-service:latest
echo - trading-system-matching-engine:latest
echo - trading-system-account-service:latest
echo - trading-system-market-data-service:latest
echo - trading-system-risk-service:latest
echo - trading-system-notification-service:latest
echo.
pause