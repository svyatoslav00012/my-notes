package services.impl

import com.redis.RedisClient
import monix.eval.Task
import services.Cache

import javax.inject.{Inject, Singleton}

@Singleton
class RedisCacheImpl @Inject()(redisClient: RedisClient) extends Cache {
  override def set[T](key: String, value: T): Task[Boolean] = Task.eval {
    redisClient.set(key, value)
  }

  override def get(key: String): Task[Option[String]] = Task {
    redisClient.get(key)
  }
}
