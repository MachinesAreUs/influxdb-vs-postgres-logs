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
    case LogEntry(batchId, uuid, email, files, status, passwordGen, timestamp, _) =>
      write2Influx(batchId, uuid, email, files, status, passwordGen, timestamp)
  }

  private def write2Influx(batchId: String,
                           uuid: String,
                           email: String,
                           files: List[String],
                           status: String,
                           passwordGen: Boolean,
                           timestamp: Long) = {
    val point = Point("tenant")
      .addTag("batchId", batchId)
      .addTag("uuid", uuid)
      .addTag("email", email)
      .addTag("files", files.mkString("|"))
      .addTag("passwordGenerated", passwordGen.toString)
      .addField("status", status)
      .addField("timestamp", timestamp)

    database.write(point)

    tracker ! UpdateTracking(System.currentTimeMillis())
  }
}
