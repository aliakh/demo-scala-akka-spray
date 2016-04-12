package demo

import akka.actor._
import akka.event.LoggingReceive
import demo.MessageJsonProtocol._
import demo.RouterActor1.Route
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.http.{Uri, _}
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.routing.RequestContext

import scala.concurrent.duration._

class RouterActor1(uri: Uri, requestContext: RequestContext) extends Actor {

  import context._

  def receive = LoggingReceive {
    case Route() =>
      setReceiveTimeout(1.second)
      val restClient = context.actorOf(Props(classOf[RestClientActor], uri))
      restClient ! HttpRequest(GET, uri)

    case Status.Success(response) =>
      response.asInstanceOf[HttpResponse].entity.as[Message] match {
        case Right(message) => requestContext.complete(OK, message)
        case Left(error) => requestContext.complete(BadRequest, Message("Router failed during JSON conversion: " + error, -1))
      }
      context.stop(self)

    case ReceiveTimeout =>
      requestContext.complete(GatewayTimeout, Message("Router failed by receive timeout", -1))
      context.stop(self)

    case Status.Failure(error) =>
      requestContext.complete(BadRequest, Message("Router failed: " + error, -1))
      context.stop(self)
  }
}

object RouterActor1 {

  case class Route()
}
