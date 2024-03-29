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
     spring.datasource.username=root
     spring.datasource.password=testing123$
     
     

## Run the applications

Execute following command to run the application by going in the directory.

    ./gradlew bootrun

## Services information

	1) Create a new Task
		Endpoint : localhost:8080/distribution-service/v1/tasks
		Request Method: POST
		Request Headers : Authorization:Basic YWRtaW46dGVzdGluZzEyMyQ=
						  Content-Type:application/json
		Request Body:
		 {
			"skillNames": ["skill1", "skill2"],
			"priority" : "Low",
		 	"taskStatus":"Not Started"
		}
		Response: 
		{
		    "taskId": 8,
		    "agentId": 3,
		    "skillIds": [
		        1
		    ],
		    "skillNames": [
		        "skill1"
		    ],
		    "priority": "Low",
		    "taskStatus": "Not Started"
		}

	2) Update Task status to Completed
		Endpoint : localhost:8080/distribution-service/v1/tasks/6/status/Completed
		Request Method: PUT
		Request Headers : Authorization:Basic YWRtaW46dGVzdGluZzEyMyQ=
						  Content-Type:application/json
		Request Body:
		Response: 200 OK
		
	3) Get all Tasks with agents are assigned
			Endpoint : localhost:8080/distribution-service/v1/tasks
			Request Method: GET
			Request Headers : Authorization:Basic YWRtaW46dGVzdGluZzEyMyQ=
						  Content-Type:application/json
			Request Body:
			Response: 
			[
			    {
			        "taskId": 16,
			        "agentId": 9,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "High",
			        "taskStatus": "Not Started"
			    },
			    {
			        "taskId": 15,
			        "agentId": 6,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "High",
			        "taskStatus": "Not Started"
			    },
			    {
			        "taskId": 14,
			        "agentId": 3,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "High",
			        "taskStatus": "Not Started"
			    },
			    {
			        "taskId": 13,
			        "agentId": 9,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "Low",
			        "taskStatus": "Not Started"
			    },
			    {
			        "taskId": 12,
			        "agentId": 6,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "Low",
			        "taskStatus": "Not Started"
			    },
			    {
			        "taskId": 11,
			        "agentId": 3,
			        "skillIds": [
			            1
			        ],
			        "skillNames": null,
			        "priority": "Low",
			        "taskStatus": "Not Started"
			    }
			]



