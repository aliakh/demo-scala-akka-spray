package demo

import spray.json.{DefaultJsonProtocol, JsValue, RootJsonFormat, _}

case class Message(payload: String, id: Int)

object MessageJsonProtocol extends DefaultJsonProtocol {
  implicit val messageFormat = jsonFormat2(Message)
}

case class Messages(payloads: Array[Message])

object MessagesJsonProtocol extends DefaultJsonProtocol {
  implicit val messagesFormat = jsonFormat2(Message)
  implicit object MessagesFormat extends RootJsonFormat[Messages] {
    def read(json: JsValue) = Messages(json.convertTo[Array[Message]])
    def write(messages: Messages) = messages.payloads.toJson
  }
}
