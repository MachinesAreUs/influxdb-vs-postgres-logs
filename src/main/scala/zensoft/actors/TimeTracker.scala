package zensoft.actors

import akka.actor.{Actor, ActorLogging}
import zensoft.actors.TimeTracker.{StartTracking, UpdateTracking}

object TimeTracker {
  case class StartTracking(long: Long, num: Int)
  case class UpdateTracking(long: Long)
}

class TimeTracker extends Actor with ActorLogging {

  // time tracking
  var start:      Long = _
  var lastUpdate: Long = _

  // item count
  var expected: Int = 0
  var count:    Int = 0

  override def receive : Receive = {
    case StartTracking(timestamp, num) =>
      start    = timestamp
      expected = num
      reportStarted()
    case UpdateTracking(timestamp) =>
      count += 1
      if (timestamp > lastUpdate) lastUpdate = timestamp
      if (count == expected) reportFinished()
  }

  private def reportStarted() = {
    Console.println(s"started: ${start}")
  }

  private def reportFinished() = {
    Console.println(s"Finished: ${lastUpdate}")
    val totalTime: Double = (lastUpdate.toDouble - start.toDouble) / 1000.toDouble
    Console.println(s"Total time: ${totalTime} secs")
  }
}
