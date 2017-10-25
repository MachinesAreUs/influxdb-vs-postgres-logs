package zensoft.model
import scalikejdbc._

case class LogEntry (
  batchId     : String,
  uuid        : String,
  email       : String,
  files       : List[String],
  status      : String,
  passwordGen : Boolean,
  timestamp   : Long,
  id          : Long = 0
)

object LogEntry extends SQLSyntaxSupport[LogEntry] {

  object Status {
    val RECEIVED  = "RECEIVED"
    val VALIDATED = "VALIDATED"
    val PACKAGED  = "PACKAGED"
    val UPLOADED  = "UPLOADED"
  }

  val STATUSES = Seq(
    "RECEIVED",
    "VALIDATED",
    "PACKAGED",
    "UPLOADED"
  )

  // Persistence

  override val tableName = "log_entry"

  def apply(log: ResultName[LogEntry])(rs: WrappedResultSet) =
    new LogEntry(rs.string(log.batchId), rs.string(log.uuid), rs.string(log.email), List(rs.string(log.files)),
      rs.string(log.status), rs.boolean(log.passwordGen), rs.long(log.timestamp), rs.long(log.id))
}