package zensoft

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import zensoft.actors.LogProducer.Produce
import zensoft.actors.{LogProducer, TimeTracker}
import zensoft.actors.TimeTracker.StartTracking
import zensoft.actors.loggers.{InfluxLogger, PostgresLogger}

object LoadTest {

  def run(db: String, numEntries: Int) = {

    val system = ActorSystem(s"test-${db}")

    val tracker =
      system.actorOf(
        props = Props(classOf[TimeTracker]),
        name = "tracker")

    val logger = db match {
      case "influx" =>
        system.actorOf(
          props = FromConfig.props(Props(classOf[InfluxLogger], tracker)),
          name = "influx")
      case "postgres" =>
        system.actorOf(
          props = FromConfig.props(Props(classOf[PostgresLogger], tracker)),
          name = "postgres")
    }

    val producer =
      system.actorOf(
        props = Props(classOf[LogProducer], logger),
        name = "producer")

    tracker ! StartTracking(System.currentTimeMillis(), numEntries)
    producer ! Produce(numEntries)
  }

}
