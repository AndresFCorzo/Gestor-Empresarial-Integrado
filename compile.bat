@echo off
echo Compilando Gestor Empresarial Integrado...
mkdir build 2>nul
mkdir build\resources 2>nul

:: Copiar archivos de resources a build
xcopy /E /I src\main\resources build\resources

:: Compilar archivos .java
dir /s /B src\main\java\*.java > sources.txt
javac -d build -cp "lib\*;build" @sources.txt
del sources.txt

echo Copiando recursos...
xcopy /Y src\main\resources\* build\resources\

echo Compilacion completada.