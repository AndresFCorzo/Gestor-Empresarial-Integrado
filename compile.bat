@echo off 
echo Compilando Gestor Empresarial Integrado... 
mkdir build 2>nul 
dir /s /B src\main\java\*.java > sources.txt 
javac -d build -cp "lib\*;build" @sources.txt 
del sources.txt 
echo Compilacion completada. 
