package demo

import akka.actor.SupervisorStrategy._
import akka.actor.{OneForOneStrategy, _}
import akka.event.Logging
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import demo.MessagesJsonProtocol._
import demo.SupervisorActor.Supervise
import demo.WorkerActor.Work
import spray.http.MediaTypes._
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._

import scala.concurrent.duration._

class SupervisorActor(commander: ActorRef) extends Actor {

  import context._

  implicit val system = context.system
  val log = Logging(system, getClass)

  var workerId = 0

  val size = 3
  var messages = List[Message]()

  override def supervisorStrategy = OneForOneStrategy() {
    case _: ArithmeticException => Restart
    case _: NullPointerException => Stop
    case _: Exception => Escalate
  }

  def createWorker: ActorRef = {
    val worker = context.actorOf(WorkerActor.props(workerId), "worker-actor-" + workerId)
    workerId = workerId + 1
    context.watch(worker)
    worker
  }

  var router = {
    val routees = Vector.fill(size) {
      ActorRefRoutee(createWorker)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def receive = {
    case Supervise() =>
      setReceiveTimeout(1.second)
      for (id <- 1 to size) router.route(Work, sender())

    case message: Message =>
      messages = message :: messages
      if (messages.size == size) {
        marshal(Messages(messages.toArray)) match {
          case Right(messagesEntity) => commander ! HttpResponse(entity = messagesEntity)
          case Left(error) => commander ! HttpResponse(entity = HttpEntity(`text/plain`, "Supervisor failed: " + error.toString))
        }
        context.stop(self)
      }

    case Terminated(ref) =>
      log.info("start new worker after stopping {}", ref)

      router = router.removeRoutee(ref)
      val worker = createWorker
      router = router.addRoutee(worker)

      worker ! Work

    case ReceiveTimeout =>
      commander ! HttpResponse(entity = HttpEntity(`text/plain`, "Supervisor failed by receive timeout"))
      context.stop(self)
  }
}

object SupervisorActor {

  case class Supervise()

  def props(commander: ActorRef): Props = Props(new SupervisorActor(commander))
}