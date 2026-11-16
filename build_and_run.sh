#!/bin/bash

# Create lib directory for dependencies
mkdir -p lib

# Download FlatLaf if it doesn't exist
if [ ! -f "lib/flatlaf-3.1.1.jar" ]; then
    echo "Downloading FlatLaf..."
    wget -qO lib/flatlaf-3.1.1.jar https://repo1.maven.org/maven2/com/formdev/flatlaf/3.1.1/flatlaf-3.1.1.jar
fi

# Download JFreeChart if it doesn't exist
if [ ! -f "lib/jfreechart-1.5.3.jar" ]; then
    echo "Downloading JFreeChart..."
    wget -qO lib/jfreechart-1.5.3.jar https://repo1.maven.org/maven2/org/jfree/jfreechart/1.5.3/jfreechart-1.5.3.jar
fi

# Compile the Java source files
echo "Compiling sources..."
javac -cp "lib/*" src/gui/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful! Running the application..."
    # Run the application
    java -cp "src:lib/*" gui.MainApplication
else
    echo "Compilation failed. Please check the errors above."
fi
