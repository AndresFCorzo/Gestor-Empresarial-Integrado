@echo off
title LIMPIAR PROYECTO
color 0C

echo ========================================
echo   LIMPIANDO ARCHIVOS GENERADOS
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

echo [1/5] Eliminando build-console...
if exist build-console rmdir /S /Q build-console
echo       ✓ Eliminado

echo [2/5] Eliminando build-web...
if exist build-web rmdir /S /Q build-web
echo       ✓ Eliminado

echo [3/5] Eliminando archivos JAR...
if exist gestor-empresarial-console.jar del /Q gestor-empresarial-console.jar
if exist gestor-empresarial.war del /Q gestor-empresarial.war
echo       ✓ Eliminados

echo [4/5] Eliminando archivos temporales...
del /Q *.txt 2>nul
del /Q errores_*.txt 2>nul
echo       ✓ Eliminados

echo [5/5] Eliminando logs...
if exist logs rmdir /S /Q logs 2>nul
echo       ✓ Eliminados

echo.
echo ========================================
echo   ✅ LIMPIEZA COMPLETADA
echo ========================================
echo.
echo   Para recompilar, ejecute:
echo   - build-console.bat (versión consola)
echo   - build-web.bat (versión web)
echo.
pause