package Server.Controller

import akka.actor.{ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import slick.jdbc.MySQLProfile
import slick.jdbc.MySQLProfile.api._

object DBExtension
  extends ExtensionId[DBExtension]
    with ExtensionIdProvider {

  override def lookup: DBExtension.type = DBExtension

  override def createExtension(system: ExtendedActorSystem) = new DBExtension

  override def get(system: ActorSystem): DBExtension = super.get(system)
}

class DBExtension extends Extension {
  val hdb: MySQLProfile.backend.Database = Database.forConfig("mysql")
}