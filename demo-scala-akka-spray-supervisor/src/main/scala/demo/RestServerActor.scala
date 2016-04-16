package demo

import akka.actor.{Actor, _}
import akka.event.LoggingReceive
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http
import spray.http.HttpMethods._
import spray.http.{HttpRequest, Uri}

import scala.concurrent.duration._

class RestServerActor extends Actor {

  implicit val timeout: Timeout = 1.seconds

  def receive = LoggingReceive {
    case _: Http.Connected => sender ? Http.Register(self)

    case request@HttpRequest(GET, Uri.Path("/supervisor"), _, _, _) =>
      val supervisor = context.actorOf(SupervisorActor.props(sender()))
      supervisor ! SupervisorActor.Supervise()
  }
}
