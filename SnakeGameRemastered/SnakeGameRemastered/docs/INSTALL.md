# Snake Game: Classic Remastered - Installation Guide

## System Requirements

- **Operating System**: Windows, macOS, or Linux
- **Java**: JDK 17 or higher (JRE not sufficient)
- **Memory**: 256MB RAM minimum
- **Disk Space**: 50MB available

## Installation Steps

### Windows

1. Download the latest release (.jar file)
2. Ensure Java 17+ is installed:
   ```
   java -version
   ```
3. Double-click the .jar file to run, or run from command prompt:
   ```
   java -jar snake-game-remastered.jar
   ```

### macOS

1. Download the .jar file
2. Verify Java is installed (comes pre-installed on macOS)
3. Run from terminal:
   ```
   java -jar snake-game-remastered.jar
   ```

### Linux

1. Install OpenJDK 17:
   ```
   sudo apt-get install openjdk-17-jdk
   ```
2. Download the .jar file
3. Run:
   ```
   java -jar snake-game-remastered.jar
   ```

## Building from Source

1. Install Maven 3.8+
2. Clone the repository
3. Build:
   ```
   mvn clean package
   ```
4. Run:
   ```
   java -jar target/snake-game-remastered.jar
   ```

## Troubleshooting

### "Java not found" error
- Ensure Java JDK (not just JRE) is installed
- Add Java to PATH environment variable

### "Could not find or load main class"
- Rebuild with Maven: `mvn clean package`
- Verify the jar file was created in target/

### Game runs slowly
- Ensure no other heavy applications are running
- Try reducing game grid size in settings
- Update graphics drivers

### Sound not working
- Check system volume
- Enable sound in Settings menu

## Getting Help

- Check the README.md for controls and features
- Review the code documentation in docs/
- Submit issues on the project repository

## Uninstalling

Simply delete the .jar file and any saved data in the saves/ and profiles/ directories.