

package com.knoldus.logging.routes


import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class KnoldusRoutingServicesSpec extends WordSpec with Matchers with ScalatestRouteTest with KnoldusRoutingService {

  "The service" should {

    "to be able to log the request in green color" in {
      Get("/lessTime") ~> routes ~> check {
        responseAs[String].contains("Hey this request has been logged with green Color:)") shouldEqual true
      }
    }

    "to be able to log the request in red color" in {
      Get("/moreTime") ~> routes ~> check {
        responseAs[String].contains("Hey this request has been logged with red Color :)") shouldEqual true
      }
    }
  }
}
