package Server.Models

import slick.dbio.Effect
import slick.jdbc.MySQLProfile.api._
import slick.sql.FixedSqlAction

final case class OrderModel(id: Int, user_id: Int,
                            transaction_id: Int,
                            phonenumber: String,
                            long: String, lat: String,
                            description: String,
                            created_at: String)

class Order(tag: Tag) extends
  Table[OrderModel](tag, "ORDERS") {
  def id = column[Int]("ID", O.PrimaryKey)

  def user_id = column[Int]("USER_ID")

  def transaction_id = column[Int]("TRANSACTION_ID")

  def phonenumber = column[String]("PHONENUMBER")

  def long = column[String]("LONG")

  def lat = column[String]("LAT")

  def description = column[String]("DESCRIPTION")

  def created_at = column[String]("CREATED_AT")

  def * = (id, user_id, transaction_id, phonenumber, long, lat, description, created_at) <> (OrderModel.tupled, OrderModel.unapply)
}

object OrderRepo extends RepoHelper {
  override type T = Order
  override type TModel = OrderModel

  override protected val instance = TableQuery[T]

  override def schema = instance.schema

  override def create(s: TModel): FixedSqlAction[Int, NoStream, Effect.Write] = instance += s

  override def exists(id: Int) = instance.filter(_.id === id).exists.result

  override def get(id: Int) = instance.filter(_.id === id).result

}

