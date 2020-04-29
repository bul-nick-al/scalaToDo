
### ToDo List app with Scala Akka HTTP API  and Quill

  
  This is a course project implemented in scala, which implements backend logic of a Todo list. One can create a user and use the credantials later to add, edit and delete tasks as well as mark them as completed or not completed

### How to run

#### Prerequisites 
This project has the following the following components to be installed on your machine:

1. Docker (for the database)
2. Java 1.8
3. sbt 1.3.9

#### Run
First you need launch the docker containers. For this do the following

1. Navigate to the root directory of the project
```
$ cd <project folder>
```
2. Run docker-compose
```
$ docker-compose up
```

Next use sbt to launch the project 
```
$ sbt run
```

#### Functionality 

Service should be up and running on `http://localhost:8086` if there are no database config issues.

Backend serves one API with `GET`, `POST`, `PUT` and `DELETE` methods to create a new user and then retrieve, create, update and delete a task. Also you can retrieve all tasks for a user and filter them.

API endpoints:


```
POST http://localhost:8086/api/register
with Basic Authentication and body 
{
"id":0,
"login": "nick",
"password": "123"
}

POST http://localhost:8086/api/tasks
with Basic Authentication and body 
{
"id":-1, // does not matter
"title": "Eat potatoes",
"description": "It is important to eat potatoes",
"userId": -1, // does not matter
"completed": false
}

GET http://localhost:8086/api/task?id={taskId}
with Basic Authentication

PUT http://localhost:8086/api/tasks
with Basic Authentication and body
{
"id":3, //here id matters
"title": "Eat potatoes",
"description": "It is pivotal to eat potatoes",
"userId": -1,
"completed": true
}

DELETE http://localhost:8086/api/task?id={taskId}
with Basic Authentication

GET http://localhost:8086/api/tasks
with Basic Authentication

GET http://localhost:8086/api/tasks?completed={true/false}
with Basic Authentication

```


### Tests

To run the test execute
  
```
$ sbt test
$ docker-compose up
```
**It is essential that you run exactly the test database in docker as it is populated for the tests!**
### Databases

There are two databases: 

1. The regular database, that will be empty at the start and will be perserved if you shout the server down as long as the docker container is running
2. The test database that is populated when deployed with dummy data.

If you want to use another port for either of them or change other setting for db access, do so in `application.conf`
