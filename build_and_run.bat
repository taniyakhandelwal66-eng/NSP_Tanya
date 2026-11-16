@echo off
echo Compiling sources...
javac -cp "lib\*" src\gui\*.java

if %ERRORLEVEL% equ 0 (
    echo Compilation successful! Running the application...
    java -cp "src;lib\*" gui.MainApplication
) else (
    echo Compilation failed. Please check the errors above.
)
pause
