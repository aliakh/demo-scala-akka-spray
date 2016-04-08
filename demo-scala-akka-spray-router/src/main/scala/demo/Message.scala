package demo

import spray.json.DefaultJsonProtocol

case class Message(payload: String, id: Int)

object MessageJsonProtocol extends DefaultJsonProtocol {
  implicit val format = jsonFormat2(Message)
}
