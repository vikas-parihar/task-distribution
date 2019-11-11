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
     
     