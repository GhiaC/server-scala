package Server.Models

import Server.ServerHelper
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContextExecutor, Future}

final case class UserModel(id: Int, username: String, password: String, token: String, created_at: String, deleted_at: Option[String] = None)

final case class UserInfo(id: Int, username: String)

class User(tag: Tag) extends
  Table[UserModel](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def username = column[String]("USERNAME")

  def password = column[String]("PASSWORD")

  //  def name = column[String]("NAME")
  //
  //  def phonenumber = column[String]("PHONENUMBER")
  //
  //  def city = column[String]("CITY")
  //
  //  def address = column[String]("ADDRESS")
  //
  //  def lat = column[String]("LAT")
  //
  //  def long = column[String]("LONG")

  def token = column[String]("TOKEN")

  def created_at = column[String]("CREATED_AT")

  def deleted_at = column[Option[String]]("DELETED_AT", O.Default(None))

  def * = (id, username, password, token, created_at, deleted_at) <> (UserModel.tupled, UserModel.unapply)
}


object UserRepo extends RepoHelper {
  override type T = User
  override type TModel = UserModel

  val instance = TableQuery[T]

  def getUniqueId(implicit hdb: MySQLProfile.backend.Database, ex: ExecutionContextExecutor): Future[Int] = {

    val id = getRandomInt
    hdb.run(this exists id) flatMap { i =>
      if (i) getUniqueId else Future(id)
    }
  }


  override def schema = instance.schema

  override def create(u: TModel) = instance += u

  override def exists(id: Int) = instance.filter(_.id === id).exists.result

  override def get(id: Int) = instance.filter(_.id === id).result

  def get(token: String) = instance.filter(_.token === token).result

  def login(username: String, password: String) =
    instance.filter(user => user.username === username && user.password === password).map(_.id).result

  def generateToken = scala.util.Random.alphanumeric.take(50).mkString

  def retoken(username: String, password: String, token: String) =
    instance.filter(user => user.username === username
      && user.password === password).map(user => (user.token)).update(token)


  def exists(username: String) = instance.filter(_.username === username).exists.result

  def getAll = instance.result

}