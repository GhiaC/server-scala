package Server.Models

import Server.Models.OrderRepo.TModel
import Server.Models.UserRepo.getRandomInt
import slick.dbio.{Effect, NoStream}
import slick.jdbc
import slick.jdbc.MySQLProfile
import slick.lifted.TableQuery
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait RepoHelper {
  type T
  type TModel

  protected val instance = TableQuery[T]

  def schema: jdbc.MySQLProfile.SchemaDescription

  def create(s: TModel): FixedSqlAction[Int, NoStream, Effect.Write]

  def exists(id: Int): FixedSqlAction[Boolean, jdbc.MySQLProfile.api.NoStream, Effect.Read]

  def get(id: Int): FixedSqlStreamingAction[Seq[TModel], _, Effect.Read]

  private def getRandomInt: Int = scala.util.Random.nextInt(100000)

  def getUniqueId(implicit hdb: MySQLProfile.backend.Database, ex: ExecutionContextExecutor): Future[Int] = {
    val id = getRandomInt
    hdb.run(this exists id) flatMap { i =>
      if (i) getUniqueId else Future(id)
    }
  }
}
