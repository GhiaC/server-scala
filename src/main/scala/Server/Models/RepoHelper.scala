package Server.Models

import Server.Models.OrderRepo.TModel
import slick.dbio.{Effect, NoStream}
import slick.jdbc
import slick.jdbc.MySQLProfile
import slick.lifted.TableQuery
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait RepoHelper {
  type T
  type TModel

  def schema: jdbc.MySQLProfile.SchemaDescription

  def create(s: TModel): FixedSqlAction[Int, NoStream, Effect.Write]

  def exists(id: Int): FixedSqlAction[Boolean, jdbc.MySQLProfile.api.NoStream, Effect.Read]

  def get(id: Int): FixedSqlStreamingAction[Seq[TModel], _, Effect.Read]

  def getRandomInt: Int = scala.util.Random.nextInt(100000)
}
