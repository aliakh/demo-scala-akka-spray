package demo

import akka.actor.{Actor, Props}
import akka.event.Logging
import demo.WorkerActor.Work

import scala.util.Random

class WorkerActor(id: Int) extends Actor {

  implicit val system = context.system
  val log = Logging(system, getClass)

  def receive = {
    case Work =>
      if (Random.nextInt(3) == 0) {
        throw new ArithmeticException(s"worker $id mistook")
      } else if (Random.nextInt(7) == 0) {
        throw new NullPointerException(s"worker $id failed")
      } else {
        context.parent ! new Message(s"worker $id", id)
      }
  }

  override def preRestart(reason: Throwable, message: Option[Any]) {
    log.info("restart worker {} after '{}' with {}", id, reason, message)
    message foreach { self forward _ }
  }
}

object WorkerActor {

  case class Work()

  def props(id: Int): Props = Props(new WorkerActor(id))
}