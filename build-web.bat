@echo off
title COMPILAR VERSIÓN WEB
color 0A

echo ========================================
echo   COMPILANDO GESTOR EMPRESARIAL
echo         VERSIÓN WEB
echo ========================================
echo.

set PROJECT_DIR=%~dp0
cd /d "%PROJECT_DIR%"

:: Crear directorio de compilación
echo [1/6] Creando directorio build-web...
if exist build-web rmdir /S /Q build-web 2>nul
mkdir build-web
mkdir build-web\WEB-INF
mkdir build-web\WEB-INF\classes
mkdir build-web\WEB-INF\lib
echo       ✓ Directorio listo

:: Verificar librerías
echo [2/6] Verificando librerías...
set LIB_DIR=src\main\webapp\WEB-INF\lib

:: Verificar MySQL Connector
if not exist "%LIB_DIR%\mysql-connector-j-8.0.33.jar" (
    echo       ⚠️  Descargando MySQL Connector...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar' -OutFile '%LIB_DIR%\mysql-connector-j-8.0.33.jar'"
)

:: Verificar JSTL
if not exist "%LIB_DIR%\jstl-1.2.jar" (
    echo       ⚠️  Descargando JSTL...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar' -OutFile '%LIB_DIR%\jstl-1.2.jar'"
)

:: Verificar Servlet API (para compilación)
if not exist "%LIB_DIR%\javax.servlet-api-4.0.1.jar" (
    echo       ⚠️  Descargando Servlet API...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar' -OutFile '%LIB_DIR%\javax.servlet-api-4.0.1.jar'"
)
echo       ✓ Librerías verificadas

:: Compilar clases Java
echo [3/6] Compilando clases Java...

set SOURCES_FILE=sources_web.txt
if exist %SOURCES_FILE% del %SOURCES_FILE%

:: Agregar modelos (compartidos)
dir /s /B src\main\java\com\gestorempresarial\modelo\*.java >> %SOURCES_FILE% 2>nul

:: Agregar DAOs (compartidos)
dir /s /B src\main\java\com\gestorempresarial\dao\*.java >> %SOURCES_FILE% 2>nul

:: Agregar utils (compartidos)
dir /s /B src\main\java\com\gestorempresarial\utils\*.java >> %SOURCES_FILE% 2>nul

:: Agregar servlets web
dir /s /B src\main\java\com\gestorempresarial\web\servlets\*.java >> %SOURCES_FILE% 2>nul

:: Agregar filters (si existen)
dir /s /B src\main\java\com\gestorempresarial\web\filters\*.java >> %SOURCES_FILE% 2>nul

:: Compilar
set CP="%LIB_DIR%\*;build-web\WEB-INF\classes"
javac -cp %CP% -d build-web\WEB-INF\classes @%SOURCES_FILE% 2> errores_web.txt

if %errorlevel% equ 0 (
    echo       ✓ Compilación exitosa
    del %SOURCES_FILE% 2>nul
) else (
    echo       ❌ Error de compilación
    echo.
    echo === ERRORES DE COMPILACIÓN ===
    type errores_web.txt
    del %SOURCES_FILE% 2>nul
    pause
    exit /b 1
)

:: Copiar archivos web (JSP, CSS, JS)
echo [4/6] Copiando archivos web...
xcopy /E /I /Y src\main\webapp\* build-web >nul

:: Copiar archivos de configuración
echo [5/6] Copiando recursos...
if exist src\main\resources\database.properties (
    copy src\main\resources\database.properties build-web\WEB-INF\classes\ >nul
)
echo       ✓ Recursos copiados

:: Crear archivo web.xml (si no existe en src)
echo [6/6] Verificando configuración...
if not exist build-web\WEB-INF\web.xml (
    echo ⚠️  Advertencia: No se encontró web.xml
    echo   Se creará uno básico...
    (
        echo ^<?xml version="1.0" encoding="UTF-8"?^>
        echo ^<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
        echo          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        echo          xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee 
        echo          http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
        echo          version="4.0"^>
        echo     ^<display-name^>Gestor Empresarial^</display-name^>
        echo     ^<welcome-file-list^>
        echo         ^<welcome-file^>login.jsp^</welcome-file^>
        echo     ^</welcome-file-list^>
        echo ^</web-app^>
    ) > build-web\WEB-INF\web.xml
)
echo       ✓ Configuración lista

:: Crear archivo WAR (opcional)
echo.
echo Creando archivo WAR...
cd build-web
jar -cvf ..\gestor-empresarial.war * >nul
cd ..
if %errorlevel% equ 0 (
    echo       ✓ WAR creado: gestor-empresarial.war
) else (
    echo       ⚠️  No se pudo crear WAR
)

echo.
echo ========================================
echo   ✅ COMPILACIÓN WEB COMPLETADA
echo ========================================
echo.
echo   Archivos generados:
echo   - build-web/          (carpeta desplegable)
echo   - gestor-empresarial.war  (archivo para Tomcat)
echo.
echo   Para desplegar:
echo   1. deploy-web.bat
echo   2. Copiar gestor-empresarial.war a Tomcat/webapps/
echo.
pause