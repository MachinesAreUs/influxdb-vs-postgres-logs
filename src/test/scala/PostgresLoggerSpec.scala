import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers, WordSpecLike}
import zensoft.actors.loggers.PostgresLogger
import zensoft.model.LogEntry

class PostgresLoggerSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll {

  var actor: ActorRef = _

  override def beforeAll {
    val tracker = system.actorOf(TestActors.forwardActorProps(testActor), name = "tracker")

    actor = system.actorOf(
      props = Props(classOf[PostgresLogger], tracker),
      name = "postgres")
  }

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  it should "write a LogEntry to postgres" in {

    Thread.sleep(2000)

    val status = LogEntry.Status.PACKAGED
    val timestamp = System.currentTimeMillis()
    actor ! LogEntry("batch-2", "uuid-2", "a@b.z", List("file_x.txt"), status, true, timestamp)
  }
}
