@echo off
echo ========================================
echo    Trading System Management Console
echo ========================================
echo.

:menu
echo Please select an option:
echo.
echo 1. Start Development Environment
echo 2. Deploy to Production
echo 3. Build Docker Images
echo 4. Health Check
echo 5. Stop All Services
echo 6. View Logs
echo 7. Exit
echo.

set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" goto start_dev
if "%choice%"=="2" goto deploy_prod
if "%choice%"=="3" goto build_images
if "%choice%"=="4" goto health_check
if "%choice%"=="5" goto stop_services
if "%choice%"=="6" goto view_logs
if "%choice%"=="7" goto exit_script

echo Invalid choice. Please try again.
timeout /t 2 /nobreak
cls
goto menu

:start_dev
cls
call start-dev.bat
goto menu

:deploy_prod
cls
call deploy-prod.bat
goto menu

:build_images
cls
call build-images.bat
goto menu

:health_check
cls
call health-check.bat
goto menu

:stop_services
cls
call stop-services.bat
goto menu

:view_logs
cls
echo.
echo Select service to view logs:
echo.
echo 1. All Services
echo 2. Gateway
echo 3. Account Service
echo 4. Order Service
echo 5. Matching Engine
echo 6. Market Data Service
echo 7. Risk Service
echo 8. Notification Service
echo 9. Back to Main Menu
echo.

set /p log_choice="Enter your choice (1-9): "

if "%log_choice%"=="1" (
    docker-compose -f docker-compose-prod.yml logs -f
    goto view_logs
)
if "%log_choice%"=="2" (
    docker-compose -f docker-compose-prod.yml logs -f trading-gateway
    goto view_logs
)
if "%log_choice%"=="3" (
    docker-compose -f docker-compose-prod.yml logs -f trading-account-service
    goto view_logs
)
if "%log_choice%"=="4" (
    docker-compose -f docker-compose-prod.yml logs -f trading-order-service
    goto view_logs
)
if "%log_choice%"=="5" (
    docker-compose -f docker-compose-prod.yml logs -f trading-matching-engine
    goto view_logs
)
if "%log_choice%"=="6" (
    docker-compose -f docker-compose-prod.yml logs -f trading-market-data-service
    goto view_logs
)
if "%log_choice%"=="7" (
    docker-compose -f docker-compose-prod.yml logs -f trading-risk-service
    goto view_logs
)
if "%log_choice%"=="8" (
    docker-compose -f docker-compose-prod.yml logs -f trading-notification-service
    goto view_logs
)
if "%log_choice%"=="9" (
    cls
    goto menu
)

echo Invalid choice. Please try again.
timeout /t 2 /nobreak
goto view_logs

:exit_script
echo.
echo Thank you for using Trading System Management Console!
pause