import com.google.inject.AbstractModule
import com.redis.RedisClient
import daos.mongo.NoteDAOMongoImpl
import daos.psql.{TokenDAOPsqlImpl, UserDAOPsqlImpl}
import daos.{NoteDAO, TokenDAO, UserDAO}
import daos.redis.{NoteDAORedisImpl, TokenDAORedisImpl, UserDAORedisImpl}
import monix.execution.Scheduler
import services.{AuthService, Cache, MailerService, NoteService, UserService}
import services.impl.{AuthServiceImpl, MailerServiceDumbImpl, NoteServiceImpl, RedisCacheImpl, UserServiceImpl}



class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[RedisClient]).toInstance(new RedisClient())
    bind(classOf[Scheduler]).toInstance(Scheduler.global)

    bind(classOf[AuthService]).to(classOf[AuthServiceImpl])
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
    bind(classOf[NoteService]).to(classOf[NoteServiceImpl])

    bind(classOf[Cache]).to(classOf[RedisCacheImpl])
    bind(classOf[MailerService]).to(classOf[MailerServiceDumbImpl])

    bind(classOf[UserDAO]).to(classOf[UserDAOPsqlImpl])
    bind(classOf[TokenDAO]).to(classOf[TokenDAOPsqlImpl])
    bind(classOf[NoteDAO]).to(classOf[NoteDAOMongoImpl])
  }

}
