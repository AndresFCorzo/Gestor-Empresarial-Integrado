@echo off
echo ========================================
echo   DEPLOY EN TOMCAT
echo ========================================
set TOMCAT_HOME=C:\apache-tomcat-9.0.xx
set WAR_FILE=%~dp0build-web
echo Copiando a Tomcat...
xcopy /E /I /Y "%WAR_FILE%" "%TOMCAT_HOME%\webapps\gestor-empresarial\" >nul
echo ✅ Desplegado en: http://localhost:8080/gestor-empresarial
pause