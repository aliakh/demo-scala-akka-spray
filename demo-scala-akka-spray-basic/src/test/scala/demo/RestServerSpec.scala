package demo

import demo.MessageJsonProtocol._
import org.scalatest.{FreeSpec, Matchers}
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.testkit.ScalatestRouteTest

class RestServerSpec extends FreeSpec with RestServer with ScalatestRouteTest with Matchers {

  def actorRefFactory = system

  "RestServer" - {
    "when calling GET /basic" - {
      "should return valid JSON" in {
        Get("/basic") ~> route ~> check {
          status shouldEqual OK
          entity.as[Message] shouldEqual Right(Message("basic", 0))
          entity.toString shouldEqual "HttpEntity(application/json; charset=UTF-8,{\n  \"payload\": \"basic\",\n  \"id\": 0\n})"
        }
      }
    }
  }
}
