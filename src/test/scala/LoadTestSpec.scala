import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import zensoft.LoadTest

class LoadTestSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  it should "sends messages to Influx" in {
    LoadTest.run("influx", 10000)
  }

}
