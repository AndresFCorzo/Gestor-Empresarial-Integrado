@echo off
title COMPILAR CONSOLA (FINAL)
color 0A

echo ========================================
echo   COMPILANDO GESTOR EMPRESARIAL
echo        VERSION CONSOLA
echo ========================================
echo.

cd /d "C:\Users\ASUS\Documents\SENA\Proyectos\GESTOR_EMPRESARIAL_INTEGRADO"

:: Limpiar
if exist build-console rmdir /S /Q build-console
mkdir build-console

:: Compilar desde src/main/java directamente
cd src\main\java
echo Compilando...
javac -encoding UTF-8 -cp "..\..\webapp\WEB-INF\lib\*" -d ..\..\..\build-console com\gestorempresarial\modelo\*.java com\gestorempresarial\dao\*.java com\gestorempresarial\utils\*.java com\gestorempresarial\console\controlador\*.java com\gestorempresarial\console\vista\*.java

:: Volver al proyecto
cd ..\..\..

:: Verificar
if exist build-console\com\gestorempresarial\console\vista\MenuPrincipal.class (
    echo.
    echo ========================================
    echo   COMPILACION EXITOSA
    echo ========================================
    echo.
    echo Ejecutando aplicacion...
    java -cp "build-console;src\main\webapp\WEB-INF\lib\mysql-connector-j-8.0.33.jar" com.gestorempresarial.console.vista.MenuPrincipal
) else if exist build-console\main\java\com\gestorempresarial\console\vista\MenuPrincipal.class (
    echo.
    echo [WARN] Archivos en ubicacion incorrecta, corrigiendo...
    mkdir build-console\com 2>nul
    xcopy /E /I /Y build-console\main\java\com\gestorempresarial build-console\com\gestorempresarial >nul
    echo.
    echo Ejecutando aplicacion...
    java -cp "build-console;src\main\webapp\WEB-INF\lib\mysql-connector-j-8.0.33.jar" com.gestorempresarial.console.vista.MenuPrincipal
) else (
    echo.
    echo ========================================
    echo   ERROR DE COMPILACION
    echo ========================================
    echo No se encontraron los archivos .class
)

pause