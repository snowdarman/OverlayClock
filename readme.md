# JavaFX Clock App

A simple always-on-top clock application built with JavaFX.  
It displays the current time (including seconds) and date (with weekday), and allows window dragging without a title bar.

![screenshot](screenshot.png)

## ✨ Features

- Displays current **time with seconds**
- Shows **date and weekday**
- **Always on top**
- **Frameless** and draggable window
- Right-click menu with **Exit**
- **Alarm support** with time + comment
- Load alarms from text file (with `hh:mm | comment` format)
- Highlight upcoming alarms 3 minutes before
- Custom background image support (optional)
- Packaged using **jlink** (custom runtime included)

## ⏰ Alarm Feature

You can manage a list of alarms using the **Settings** dialog.  
Each alarm consists of a time (`hh:mm`) and a comment. The clock will monitor upcoming alarms and show the relevant comment **3 minutes before** the scheduled time.

### � Alarm File Format

You can load alarms from a `.txt` file using the “Browse” button in the settings dialog.

- One alarm per line
- Use `|` (pipe) or `｜` (full-width pipe) to separate time and comment
- Format:  

## 🛠 Requirements

- Java 17+
- JavaFX 21+
- Maven 3.6+
- Module-aware JDK (for `jlink`)

## 📦 Build and Run

To build the project and create a self-contained runtime using `jlink`:

```bash
mvn clean install
mvn jlink:jlink
```

## To run the application:

./target/app-image/bin/clock

## 📁 Project Structure
```
src/
└── main/
    ├── java/
    │   └── com/example/ClockApp.java
    ├── resources/
    │   └── background.png (optional)
pom.xml
```

## 🚀 Distribution

The output in target/app-image/ contains the clock binary and all required Java modules.
You can zip and distribute this folder directly. Java installation is not required on the target machine.

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

Created by [Toshiaki Shioya @ aioidgt.co.jp]<br>
E-mail t-shioya@aioidgt.co.jp<br>
GitHub: https://github.com/yourusername<br>

# 🛠 Installation Instructions (with JavaFX)

To run this application, you need to have a Java environment with JavaFX installed. Follow the steps below according to your OS.
### 1. Install JDK (Java Development Kit)

Download and install JDK 17 or later if not already installed.

Verify installation:

```
java -version
javac -version
```

### 2. Install JavaFX SDK

Download JavaFX SDK from https://gluonhq.com/products/javafx/, and unzip it to a known location.
### 3. Set Environment Variables (Optional but recommended)

Add the JavaFX SDK lib path to an environment variable for convenience. For example:

    Windows:

        Set PATH_TO_FX:
        C:\javafx-sdk-21\lib

    macOS / Linux:

        Add to ~/.bashrc or ~/.zshrc:

        export PATH_TO_FX=/path/to/javafx-sdk-21/lib

### 4. Run the Application with JavaFX Modules

When running from the terminal:

java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.graphics -jar your-app.jar

    Replace your-app.jar with the name of your compiled JAR file.

### 5. Optional: Run with Maven

If using Maven, JavaFX dependencies will be resolved automatically. To run the app:

mvn javafx:run

Make sure pom.xml includes the correct JavaFX dependencies and plugin configuration.

