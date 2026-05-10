@echo off
title DESPLEGAR EN TOMCAT
color 0E

echo ========================================
echo   DESPLEGANDO GESTOR EMPRESARIAL
echo         VERSIÓN WEB
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Configurar ruta de Tomcat (CAMBIAR SEGÚN INSTALACIÓN)
set TOMCAT_HOME=C:\apache-tomcat-9.0.89

:: Si no existe, preguntar al usuario
if not exist "%TOMCAT_HOME%" (
    echo ⚠️  No se encontró Tomcat en: %TOMCAT_HOME%
    echo.
    set /p TOMCAT_HOME="Ingrese la ruta de instalación de Tomcat: "
    if not exist "%TOMCAT_HOME%" (
        echo ❌ Ruta inválida
        pause
        exit /b 1
    )
)

:: Verificar WAR
echo [1/4] Verificando archivo WAR...
if not exist gestor-empresarial.war (
    if not exist build-web (
        echo ❌ No se encuentra el WAR o build-web
        echo Ejecute primero: build-web.bat
        pause
        exit /b 1
    )
    echo    ⚠️  No se encontró WAR, desplegando carpeta...
    set DEPLOY_MODE=folder
) else (
    echo       ✓ WAR encontrado
    set DEPLOY_MODE=war
)

:: Detener Tomcat si está corriendo
echo [2/4] Deteniendo Tomcat...
call "%TOMCAT_HOME%\bin\shutdown.bat" 2>nul
timeout /t 3 /nobreak >nul
echo       ✓ Tomcat detenido

:: Limpiar despliegue anterior
echo [3/4] Limpiando despliegue anterior...
if exist "%TOMCAT_HOME%\webapps\gestor-empresarial" (
    rmdir /S /Q "%TOMCAT_HOME%\webapps\gestor-empresarial" 2>nul
)
if exist "%TOMCAT_HOME%\webapps\gestor-empresarial.war" (
    del /Q "%TOMCAT_HOME%\webapps\gestor-empresarial.war" 2>nul
)
echo       ✓ Limpieza completada

:: Desplegar aplicación
echo [4/4] Desplegando aplicación...

if "%DEPLOY_MODE%"=="war" (
    copy gestor-empresarial.war "%TOMCAT_HOME%\webapps\" >nul
    echo       ✓ WAR copiado a webapps/
) else (
    xcopy /E /I /Y build-web "%TOMCAT_HOME%\webapps\gestor-empresarial\" >nul
    echo       ✓ Carpeta copiada a webapps/gestor-empresarial/
)

:: Iniciar Tomcat
echo.
echo Iniciando Tomcat...
call "%TOMCAT_HOME%\bin\startup.bat"

timeout /t 5 /nobreak >nul

echo.
echo ========================================
echo   ✅ DESPLIEGUE COMPLETADO
echo ========================================
echo.
echo   Aplicación disponible en:
echo   http://localhost:8080/gestor-empresarial
echo.
echo   Credenciales de acceso:
echo   Usuario: admin@gestorempresarial.com
echo   Contraseña: admin123
echo.
echo   Para detener Tomcat:
echo   %TOMCAT_HOME%\bin\shutdown.bat
echo.
pause