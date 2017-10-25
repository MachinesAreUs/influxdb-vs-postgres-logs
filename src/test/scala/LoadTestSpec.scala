import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import zensoft.LoadTest

class LoadTestSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  ignore should "sends messages to postgres" in {
    LoadTest.run("postgres", 10000)
    Thread.sleep(60000)
  }

  it should "sends messages to influx" in {
    LoadTest.run("influx", 10000)
    Thread.sleep(60000)
  }

}
