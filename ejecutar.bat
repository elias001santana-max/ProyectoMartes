@echo off
setlocal

:: Definir ruta del JDK
set "JAVA_BIN=C:\Users\usuario\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.8.v20250724-1412\jre\bin"

echo ========================================
echo   COMPILANDO PROYECTO HOTELERO
echo ========================================

:: Ir al directorio raíz del proyecto
cd /d "C:\Users\usuario\Desktop\ProyectoMartes"

:: Limpiar directorio bin
echo.
echo [1/3] Limpiando compilaciones anteriores...
if exist bin (
    rd /s /q bin
)
mkdir bin

:: Compilar con el driver SQLite en el classpath
echo.
echo [2/3] Compilando archivos Java...
cd src
"%JAVA_BIN%\javac.exe" -encoding UTF-8 -cp ".;BD\sqlite-jdbc-3.51.1.0 (1).jar" -d ..\bin BD\*.java

if %errorlevel% neq 0 (
    echo.
    echo ERROR: No se pudo compilar el proyecto
    echo Verifica que tengas Java JDK instalado
    echo.
    pause
    cd ..
    exit /b 1
)

cd ..

echo.
echo Compilacion exitosa!
echo.
echo ========================================
echo   EJECUTANDO SISTEMA HOTELERO
echo ========================================
echo.

:: Ejecutar con el driver SQLite en el classpath
echo [3/3] Iniciando aplicacion...
"%JAVA_BIN%\java.exe" -cp "bin;src\BD\sqlite-jdbc-3.51.1.0 (1).jar" BD.Loginn

if %errorlevel% neq 0 (
    echo.
    echo El programa se cerro de forma inesperada.
    pause
)

echo.
pause
