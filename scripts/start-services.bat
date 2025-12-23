@echo off
echo Starting Trading System Microservices...

REM 设置Java环境变量
set JAVA_OPTS=-Xmx1g -Xms512m

echo Starting required infrastructure services...
docker-compose up -d

timeout /t 15 /nobreak

echo Starting Gateway Service...
start "Gateway Service" cmd /c "cd gateway && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Account Service...
start "Account Service" cmd /c "cd account-service && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Order Service...
start "Order Service" cmd /c "cd order-service && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Matching Engine...
start "Matching Engine" cmd /c "cd matching-engine && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Market Data Service...
start "Market Data Service" cmd /c "cd market-data-service && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Risk Service...
start "Risk Service" cmd /c "cd risk-service && mvn spring-boot:run && pause"

timeout /t 10 /nobreak

echo Starting Notification Service...
start "Notification Service" cmd /c "cd notification-service && mvn spring-boot:run && pause"

echo All services started successfully!
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
echo Press any key to exit...
pause > nul