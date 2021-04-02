package daos

import models.Token
import monix.eval.Task

trait TokenDAO extends ModelDAO[Token, Long] {
  def getByTokenStringAndType(token: String, tokenType: String): Task[Option[Token]]
  def getByUserId(userId: Long): Task[Seq[Token]]
}
