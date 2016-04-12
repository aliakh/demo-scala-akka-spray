package demo

import akka.actor._
import akka.event.{Logging, LoggingReceive}
import demo.AggregatorActor1.Aggregate
import demo.MessagesJsonProtocol._
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.http.{Uri, _}
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.routing.RequestContext

import scala.concurrent.duration._

class AggregatorActor1(uri: Uri, requestContext: RequestContext) extends Actor {

  import context._

  implicit val system = context.system
  val log = Logging(system, getClass)

  val size = 3
  var messages = List[Message]()

  def receive = LoggingReceive {
    case Aggregate() =>
      log.info("Aggregation started")
      setReceiveTimeout(1.second)

      for (i <- 1 to size) {
        log.info("Aggregator send request {}", i)
        val restClient = context.actorOf(RestClientActor.props(uri))
        restClient ! HttpRequest(GET, uri)
      }

      context.become(waitingForResponse())
  }

  def waitingForResponse(): Receive = {
    case Status.Success(response) =>
      log.info("Aggregator received response {}", messages.size)

      response.asInstanceOf[HttpResponse].entity.as[Message] match {
        case Right(message) => messages = message :: messages
        case Left(error) =>
          requestContext.complete(BadRequest, Message("Aggregator failed during JSON conversion: " + error, -1))
          context.stop(self)
      }

      if (messages.size == size) {
        log.info("Aggregation succeed: {}", messages)
        requestContext.complete(OK, Messages(messages.toArray))
        context.stop(self)
      }

    case ReceiveTimeout =>
      requestContext.complete(GatewayTimeout, Message("Aggregator failed by receive timeout", -1))
      context.stop(self)

    case Status.Failure(error) =>
      requestContext.complete(BadRequest, Message("Aggregator failed: " + error, -1))
      context.stop(self)
  }
}

object AggregatorActor1 {

  case class Aggregate()

  def props(uri: Uri, requestContext: RequestContext): Props = Props(new AggregatorActor1(uri, requestContext))
}