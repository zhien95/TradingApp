@echo off
echo Building Docker images for Trading System Microservices...

REM 设置变量
set DOCKER_REGISTRY=localhost:5000
set PROJECT_NAME=trading-system

echo Building Common module...
cd common
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-common:latest .
) else (
  echo No Dockerfile found for common module
  echo Creating Dockerfile for common module...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8080>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
cd ..

echo Building Gateway Service...
cd gateway
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-gateway:latest .
) else (
  echo Creating Dockerfile for gateway...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8080>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-gateway:latest .
cd ..

echo Building Order Service...
cd order-service
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-order-service:latest .
) else (
  echo Creating Dockerfile for order-service...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8081>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-order-service:latest .
cd ..

echo Building Matching Engine...
cd matching-engine
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-matching-engine:latest .
) else (
  echo Creating Dockerfile for matching-engine...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8082>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-matching-engine:latest .
cd ..

echo Building Account Service...
cd account-service
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-account-service:latest .
) else (
  echo Creating Dockerfile for account-service...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8083>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-account-service:latest .
cd ..

echo Building Market Data Service...
cd market-data-service
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-market-data-service:latest .
) else (
  echo Creating Dockerfile for market-data-service...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8084>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-market-data-service:latest .
cd ..

echo Building Risk Service...
cd risk-service
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-risk-service:latest .
) else (
  echo Creating Dockerfile for risk-service...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8085>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-risk-service:latest .
cd ..

echo Building Notification Service...
cd notification-service
if exist Dockerfile (
  docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-notification-service:latest .
) else (
  echo Creating Dockerfile for notification-service...
  echo FROM openjdk:21-jdk-slim> Dockerfile
  echo COPY target/*.jar app.jar>> Dockerfile
  echo EXPOSE 8086>> Dockerfile
  echo ENTRYPOINT \[\"java\", \"-jar\", \"/app.jar\"\]>> Dockerfile
)
docker build -t %DOCKER_REGISTRY%/%PROJECT_NAME%-notification-service:latest .
cd ..

echo All Docker images built successfully!
echo.
echo Built images:
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-gateway:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-order-service:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-matching-engine:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-account-service:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-market-data-service:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-risk-service:latest
echo - %DOCKER_REGISTRY%/%PROJECT_NAME%-notification-service:latest
echo.
echo To push images to registry, run: docker push [image-name]
pause