package daos

import monix.eval.Task

trait ModelDAO[T, ID] {
  def getAll: Task[Seq[T]]
  def getById(id: ID): Task[Option[T]]
  def create(model: T): Task[T]
  def update(model: T): Task[Unit]
  def delete(Id: ID): Task[Unit]
}
