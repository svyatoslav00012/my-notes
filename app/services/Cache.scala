package services

import monix.eval.Task


trait Cache {
  def set[T](key: String, value: T): Task[Boolean]

  def get(key: String): Task[Option[String]]
}
