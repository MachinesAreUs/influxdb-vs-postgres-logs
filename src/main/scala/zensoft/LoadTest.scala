package zensoft

import akka.actor.{ActorSystem, Props}
import akka.routing.FromConfig
import zensoft.actors.LogProducer.Produce
import zensoft.actors.{LogProducer, TimeTracker}
import zensoft.actors.TimeTracker.StartTracking
import zensoft.actors.loggers.InfluxLogger

object LoadTest {

  def run(db: String, numEntries: Int) = {

    val system = ActorSystem(s"test-${db}")

    val tracker =
      system.actorOf(
        props = Props(classOf[TimeTracker]),
        name = "tracker")

    val influx =
      system.actorOf(
        props = FromConfig.props(Props(classOf[InfluxLogger], tracker)),
        name = "influx")

    val producer =
      system.actorOf(
        props = Props(classOf[LogProducer], influx),
        name = "producer")

    tracker ! StartTracking(System.currentTimeMillis(), numEntries)
    producer ! Produce(numEntries)
  }

}
