import java.util.concurrent.TimeUnit

import Server.Controller.DBExtension
import Server.HttpRoute
import Server.Models._
import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "COFFEES") {
  def name = column[String]("COF_NAME", O.PrimaryKey)

  def supID = column[Int]("SUP_ID")

  def price = column[Double]("PRICE")

  def sales = column[Int]("SALES", O.Default(0))

  def total = column[Int]("TOTAL", O.Default(0))

  def * = (name, supID, price, sales, total)
}

object CoffeesRepo {
  private val coffees = TableQuery[Coffees]

  def schema = coffees.schema

}

object WebServer {

  implicit val system: ActorSystem = ActorSystem("actorSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  def main(args: Array[String]) {
    val hdbExt = DBExtension(system).hdb

    val log = Logging(system.eventStream, "logger")

    val conf: Config = ConfigFactory.load()

//    val create = (ProviderRepo.schema).create
//    hdbExt.run(create)

    implicit val timeout: Timeout = Timeout(5, TimeUnit.SECONDS)

    val bindingFuture = Http().bindAndHandle(HttpRoute().route, conf.getString("bind-interface"), conf.getInt("bind-port"))
    log.info(s"Server online at http://" + conf.getString("bind-interface") + ":" + conf.getString("bind-port") + "/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }

}