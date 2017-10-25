package zensoft

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class LoadTestSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  ignore should "sends messages to postgres" in {
    LoadTest.run("postgres", 100) // Change this parameter
  }

  ignore should "sends messages to influx" in {
    LoadTest.run("influx", 100) // Change this parameter
  }
}
