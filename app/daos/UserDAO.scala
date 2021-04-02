package daos

import models.User
import monix.eval.Task

trait UserDAO extends ModelDAO[User, Long] {
  def getByEmail(email: String): Task[Option[User]]
}
