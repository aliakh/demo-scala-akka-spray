package demo

import akka.actor.{Actor, Props}
import akka.event.LoggingReceive
import spray.http.Uri
import spray.routing._

class RestServerActor extends Actor with RestServer {

  def actorRefFactory = context

  def receive = LoggingReceive {
    runRoute(route)
  }
}

trait RestServer extends HttpService {

  val uri = Uri("http://localhost:9001/basic")

  val route =
    path("router1") {
      requestContext =>
        val router = actorRefFactory.actorOf(Props(classOf[RouterActor1], uri, requestContext))
        router ! RouterActor1.Route()
    } ~
    path("router2") {
      requestContext =>
        val router = actorRefFactory.actorOf(Props(classOf[RouterActor2], uri, requestContext))
        router ! RouterActor2.Route()
    }
}