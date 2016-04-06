package demo

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.Actor
import akka.event.LoggingReceive
import spray.routing._

class RestServerActor extends Actor with RestServer {

  def actorRefFactory = context

  def receive = LoggingReceive {
    runRoute(route)
  }
}

trait RestServer extends HttpService {

  import MessageJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val ids: AtomicInteger = new AtomicInteger(0)

  val route =
    path("basic") {
      get {
        complete(Message("basic", ids.getAndIncrement()))
      }
    }
}

