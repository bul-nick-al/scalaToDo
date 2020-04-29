package todo.api

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.headers.BasicHttpCredentials
import akka.http.scaladsl.model.{MessageEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import spray.json._
import todo.mock
import todo.mock.{AuthenticatorMock, ModelServiceMockSuccessful, ModelServiceMockUnsuccessful}
import todo.utils.TaskToJsonMapping

class TestApiSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with ScalaFutures with TaskToJsonMapping{

  val taskToJsonMapping: TaskToJsonMapping = new TaskToJsonMapping {}

  val modelServiceSuccessful = new ModelServiceMockSuccessful()
  val modelServiceUnSuccessful = new ModelServiceMockUnsuccessful()
  val validCredentials: BasicHttpCredentials = BasicHttpCredentials("login", "password")
  val invalidCredentials: BasicHttpCredentials = BasicHttpCredentials("lol", "kek")
  val api: TasksApi = new TasksApi(
    new AuthenticatorMock(),
    modelServiceSuccessful,
    taskToJsonMapping
  )

  val apiUnSuccessful: TasksApi = new TasksApi(
    new AuthenticatorMock(),
    modelServiceUnSuccessful,
    taskToJsonMapping
  )

  "The api" should {
    "Return Not Found for not matching paths" in {
      Get("/kek") ~> api.tasksApi ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }

    "Not allow unsupported http methods" in {
      Post("/task") ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
      }
    }

    "Register a new user" in {
      val userEntity = Marshal(mock.user1).to[MessageEntity].futureValue
      Post("/register").withEntity(userEntity)  ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual "1"
      }
    }
  }

  "The sealed api" should {

    "Block unauthorised access" in {
      Get("/tasks") ~> addCredentials(invalidCredentials) ~> api.tasksApi ~> check {
        status shouldEqual StatusCodes.Unauthorized
      }
    }

    "return all tasks of a user" in {
      Get("/tasks") ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual mock.listOfTasks.toJson.toString()
      }
    }

    "return empty list if there are no tasks" in {
      Get("/tasks") ~> addCredentials(validCredentials) ~> apiUnSuccessful.tasksApi ~> check {
        responseAs[String] shouldEqual "[]"
      }
    }

    "return a task for its id" in {
      Get("/task?id=1") ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual mock.task2.toJson.toString()
      }
    }

    "return Not Found for a non existing task" in {
      Get("/task?id=1") ~> addCredentials(validCredentials) ~> apiUnSuccessful.tasksApi ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }

    "create new task" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Post("/tasks").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual "1"
      }
    }

    "return 0 if a task couldn't be created" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Post("/tasks").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> apiUnSuccessful.tasksApi ~> check {
        responseAs[String] shouldEqual "0"
      }
    }

    "amend existing task" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Put("/tasks").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual "2"
      }
    }

    "return 0 if a task couldn't be amended" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Put("/tasks").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> apiUnSuccessful.tasksApi ~> check {
        responseAs[String] shouldEqual "0"
      }
    }

    "delete new task" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Delete("/task?id=1").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> api.tasksApi ~> check {
        responseAs[String] shouldEqual "5"
      }
    }

    "return 0 if a task couldn't be deleted" in {
      val taskEntity = Marshal(mock.task1).to[MessageEntity].futureValue
      Delete("/task?id=3").withEntity(taskEntity) ~> addCredentials(validCredentials) ~> apiUnSuccessful.tasksApi ~> check {
        responseAs[String] shouldEqual "0"
      }
    }
  }
}
