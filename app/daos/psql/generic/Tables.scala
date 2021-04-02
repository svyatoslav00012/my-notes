package daos.psql.generic

import controllers.utils.Exceptions.DbResultException
import models._
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import slick.lifted
import slick.lifted.ProvenShape

import java.time.Instant
import scala.concurrent.ExecutionContext


class Tables(dbConfigProvider: DatabaseConfigProvider) {
  val conf = dbConfigProvider.get[JdbcProfile]

  import conf.profile.api._

  implicit class SafeDB(action: conf.profile.ProfileAction[Int, NoStream, Effect.Write]) {
    def checkRowsAffected(implicit ex: ExecutionContext): DBIOAction[Int, NoStream, Effect.Write with Effect with Effect.Transactional] =
      action.flatMap { updatedRows =>
        if (updatedRows == 0) DBIO.failed(DbResultException)
        else DBIO.successful(updatedRows)
      }.transactionally
  }

  class UserTable(tag: Tag) extends Table[User](tag, "users") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def email: Rep[String] = column[String]("email")

    def password: Rep[String] = column[String]("password")

    def isEmailConfirmed: Rep[Boolean] = column[Boolean]("is_email_confirmed")

    def createdAt: Rep[Instant] = column[Instant]("created_at")

    def updatedAt: Rep[Instant] = column[Instant]("updated_at")

    def * : ProvenShape[User] =
      (id, email, password, isEmailConfirmed, createdAt, updatedAt) <> (User.tupled, User.unapply)
  }

  val users = lifted.TableQuery[UserTable]

  class TokenTable(tag: Tag) extends Table[Token](tag, "token") {
    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def tokenType: Rep[String] = column[String]("token_type")

    def userId: Rep[Long] = column[Long]("user_id")

    def token: Rep[String] = column[String]("token")

    def createdAt: Rep[Instant] = column[Instant]("created_at")

    def * : ProvenShape[Token] = (id, tokenType, userId, token, createdAt) <> (Token.tupled, Token.unapply)

  }

  val tokens = lifted.TableQuery[TokenTable]

}
