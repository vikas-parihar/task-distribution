## Prerequisites 
Project uses Gradle as the build tool `gradle -v`. The project is set for java compatibility of version 1.8 inside the gradle script as shown below.
    
    sourceCompatibility = 1.8

The above gradle setting enables the build script to use the mentioned Java version to use when compiling Java source. So 1.8 is the recommended JVM to use.

---

## Build the artifact

Go to the root of the project folder where the code is checked out and simply execute `gradle build`. This will get the code compiled and assembles the artifacts and executed the test (Unit test) and generates the coverage report (Jacoco).


---

## Executing the Unit test

To execute the test seperately,  simply execute `gradle check`. This will get the Unit test, Check Style and Findbugs executed and will generate the coverage report. The reports can be found inside the the reports directory as listed below

<pre>
Unit test report: ./build/reports/tests/test/index.html
</pre>

## Database setup

For information on how to setup the postgres database for the application and create the database, see below:

# Configuring Postgres database in local.
### Mac Setup.
To install the postgres locally using homebrew 
     
     brew install postgresql
     brew services start postgresql
     
Install any IDE to connect to postgres like (Datagrip). Create the database called workdistribution. Create database with following query. 

     create database workdistribution;
     create schema public;
     
Add the following properties in applicaiton.properties file 
     
     spring.datasource.url=jdbc:postgresql://localhost:5432/workdistribution
     spring.datasource.username=pearson
     spring.datasource.password=pearson
     
     

## Run the applications

Execute following command to run the application by going in the directory.

    ./gradlew bootrun



---

