package demo

import akka.event.LoggingReceive
import spray.http.Uri
import spray.routing._

class RestServerActor extends HttpServiceActor {

  val settings = Settings(context.system)
  val uri = Uri(settings.SERVER_URL)

  def receive = LoggingReceive {
    runRoute(route)
  }

  val route =
    path("aggregator1") {
      requestContext =>
        val aggregator = actorRefFactory.actorOf(AggregatorActor1.props(uri, requestContext))
        aggregator ! AggregatorActor1.Aggregate()
    } ~
    path("aggregator2") {
      requestContext =>
        val aggregator = actorRefFactory.actorOf(AggregatorActor2.props(uri, requestContext))
        aggregator ! AggregatorActor2.Aggregate()
    }
}
