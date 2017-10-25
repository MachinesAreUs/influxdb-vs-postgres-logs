package zensoft.actors

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef}
import zensoft.actors.LogProducer.Produce
import zensoft.model.LogEntry

import scala.util.Random

object LogProducer {
  case class Produce(n: Int)
}

class LogProducer(consumer: ActorRef) extends Actor with ActorLogging {

  override def receive : Receive = {
    case Produce(num) =>
      val batchId = randomBatchId()
      val logItems = for (_ <- 0 until num) yield newLogItem(batchId)
      for (item <- logItems)
        consumer ! item
  }

  def newLogItem(batchId: String): LogEntry = {
    LogEntry(
      batchId     = batchId,
      uuid        = randomUUID(),
      email       = randomEmail(),
      files       = randomFileNames(),
      status      = randomStatus(),
      passwordGen = randomPasswordGenerated(),
      timestamp   = System.currentTimeMillis()
    )
  }

  def randomBatchId(): String = {
    UUID.randomUUID().toString
  }

  def randomUUID(): String = {
    UUID.randomUUID().toString
  }

  def randomEmail(): String = {
    val emails = List(
      "agus_1@zensoft.mx",
      "agus_2@zensoft.mx",
      "agus_3@zensoft.mx",
      "agus_4@zensoft.mx",
      "agus_5@zensoft.mx"
    )
    Random.shuffle(emails).head
  }

  def randomFileNames(): List[String] = {
    List("file1.txt", "file2.txt")
  }

  def randomStatus(): String = {
    Random.shuffle(LogEntry.STATUSES).head
  }

  def randomPasswordGenerated(): Boolean = {
    Random.nextFloat() > 0.5
  }

}
