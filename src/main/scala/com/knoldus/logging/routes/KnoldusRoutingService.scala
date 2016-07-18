package com.knoldus.logging.routes

import java.util.UUID

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer


/**
  *
  * author: Shivansh
  * mail: shivansh@knoldus.com
  *
  */

trait KnoldusRoutingService {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  val logger = Logging(system, getClass)

  val TIME_ELAPSED = 10000
  implicit def myExceptionHandler =
    ExceptionHandler {
      case e: ArithmeticException =>
        extractUri { uri =>
          complete(HttpResponse(StatusCodes.InternalServerError, entity = s"The requested resource is not found"))
        }
    }

  val routes: Route = {
    get {
      path("lessTime") {
        complete {
          try {
            HttpResponse(StatusCodes.OK, entity = s"Hey this request has been logged with green Color:)")
          }
          catch {
            case ex: Throwable =>
              logger.error(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError, entity = s"Error found")
          }
        }
      }
    } ~ get {
      path("moreTime") {
        complete {
          try {
            Thread.sleep(TIME_ELAPSED)
            HttpResponse(StatusCodes.OK, entity = s"Hey this request has been logged with red Color :)")
          }
          catch {
            case ex: Throwable =>
              logger.error(ex, ex.getMessage)
              HttpResponse(StatusCodes.InternalServerError, entity = s"Error found")
          }
        }
      }
    }
  }
}
