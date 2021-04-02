package services.helpers

import java.util.UUID

trait UUIDGenerator {
  def generate: UUID
}

object UUIDGenerator {
  implicit val defaultGenerator: UUIDGenerator = new UUIDGenerator {
    override def generate: UUID = UUID.randomUUID()
  }
}

