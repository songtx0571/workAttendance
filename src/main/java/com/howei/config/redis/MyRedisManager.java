package com.howei.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
//@ConfigurationProperties(value="jedis.pool")
public class MyRedisManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    volatile static MyRedisManager redisSingleton;

    private String host;
    private int port;
    private int expire;
    private int timeout;
    private String password;
    private static JedisPool jedisPool = null;
    //第几个仓库
    private String database;

    public String getDatabase() {
        if(null == database || "".equals(database)) return "0";
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public MyRedisManager() {

    }

    public static MyRedisManager getRedisSingleton() {
        if(redisSingleton == null) {
            synchronized (MyRedisManager.class) {
                if(redisSingleton == null) return new MyRedisManager();
            }
        }
        return redisSingleton;
    }

    public void init() {
        if (jedisPool == null) {
            if (password != null && !"".equals(password)) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
            } else if (timeout != 0) {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
            } else {
                jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(Integer.parseInt(getDatabase()));
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("Jedis set 异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(Integer.parseInt(getDatabase()));
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("Jedis get 异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value, long time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(Integer.parseInt(getDatabase()));
            jedis.set(key, value);
            jedis.set(key, value, "XX", "EX", time);
        } catch (Exception e) {
            logger.error("Jedis set 异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Long expire(String key,int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(Integer.parseInt(getDatabase()));
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            logger.error("Jedis expire 异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Boolean exist(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(Integer.parseInt(getDatabase()));
            return jedis.exists(key);
        } catch (Exception e) {
            logger.error("Jedis exist 异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
