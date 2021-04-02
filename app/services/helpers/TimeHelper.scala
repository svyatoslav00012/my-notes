package services.helpers

import java.time.Instant

trait TimeHelper {
  def now: Instant
}

object TimeHelper {
  implicit val defaultTimeHelper: TimeHelper = new TimeHelper {
    override def now: Instant = Instant.now()
  }
}
