package Server

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar

import Server.Models.UserRepo
import slick.jdbc.{H2Profile, MySQLProfile}

import scala.concurrent.{ExecutionContextExecutor, Future}

object ServerHelper {
  def getCurrentTimeStamp: String = {
    val today = Calendar.getInstance.getTimeInMillis
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date: String = df.format(today.toLong)
    val re = Timestamp.valueOf(date)
    re toString
  }

}
