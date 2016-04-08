package demo

import akka.actor.Actor
import akka.event.LoggingReceive
import demo.MessageJsonProtocol._
import demo.RouterActor2._
import spray.client.pipelining._
import spray.http.Uri
import spray.httpx.SprayJsonSupport._
import spray.routing.RequestContext

import scala.util.{Failure, Success}

class RouterActor2(uri: Uri, requestContext: RequestContext) extends Actor {

  import context._

  implicit val system = context.system

  def receive = LoggingReceive {
    case Route() =>

      val pipeline = sendReceive ~> unmarshal[Message]
      val responseFuture = pipeline {
        Get(uri)
      }

      responseFuture.onComplete {
        case Success(message) => requestContext.complete(message)
        case Failure(error) => requestContext.complete(error)
      }

      context.stop(self)
  }
}

object RouterActor2 {
  case class Route()
}
