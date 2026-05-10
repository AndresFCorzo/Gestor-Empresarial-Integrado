@echo off
echo ========================================
echo   COMPILANDO VERSION CONSOLA
echo ========================================
cd /d %~dp0
if not exist build-console mkdir build-console
dir /s /B src\main\java\com\gestorempresarial\modelo\*.java > sources.txt
dir /s /B src\main\java\com\gestorempresarial\dao\*.java >> sources.txt
dir /s /B src\main\java\com\gestorempresarial\utils\*.java >> sources.txt
dir /s /B src\main\java\com\gestorempresarial\console\*.java >> sources.txt
javac -cp "src\main\webapp\WEB-INF\lib\*;build-console" -d build-console @sources.txt 2> errores.txt
if %errorlevel% equ 0 ( echo ✅ Compilacion exitosa ) else ( echo ❌ Error de compilacion & type errores.txt )
del sources.txt
pause