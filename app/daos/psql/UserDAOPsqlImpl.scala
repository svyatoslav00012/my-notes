package daos.psql

import daos.UserDAO
import daos.psql.generic.Tables
import models.User
import monix.eval.Task
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UserDAOPsqlImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                           (implicit ex: ExecutionContext) extends Tables(dbConfigProvider)
  with UserDAO
  with HasDatabaseConfigProvider[JdbcProfile] {

  import daos.helpers.Helpers._
  import profile.api._

  private val usersQuery = users returning users

  override def getById(userId: Long): Task[Option[User]] =
    db.run(users.filter(_.id === userId).result.headOption).wrapEx

  override def getAll: Task[Seq[User]] = db.run(users.result).wrapEx

  override def create(user: User): Task[User] = db.run(usersQuery += user).wrapEx

  override def update(user: User): Task[Unit] = db.run(
    users.filter(_.id === user.id).update(user).checkRowsAffected
  ).wrapEx.map(_ => Unit)

  override def delete(id: Long): Task[Unit] = db.run(
    users.filter(_.id === id).delete.checkRowsAffected
  ).wrapEx.map(_ => Unit)

  override def getByEmail(email: String): Task[Option[User]] =
    db.run(users.filter(_.email === email).result.headOption).wrapEx

}
