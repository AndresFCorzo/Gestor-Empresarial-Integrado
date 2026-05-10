@echo off
echo ========================================
echo   COMPILANDO VERSION WEB
echo ========================================
cd /d %~dp0
if not exist build-web mkdir build-web
if not exist build-web\WEB-INF\classes mkdir build-web\WEB-INF\classes
if not exist build-web\WEB-INF\lib mkdir build-web\WEB-INF\lib
dir /s /B src\main\java\com\gestorempresarial\modelo\*.java > sources.txt
dir /s /B src\main\java\com\gestorempresarial\dao\*.java >> sources.txt
dir /s /B src\main\java\com\gestorempresarial\utils\*.java >> sources.txt
dir /s /B src\main\java\com\gestorempresarial\web\servlets\*.java >> sources.txt
javac -cp "src\main\webapp\WEB-INF\lib\*;build-web\WEB-INF\classes" -d build-web\WEB-INF\classes @sources.txt 2> errores.txt
if %errorlevel% equ 0 ( echo ✅ Compilacion exitosa ) else ( echo ❌ Error de compilacion & type errores.txt )
del sources.txt
xcopy /E /I /Y src\main\webapp\* build-web >nul
echo WAR generado en: build-web\
pause