package Server

import Server.Controller.DBExtension
import Server.Models._
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer {

  implicit val system: ActorSystem = ActorSystem("actorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]) {
    val hdbExt = DBExtension(system).hdb

    val log = Logging(system.eventStream, "logger")

    val conf: Config = ConfigFactory.load()

    hdbExt.run(UserRepo.schema.create)

    val bindingFuture = HttpRoute().start()

    log.info(s"Server online at http://" + conf.getString("bind-interface") + ":" + conf.getString("bind-port") + "/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }

}