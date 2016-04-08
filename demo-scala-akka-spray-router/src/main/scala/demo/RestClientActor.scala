package demo

import akka.actor._
import akka.event.LoggingReceive
import akka.io.IO
import spray.can.Http
import spray.http._

class RestClientActor(uri: Uri) extends Actor {

  import context.system

  val host = uri.authority.host.address
  val port = uri.authority.port

  def receive: Receive = LoggingReceive {
    case request: HttpRequest =>
      IO(Http) ! Http.Connect(host, port)
      context.become(waitingForConnection(sender, request))
  }

  def waitingForConnection(commander: ActorRef, request: HttpRequest): Receive = {
    case event: Http.Connected =>
      sender ! request
      context.become(waitingForResponse(commander))

    case Http.CommandFailed(Http.Connect(address, _, _, _, _)) =>
      commander ! Status.Failure(new RuntimeException("Client could not connect to: " + address))
      context.stop(self)
  }

  def waitingForResponse(commander: ActorRef): Receive = {
    case response@HttpResponse(status, entity, _, _) =>
      sender ! Http.Close
      context.become(waitingForClose(commander, response))

    case event@Http.SendFailed(_) =>
      commander ! Status.Failure(new RuntimeException("Client failed during sending: " + event))
      context.stop(self)

    case event@Timedout(_) =>
      commander ! Status.Failure(new RuntimeException("Client failed by timeout: " + event))
      context.stop(self)
  }

  def waitingForClose(commander: ActorRef, response: HttpResponse): Receive = {
    case event: Http.ConnectionClosed =>
      commander ! Status.Success(response)
      context.stop(self)

    case Http.CommandFailed(Http.Close) =>
      commander ! Status.Failure(new RuntimeException("Client could not close connection"))
      context.stop(self)
  }
}
