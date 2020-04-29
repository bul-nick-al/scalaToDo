package todo.utils

import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import todo.models.{Task, User}

trait TaskToJsonMapping extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def taskToJson: RootJsonFormat[Task] = jsonFormat5(Task)

  implicit def userToJson: RootJsonFormat[User] = jsonFormat3(User)

}
