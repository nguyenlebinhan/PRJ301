@echo off
REM Script để build và deploy lên AWS Elastic Beanstalk (Windows)

echo === Building Thesis Management Application ===

REM Build WAR file (sử dụng Ant)
echo Building WAR file...
call ant clean dist

if %ERRORLEVEL% NEQ 0 (
    echo Build failed! Please check errors above.
    exit /b 1
)

echo Build successful! WAR file created at: dist\ThesisManagement.war

REM Check if EB CLI is installed
where eb >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo.
    echo === Deploying to Elastic Beanstalk ===
    set /p deploy="Do you want to deploy now? (y/n): "
    if /i "%deploy%"=="y" (
        eb deploy
    ) else (
        echo Deployment cancelled. You can deploy later using: eb deploy
    )
) else (
    echo.
    echo EB CLI not found. Please deploy manually:
    echo 1. Go to AWS Console -^> Elastic Beanstalk
    echo 2. Upload dist\ThesisManagement.war
    echo.
    echo Or install EB CLI: pip install awsebcli
)

pause

