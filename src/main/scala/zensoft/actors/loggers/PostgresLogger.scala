package zensoft.actors.loggers

import java.sql.Timestamp

import akka.actor.{Actor, ActorLogging, ActorRef}
import scalikejdbc._
import zensoft.actors.TimeTracker.UpdateTracking
import zensoft.model.LogEntry

class PostgresLogger(tracker: ActorRef) extends Actor with ActorLogging {

  val db_url      = "jdbc:postgresql://localhost/hermes_perftest"
  val db_username = "postgres"
  val db_password = ""

  Class.forName("org.postgresql.Driver")

  val settings = ConnectionPoolSettings(
    initialSize             = 20,
    maxSize                 = 20,
    connectionTimeoutMillis = 3000L,
    validationQuery         = "select 1 from log_entry")

  ConnectionPool.add('default, db_url, db_username, db_password)

  override def receive : Receive = {
    case logEntry: LogEntry =>
      write2Postgres(logEntry)
    case x =>
      log.warning(s"Received unknown message: ${x}")
  }

  private def write2Postgres(le: LogEntry) = {

    DB localTx {
      implicit s =>

        val timestamp = new Timestamp(le.timestamp)
        val files = le.files.mkString("|")
        sql"insert into log_entry (batch_id, uuid, email, files, status, password_gen, timestamp) values(${le.batchId}, ${le.uuid}, ${le.email}, ${files}, ${le.status}, ${le.passwordGen}, ${timestamp})".update.apply()

//        applyExecute {
//          val lec = LogEntry.column
//          val timestamp = new Timestamp(le.timestamp)
//
//          insert
//            .into(LogEntry)
//            .columns(lec.batchId, lec.uuid, lec.email, lec.files, lec.status, lec.passwordGen, lec.timestamp)
//            .values(le.batchId, le.uuid, le.email, le.files, le.status, le.passwordGen, timestamp).returningId
//        }
    }

    tracker ! UpdateTracking(System.currentTimeMillis())
  }
}
