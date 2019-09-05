package edu.xust.bigdata.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtil {
    private static JedisPool jedisPool;

    static {
        jedisPool = new JedisPool("node1", 6379);
    }

    public static Jedis getJedisConn(){
        return jedisPool.getResource();
    }
}
