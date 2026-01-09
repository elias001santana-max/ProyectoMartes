@echo off
setlocal

:: Configuración de colores (Azul de fondo, Blanco de texto)
color 1F

title INSTALADOR Y EJECUTOR - HOTEL VENTURA
echo ========================================
echo   SISTEMA DE GESTION HOTELERA VENTURA
echo ========================================
echo.

:: 1. Verificar si existe Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] No se reconoce 'java'. Por favor instala el JDK.
    pause
    exit /b 1
)

:: 2. Limpiar compilaciones anteriores
echo [1/4] Limpiando archivos temporales...
if exist bin (
    rd /s /q bin
)
mkdir bin

:: 3. Compilar
echo.
echo [3/4] Compilando archivos Java...
cd src
javac -encoding UTF-8 -cp ".;BD\sqlite-jdbc-3.51.1.0 (1).jar" -d ..\bin BD\*.java 2>error.log

if %errorlevel% neq 0 (
    echo.
    echo ERROR al compilar. Detalles:
    type error.log
    echo.
    pause
    cd ..
    exit /b 1
)

del error.log 2>nul
cd ..

echo.
echo [4/4] Ejecutando programa...
echo ========================================
echo.

java -cp "bin;src;src\BD\sqlite-jdbc-3.51.1.0 (1).jar" BD.Loginn

if %errorlevel% neq 0 (
    echo.
    echo El programa se cerro de forma inesperada.
    pause
)

echo.
echo Proceso finalizado.
pause
exit /b 0
