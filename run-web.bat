@echo off
title EJECUTAR GESTOR EMPRESARIAL - WEB
color 0A

echo ========================================
echo   INICIANDO GESTOR EMPRESARIAL
echo         VERSIÓN WEB
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Configurar ruta de Tomcat
set TOMCAT_HOME=C:\apache-tomcat-9.0.89

if not exist "%TOMCAT_HOME%" (
    echo ❌ No se encontró Tomcat
    echo.
    echo Para desplegar la aplicación web necesitas:
    echo 1. Instalar Apache Tomcat 9
    echo 2. Ejecutar build-web.bat primero
    echo 3. Ejecutar deploy-web.bat
    echo.
    pause
    exit /b 1
)

:: Verificar despliegue
if not exist "%TOMCAT_HOME%\webapps\gestor-empresarial" (
    if not exist "%TOMCAT_HOME%\webapps\gestor-empresarial.war" (
        echo ⚠️  La aplicación no está desplegada
        echo.
        echo Ejecute primero: build-web.bat y luego deploy-web.bat
        echo.
        pause
        exit /b 1
    )
)

:: Verificar si Tomcat está corriendo
echo [1/2] Verificando Tomcat...
tasklist /FI "IMAGENAME eq java.exe" 2>nul | find /I "java.exe" >nul

if %errorlevel% equ 0 (
    echo       ✓ Tomcat ya está corriendo
) else (
    echo       Iniciando Tomcat...
    call "%TOMCAT_HOME%\bin\startup.bat"
    timeout /t 4 /nobreak >nul
)

:: Abrir navegador
echo [2/2] Abriendo navegador...
start http://localhost:8080/gestor-empresarial

echo.
echo ========================================
echo   ✅ APLICACIÓN INICIADA
echo ========================================
echo.
echo   URL: http://localhost:8080/gestor-empresarial
echo.
echo   Credenciales:
echo   Usuario: admin@gestorempresarial.com
echo   Contraseña: admin123
echo.
pause