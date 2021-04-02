package daos.psql

import daos.{TokenDAO, UserDAO}
import daos.psql.generic.Tables
import models.Token
import monix.eval.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class TokenDAOPsqlImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                (implicit ex: ExecutionContext) extends Tables(dbConfigProvider)
  with TokenDAO
  with HasDatabaseConfigProvider[JdbcProfile] {

  import daos.helpers.Helpers._
  import profile.api._

  private val tokensQuery = tokens returning tokens

  override def getById(userId: Long): Task[Option[Token]] =
    db.run(tokens.filter(_.id === userId).result.headOption).wrapEx

  override def getAll: Task[Seq[Token]] = db.run(tokens.result).wrapEx

  override def create(token: Token): Task[Token] = db.run(tokensQuery += token).wrapEx

  override def update(token: Token): Task[Unit] = db.run(
    tokens.filter(_.id === token.id).update(token).checkRowsAffected
  ).wrapEx.map(_ => Unit)

  override def delete(id: Long): Task[Unit] = db.run(
    tokens.filter(_.id === id).delete.checkRowsAffected
  ).wrapEx.map(_ => Unit)

  override def getByTokenStringAndType(token: String, tokenType: String): Task[Option[Token]] =
    db.run(tokens.filter(t => t.token === token && t.tokenType === tokenType).result.headOption).wrapEx

  override def getByUserId(userId: Long): Task[Seq[Token]] =
    db.run(tokens.filter(_.userId === userId).result).wrapEx
}