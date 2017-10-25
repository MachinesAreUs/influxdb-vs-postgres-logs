package zensoft.model

import zensoft.model.LogEntry.Status

case class LogEntry (
  batchId           : String,
  uuid              : String,
  email             : String,
  files             : List[String],
  status            : Status,
  passwordGenerated : Boolean,
  id                : Long = 0
)

object LogEntry {

  sealed abstract class Status

  case object RECEIVED  extends Status
  case object VALIDATED extends Status
  case object PACKAGED  extends Status
  case object UPLOADED  extends Status

  val statuses = Seq(
    RECEIVED,
    VALIDATED,
    PACKAGED,
    UPLOADED
  )
}