package demo

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object Boot extends App {

  implicit val system = ActorSystem("supervisor-actor-system")

  val restServerActor = system.actorOf(Props[RestServerActor], "rest-server-actor")

  implicit val timeout = Timeout(5.seconds)

  IO(Http) ? Http.Bind(restServerActor, interface = "localhost", port = 9004)

  sys.addShutdownHook(system.terminate())
}