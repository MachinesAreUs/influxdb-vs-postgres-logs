package zensoft.actors.loggers

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.paulgoldbaum.influxdbclient._
import zensoft.actors.TimeTracker.UpdateTracking
import zensoft.model.LogEntry

import scala.concurrent.ExecutionContext.Implicits.global

class InfluxLogger(tracker: ActorRef) extends Actor with ActorLogging {

  val influx   = InfluxDB.connect("localhost", 8086)
  val database = influx.selectDatabase("perftest")

  override def receive : Receive = {
    case (LogEntry(batchId, uuid, email, files, status, passwordGenerated, _), idx: Int) =>
      write2Influx(idx, batchId, uuid, email, files, status, passwordGenerated)
  }

  private def write2Influx(idx: Int,
                           batchId: String,
                           uuid: String,
                           email: String,
                           files: List[String],
                           status: LogEntry.Status,
                           passwordGenerated: Boolean) = {
    val point = Point("tenant")
      .addTag("batchId", batchId)
      .addTag("uuid", uuid)
      .addTag("email", email)
      .addTag("files", files.mkString("|"))
      .addTag("passwordGenerated", passwordGenerated.toString)
      .addField("status", status.toString)

    database.write(point)

    tracker ! UpdateTracking(System.currentTimeMillis())
  }
}
