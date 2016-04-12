package demo

import akka.actor.{Actor, _}
import akka.event.{Logging, LoggingReceive}
import akka.util.Timeout
import demo.AggregatorActor2.Aggregate
import demo.MessagesJsonProtocol._
import spray.client.pipelining._
import spray.http.StatusCodes._
import spray.http.Uri
import spray.httpx.SprayJsonSupport._
import spray.routing.RequestContext

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class AggregatorActor2(uri: Uri, requestContext: RequestContext) extends Actor {

  implicit val system = context.system
  val log = Logging(system, getClass)

  val size = 3

  def receive = LoggingReceive {
    case Aggregate() =>
      log.info("Aggregation started")

      implicit val timeout: Timeout = 1.second
      implicit val ec: ExecutionContext = context.dispatcher

      val pipeline = sendReceive ~> unmarshal[Message]

      val sequence: Seq[Future[Message]] = (1 to size).map{i => pipeline { Get(uri) } }
      val response: Future[Seq[Message]] = Future.sequence(sequence)

      response.onComplete {
        case Success(messages) =>
          log.info("Aggregation succeed: {}", messages)
          requestContext.complete(OK, Messages(messages.toArray))
          context.stop(self)

        case Failure(error) =>
          log.info("Aggregation failed: {}", error)
          requestContext.complete(BadRequest, Message("Aggregator failed: " + error, -1))
          context.stop(self)
      }
  }
}

object AggregatorActor2 {

  case class Aggregate()

  def props(uri: Uri, requestContext: RequestContext): Props = Props(new AggregatorActor2(uri, requestContext))
}