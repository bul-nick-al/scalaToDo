package utils

import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{Task, User}

trait TaskToJsonMapping extends SprayJsonSupport with DefaultJsonProtocol {
  implicit def taskToJson = jsonFormat4(Task)

  implicit def userToJson = jsonFormat3(User)

}
