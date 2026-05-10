@echo off
title CONFIGURACIÓN INICIAL
color 0A

echo ========================================
echo   CONFIGURANDO GESTOR EMPRESARIAL
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Crear directorios necesarios
echo [1/7] Creando directorios...
if not exist src\main\webapp\WEB-INF\lib mkdir src\main\webapp\WEB-INF\lib
if not exist sql mkdir sql
if not exist logs mkdir logs
echo       ✓ Directorios creados

:: Verificar base de datos
echo [2/7] Verificando MySQL...
mysql --version >nul 2>nul
if %errorlevel% neq 0 (
    echo ❌ MySQL no está instalado o no está en PATH
    echo.
    echo Por favor, instale MySQL desde: https://dev.mysql.com/downloads/
    pause
    exit /b 1
)
echo       ✓ MySQL encontrado

:: Crear base de datos
echo [3/7] Creando base de datos...
echo ¿Tiene contraseña de root? (Presione Enter si no tiene)
set /p MYSQL_PASS="Contraseña de root (dejar vacío si no tiene): "

if "%MYSQL_PASS%"=="" (
    mysql -u root < sql\schema.sql 2>nul
) else (
    mysql -u root -p%MYSQL_PASS% < sql\schema.sql 2>nul
)

if %errorlevel% equ 0 (
    echo       ✓ Base de datos creada
) else (
    echo       ⚠️  No se pudo crear la base de datos
    echo   Ejecute manualmente: mysql -u root -p ^< sql\schema.sql
)

:: Configurar credenciales en ConexionBD.java
echo [4/7] Configurando credenciales...
set /p DB_USER="Usuario de MySQL (default: root): "
if "%DB_USER%"=="" set DB_USER=root

set /p DB_PASS="Contraseña de MySQL (dejar vacío si no tiene): "

:: Actualizar ConexionBD.java
set CONEXION_FILE=src\main\java\com\gestorempresarial\dao\ConexionBD.java
if exist "%CONEXION_FILE%" (
    powershell -Command "(Get-Content '%CONEXION_FILE%') -replace 'private static final String USUARIO = \".*\"', 'private static final String USUARIO = \"%DB_USER%\"' | Set-Content '%CONEXION_FILE%'"
    powershell -Command "(Get-Content '%CONEXION_FILE%') -replace 'private static final String CONTRASENA = \".*\"', 'private static final String CONTRASENA = \"%DB_PASS%\"' | Set-Content '%CONEXION_FILE%'"
    echo       ✓ Credenciales configuradas
)

:: Descargar librerías
echo [5/7] Descargando librerías necesarias...
set LIB_DIR=src\main\webapp\WEB-INF\lib

powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile '%LIB_DIR%\mysql-connector-j-8.0.33.jar'"
echo       ✓ MySQL Connector descargado

powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar' -OutFile '%LIB_DIR%\jstl-1.2.jar'"
echo       ✓ JSTL descargado

powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar' -OutFile '%LIB_DIR%\javax.servlet-api-4.0.1.jar'"
echo       ✓ Servlet API descargado

:: Crear archivo .env para variables de entorno
echo [6/7] Creando archivo de configuración...
(
echo # Configuración de base de datos
echo DB_HOST=localhost
echo DB_PORT=3306
echo DB_NAME=gestor_empresarial
echo DB_USER=%DB_USER%
echo DB_PASSWORD=%DB_PASS%
echo.
echo # Configuración de Tomcat
echo TOMCAT_HOME=C:\apache-tomcat-9.0.89
) > .env
echo       ✓ .env creado

:: Verificar compilación
echo [7/7] Verificando compilación...
call build-console.bat

echo.
echo ========================================
echo   ✅ CONFIGURACIÓN COMPLETADA
echo ========================================
echo.
echo   Para ejecutar la versión consola:
echo   run-console.bat
echo.
echo   Para ejecutar la versión web:
echo   1. build-web.bat
echo   2. deploy-web.bat (si tienes Tomcat)
echo   3. run-web.bat
echo.
pause