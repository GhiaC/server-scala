package Server.Models

import Server.ServerHelper
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContextExecutor, Future}

final case class UserModel(id: Int, username: String, password: String, created_at: String, deleted_at: Option[String] = None)

class User(tag: Tag) extends
  Table[UserModel](tag, "USERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def username = column[String]("USERNAME")

  def password = column[String]("PASSWORD")

  def created_at = column[String]("CREATED_AT")

  def deleted_at = column[Option[String]]("DELETED_AT", O.Default(None))

  def * = (id, username, password, created_at, deleted_at) <> (UserModel.tupled, UserModel.unapply)
}

object UserRepo {
  private val users = TableQuery[User]

  def create(u: UserModel) = users += u

  def exists(id: Int) = users.filter(_.id === id).exists.result

  def find(id: Int) = users.filter(_.id === id).result

  def delete(id: Int) = users.filter(_.id === id).map(_.deleted_at).update(Some(ServerHelper.getCurrentTimeStamp))

  def getAll = users.result

  def schema = users.schema

  def getRandomInt: Int = scala.util.Random.nextInt(1000)

  def getUniqueId(implicit hdb: MySQLProfile.backend.Database, ex: ExecutionContextExecutor): Future[Int] = {
    val id = getRandomInt
    hdb.run(UserRepo exists id) flatMap { i =>
      if (i) getUniqueId else Future(id)
    }
  }
}