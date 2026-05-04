***How to run the code***

***Requirements!!!***
    Java 21 or newer
    JavaFX SDK 21 — download from https://gluonhq.com/products/javafx/

***On Mac or Linux***


Clone the repository and open Terminal, navigate to the project folder:

            cd path/to/term-project-team-31


Compile ***This first!!***:

***The path must be to the location of sdk***
***For example:*** java --module-path /Users/vahidtaghiyev/Dev/javafx-sdk-21.0.10/lib \

```bash
javac --module-path /path/to/javafx-sdk-21.0.10/lib \
--add-modules javafx.controls,javafx.fxml \
-d out \
$(find src -name "*.java")
```

Run:
***The path must be to the location of sdk***
```bash
java --module-path /path/to/javafx-sdk-21.0.10/lib \
--add-modules javafx.controls,javafx.fxml \
-cp out main.MainApp
```

***On Windows*** 

***Clone the repository and open Command Prompt, navigate to the project folder:***

            cd path\to\term-project-team-31


***Compile:***
            ***The path must be to the location of sdk***

```bash 
javac --module-path C:\path\to\javafx-sdk-21.0.10\lib --add-modules javafx.controls,javafx.fxml -d out $(find src -name "*.java")
```
Run:
            ***The path must be to the location of sdk***
```bash
java --module-path C:\path\to\javafx-sdk-21.0.10\lib --add-modules javafx.controls,javafx.fxml -cp out main.MainApp
```
