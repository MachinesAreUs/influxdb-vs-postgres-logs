import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import scalikejdbc._


class ScalaLikeSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  it should "create schema" in {

    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton("jdbc:postgresql://localhost/hermes_perftest", "postgres", "")

    implicit val session = AutoSession

    sql"""
      DROP INDEX IF EXISTS task_log_example_idx_batch_id;
      DROP TABLE IF EXISTS task_log_example;

      CREATE TABLE task_log_example (
        id           serial not null primary key,
        batch_id     varchar(32),
        uuid         varchar(32),
        email        varchar(50),
        files        varchar(100),
        status       varchar(20),
        password_gen boolean,

        timestamp timestamp not null
      );

      CREATE INDEX task_log_example_idx_batch_id on task_log_example (batch_id);
    """.execute.apply()

    sql"INSERT INTO task_log_example (batch_id, uuid, email, files, status, password_gen, timestamp) VALUES ('batch-1', 'uuid-1', 'a@b.c', 'file1.txt', 'DELIVERED', false, current_timestamp)".update.apply()

    sql"""
      DROP TABLE task_log_example;
    """.execute.apply()
  }

}
