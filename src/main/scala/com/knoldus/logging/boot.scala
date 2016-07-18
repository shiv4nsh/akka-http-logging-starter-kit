package com.knoldus.logging


import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory


object StartApplication extends App {
  StartApp
}

object StartApp {
  implicit val system: ActorSystem = ActorSystem("Knoldus-Routing-service")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()
  val server = new KnoldusRoutingServer()
  val config = ConfigFactory.load()
  val serverUrl = config.getString("http.interface")
  val port = config.getInt("http.port")
  server.startServer(serverUrl, port)
}