package zensoft.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef}
import zensoft.actors.LogProducer.Produce
import zensoft.model.LogEntry
import zensoft.model.LogEntry.Status

import scala.util.Random

object LogProducer {
  case class Produce(n: Int)
}

class LogProducer(consumer: ActorRef) extends Actor with ActorLogging {

  override def receive : Receive = {
    case Produce(num) =>
      val batchId = randomBatchId()
      val logItems = for (_ <- 0 until num) yield newLogItem(batchId)
      for (itemWithIdx <- logItems.zipWithIndex)
        consumer ! itemWithIdx
  }

  def newLogItem(batchId: String): LogEntry = {
    LogEntry(
      batchId           = batchId,
      uuid              = randomUUID(),
      email             = randomEmail(),
      files             = randomFileNames(),
      status            = randomStatus(),
      passwordGenerated = randomPasswordGenerated()
    )
  }

  def randomBatchId(): String = {
    UUID.randomUUID().toString
  }

  def randomUUID(): String = {
    UUID.randomUUID().toString
  }

  def randomEmail(): String = {
    "agus@zensoft.mx"
  }

  def randomFileNames(): List[String] = {
    List("file1.txt", "file2.txt")
  }

  def randomStatus(): Status = {
    Random.shuffle(LogEntry.statuses).head
  }

  def randomPasswordGenerated(): Boolean = {
    Random.nextFloat() > 0.5
  }

}
