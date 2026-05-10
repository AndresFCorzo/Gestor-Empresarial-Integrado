@echo off
title EJECUTAR CONSOLA (DEFINITIVO)
color 0E

echo ========================================
echo   EJECUTANDO GESTOR EMPRESARIAL
echo        VERSION CONSOLA
echo ========================================
echo.

cd /d "C:\Users\ASUS\Documents\SENA\Proyectos\GESTOR_EMPRESARIAL_INTEGRADO"

set LIB_DIR=src\main\webapp\WEB-INF\lib

:: Buscar la clase en cualquier ubicación
if exist build-console\com\gestorempresarial\console\vista\MenuPrincipal.class (
    set CLASSPATH=build-console
) else if exist build-console\main\java\com\gestorempresarial\console\vista\MenuPrincipal.class (
    set CLASSPATH=build-console\main\java
) else (
    echo [ERROR] No se encuentra MenuPrincipal.class
    echo Ejecute primero: build-console-definitivo.bat
    pause
    exit /b 1
)

echo Classpath: %CLASSPATH%
echo.

:: Ejecutar
java -cp "%LIB_DIR%\mysql-connector-j-8.0.33.jar;%LIB_DIR%\jstl-1.2.jar;%CLASSPATH%" com.gestorempresarial.console.vista.MenuPrincipal

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   ERROR - REVISE LA CONEXION A BD
    echo ========================================
)

pause