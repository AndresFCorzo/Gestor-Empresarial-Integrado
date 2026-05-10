@echo off
echo ========================================
echo   EJECUTANDO VERSION CONSOLA
echo ========================================
cd /d %~dp0
java -cp "src\main\webapp\WEB-INF\lib\*;build-console" com.gestorempresarial.console.MenuPrincipal
pause