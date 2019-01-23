package Server.Models

import Server.ServerHelper
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContextExecutor, Future}

final case class ServiceModel(id: Int, title: String, picture: String, deleted_at: Option[String] = None)

class Services(tag: Tag) extends
  Table[ServiceModel](tag, "SERVICES") {
  def id = column[Int]("ID", O.PrimaryKey)

  def title = column[String]("TITLE")

  def picture = column[String]("PICTURE")

  def deleted_at = column[Option[String]]("DELETED_AT", O.Default(None))

  def * = (id, title, picture, deleted_at) <> (ServiceModel.tupled, ServiceModel.unapply)
}

object ServicesRepo {
  private val services = TableQuery[Services]

  def create(s: ServiceModel) = services += s

  def getAll = services.filter(_.deleted_at.isEmpty).result

  def delete(id: Int) = services.filter(_.id === id).map(_.deleted_at).update(Some(ServerHelper.getCurrentTimeStamp))

  def update(id: Int, service: ServiceModel) = services.filter(_.id === id).update(service)

  def exists(id: Int) = services.filter(_.id === id).exists.result

  def schema = services.schema

  def getRandomInt: Int = scala.util.Random.nextInt(1000)

  def getUniqueId(implicit hdb: MySQLProfile.backend.Database, ex: ExecutionContextExecutor): Future[Int] = {
    val id = getRandomInt
    hdb.run(ServicesRepo exists id) flatMap { i =>
      if (i) getUniqueId else Future(id)
    }
  }
}

