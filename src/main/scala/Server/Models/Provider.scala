package Server.Models

import Server.Models.UserRepo.getRandomInt
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContextExecutor, Future}


final case class ProviderModel(id: Int, service_id: Int,
                               title: String,
                               picture: String, start_time: String,
                               end_time: String, description: String,
                               phonenumber: String, stars: Int,
                               lat: String, long: String,
                               deleted_at: Option[String] = None)

class Provider(tag: Tag) extends
  Table[ProviderModel](tag, "PROVIDERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def service_id = column[Int]("SERVICE_ID")

  def title = column[String]("TITLE")

  def picture = column[String]("PICTURE")

  def start_time = column[String]("START_TIME")

  def end_time = column[String]("END_TIME")

  def description = column[String]("DESCRIPTION")

  def phonenumber = column[String]("PHONENUMBER")

  def stars = column[Int]("STARS")

  def lat = column[String]("LAT")

  def long = column[String]("LONG")

  def deleted_at = column[Option[String]]("DELETED_AT", O.Default(None))

  def * = (id, service_id, title, picture, start_time, end_time, description, phonenumber, stars, lat, long, deleted_at) <> (ProviderModel.tupled, ProviderModel.unapply)
}

object ProviderRepo {
  private val providers = TableQuery[Provider]

  def create(s: ProviderModel) = providers += s

  def get(id: Int) = providers.filter(_.id === id).result

  def searchByName(name: String) = providers.filter(_.title === name).result

  def searchByLocation(lat: String, long: String) = providers.filter(_.lat === lat).filter(_.long === long).result

  def getProviders(id: Int) = providers.filter(_.service_id === id).result

  def delete(id: Int) = providers.filter(_.id === id).delete

  def update(id: Int, salary: ProviderModel) = providers.filter(_.id === id).update(salary)

  def schema = providers.schema

  def exists(id: Int) = providers.filter(_.id === id).exists.result

  def getUniqueId(implicit hdb: MySQLProfile.backend.Database, ex: ExecutionContextExecutor): Future[Int] = {
    val id = getRandomInt
    hdb.run(ProviderRepo exists id) flatMap { i =>
      if (i) getUniqueId else Future(id)
    }
  }
}

