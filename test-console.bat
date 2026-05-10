@echo off
title EJECUTAR PRUEBAS - CONSOLA
color 0B

echo ========================================
echo   EJECUTANDO PRUEBAS DEL SISTEMA
echo        VERSIÓN CONSOLA
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Verificar compilación
if not exist build-console (
    echo ❌ No se encuentra build-console
    echo Ejecute primero: build-console.bat
    pause
    exit /b 1
)

set LIB_DIR=src\main\webapp\WEB-INF\lib

:: Crear directorio de pruebas
if not exist build-console\test mkdir build-console\test

:: Compilar pruebas
echo [1/2] Compilando pruebas...
set CP="%LIB_DIR%\*;build-console"

:: Buscar archivos de prueba
dir /s /B src\test\java\com\gestorempresarial\test\*.java 2>nul > test_sources.txt

if %errorlevel% neq 0 (
    echo ⚠️  No se encontraron archivos de prueba
    echo.
    echo Para crear pruebas, cree archivos en: src\test\java\
    pause
    exit /b 0
)

javac -cp %CP% -d build-console\test @test_sources.txt 2>nul
del test_sources.txt

if %errorlevel% neq 0 (
    echo ❌ Error al compilar pruebas
    pause
    exit /b 1
)
echo       ✓ Pruebas compiladas

:: Ejecutar pruebas
echo [2/2] Ejecutando pruebas...
echo.

java -cp "%LIB_DIR%\*;build-console;build-console\test" org.junit.runner.JUnitCore com.gestorempresarial.test.ClienteDAOTest

echo.
echo ========================================
echo   PRUEBAS FINALIZADAS
echo ========================================
pause