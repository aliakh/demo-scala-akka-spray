package demo

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("basic-actor-system")

  val restServer = system.actorOf(Props[RestServerActor])

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(restServer, interface = "localhost", port = 9001)
}