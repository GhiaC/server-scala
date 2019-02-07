package Server.Models

import Server.ServerHelper
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContextExecutor, Future}

final case class UserModel(id: Int, username: String, password: String
                           , created_at: String, deleted_at: Option[String] = None)

final case class UserInfo(id: Int, username: String)

class User(tag: Tag) extends
  Table[UserModel](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def username = column[String]("USERNAME")

  def password = column[String]("PASSWORD")

  def name = column[String]("NAME")

  def phonenumber = column[String]("PHONENUMBER")

  def city = column[String]("CITY")

  def address = column[String]("ADDRESS")

  def lat = column[String]("LAT")

  def long = column[String]("LONG")

  def token = column[String]("TOKEN")

  def created_at = column[String]("CREATED_AT")

  def deleted_at = column[Option[String]]("DELETED_AT", O.Default(None))

  def * = (id, username, password, created_at, deleted_at) <> (UserModel.tupled, UserModel.unapply)
}

object UserRepo extends RepoHelper {
  override type T = User
  override type TModel = UserModel

  override def schema = instance.schema

  override def create(u: TModel) = instance += u

  override def exists(id: Int) = instance.filter(_.id === id).exists.result

  override def get(id: Int) = instance.filter(_.id === id).result

  def login(username: String, password: String) = instance.filter(user => user.username === username && user.password === password).map(_.id).result

  def exists(username: String) = instance.filter(_.username === username).exists.result

  def getAll = instance.result

}