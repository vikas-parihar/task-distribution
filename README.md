## Prerequisites 
Project uses Gradle as the build tool `gradle -v`. The project is set for java compatibility of version 1.8 inside the gradle script as shown below.
    
    sourceCompatibility = 1.8

The above gradle setting enables the build script to use the mentioned Java version to use when compiling Java source. So 1.8 is the recommended JVM to use.

[BACK to TOC](../../README.md)

---

## Build the artifact

Go to the root of the project folder where the code is checked out and simply execute `gradle build`. This will get the code compiled and assembles the artifacts and executed the test (Unit test) and generates the coverage report (Jacoco).

[BACK to TOC](../../README.md)

---

## Executing the Unit test

To execute the test seperately,  simply execute `gradle check`. This will get the Unit test, Check Style and Findbugs executed and will generate the coverage report. The reports can be found inside the the reports directory as listed below

<pre>
Unit test report: ./build/reports/tests/test/index.html
</pre>

## Database setup

For information on how to setup the postgres database for the application and create the database, see [this guide](./doc/Database.md)

## Run the applications

Execute following command to run the application by going in the directory.

    ./gradlew bootrun


[BACK to TOC](../../README.md)

---

