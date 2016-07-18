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

  implicit def myExceptionHandler =
    ExceptionHandler {
      case e: ArithmeticException =>
        extractUri { uri =>
          complete(HttpResponse(StatusCodes.InternalServerError, entity = s"The requested resource is not found"))
        }
    }

  val routes: Route = {
    get {
      path("logMe") {
        complete {
          try {
            HttpResponse(StatusCodes.OK, entity = s"Hey this request has been logged :)")
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
