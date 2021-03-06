package com.knoldus.logging

/**
  * Created by shivansh on 18/7/16.
  */

import akka.actor.ActorSystem
import akka.event.{LoggingAdapter, Logging}
import akka.event.Logging.LogLevel
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.RouteResult.Complete
import akka.stream.{Materializer, ActorMaterializer}
import com.knoldus.logging.routes.KnoldusRoutingService
import akka.http.scaladsl.server.directives.{LoggingMagnet, LogEntry, DebuggingDirectives}
import pl.project13.scala.rainbow.Rainbow
import scala.concurrent.ExecutionContext.Implicits.global
import Rainbow._

import scala.concurrent.ExecutionContext

class KnoldusRoutingServer(implicit val system: ActorSystem,
                            implicit val materializer: ActorMaterializer) extends KnoldusRoutingService {
  def startServer(address: String, port: Int) = {
    val loggedRoute = requestMethodAndResponseStatusAsInfo(Logging.InfoLevel, routes)
    Http().bindAndHandle(loggedRoute, address, port)
  }

  def requestMethodAndResponseStatusAsInfo(level: LogLevel, route: Route)
                                          (implicit m: Materializer, ex: ExecutionContext) = {

    def akkaResponseTimeLoggingFunction(loggingAdapter: LoggingAdapter, requestTimestamp: Long)(req: HttpRequest)(res: Any): Unit = {
      val entry = res match {
        case Complete(resp) =>
          val responseTimestamp: Long = System.currentTimeMillis()
          val elapsedTime: Long = responseTimestamp - requestTimestamp
          val loggingString = "Logged Request:" + req.method + ":" + req.uri + ":" + resp.status + ":" + elapsedTime
          val coloredLoggingString = if (elapsedTime > StartApp.thresholdValue) {
            loggingString.red
          } else {
            loggingString.green
          }
          LogEntry(coloredLoggingString, level)
        case anythingElse =>
          LogEntry(s"$anythingElse", level)
      }
      entry.logTo(loggingAdapter)
    }
    DebuggingDirectives.logRequestResult(LoggingMagnet(log => {
      val requestTimestamp = System.currentTimeMillis()
      akkaResponseTimeLoggingFunction(log, requestTimestamp)
    }))(route)

  }
}
