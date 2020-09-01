package alyona.bestorm.storage;

import java.util.concurrent.ConcurrentHashMap;
import redis.clients.jedis.Jedis;

public class RedisStorageProvider implements IRedisStorageProvider {
  
  private final Jedis jedis;
  private final ConcurrentHashMap<Class<?>, RedisStorage<?,?>> storages = new ConcurrentHashMap<>();

  public RedisStorageProvider(Jedis jedis){
    this.jedis = jedis;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Data, Key> IRedisStorage<Data, Key> getStorage(Class<Data> dataClass,
      Class<Key> keyClass) {
    return (IRedisStorage<Data, Key>) storages.computeIfAbsent(dataClass,
        dataClassParam -> new RedisStorage<>(jedis, dataClassParam, keyClass, this));
  }

}