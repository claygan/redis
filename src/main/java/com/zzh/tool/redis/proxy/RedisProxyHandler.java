package com.zzh.tool.redis.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import com.zzh.tool.redis.Redis;
import com.zzh.tool.redis.RedisDispatch;
import com.zzh.tool.redis.impl.RedisDispatchImpl;
import com.zzh.tool.redis.impl.RedisPoolFactory;

public class RedisProxyHandler implements InvocationHandler {

	private RedisDispatch redisDispatch;

	private RedisPoolFactory redisPoolFactory;

	public RedisProxyHandler(RedisPoolFactory redisPoolFactory) {
		this.redisPoolFactory = redisPoolFactory;
		// 初始化redis数据分发器
		redisDispatch = new RedisDispatchImpl(redisPoolFactory);
	}

	@SuppressWarnings("serial")
	private static Set<String> READABLE = new HashSet<String>() {
		{
			// 获得键所对应的值
			add("get");

			// 模糊查询所有key值
			add("keys");

			// 判断key是否存在
			add("exists");

			// 返回某个key元素的数据类型 ( none:不存在,string:字符,list,set,zset,hash)
			add("type");

			// 查找某个key还有多长时间过期,返回时间秒
			add("ttl");

			// Returns the bit value at offset in the string value stored at key
			add("getbit");

			// Get a substring of the string stored at a key
			add("getrange");

			add("hget");

			add("hmget");

			add("hexists");

			add("hlen");

			// Get all the fields in a hash
			add("hkeys");

			add("hvals");

			add("hgetAll");

			add("llen");

			add("lrange");

			add("lindex");

			add("smembers");

			add("scard");

			add("sismember");

			add("srandmember");

			add("zrange");

			add("zrank");

			add("zrevrank");

			add("zrevrange");

			add("zrangeWithScores");

			add("zrevrangeWithScores");

			add("zcard");

			add("zscore");

			add("zcount");

			add("zrangeByScore");

			add("zrevrangeByScore");

			add("zrangeByScoreWithScores");

			add("zrevrangeByScoreWithScores");
		}
	};

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Redis redis = null;
		Object object = null;
		boolean borrowOrOprSuccess = true;
		try {
			String methodName = method.getName();
			// 根据读写分离主机和备机
			if (READABLE.contains(methodName)) {
				redis = redisDispatch.getReadableRedis();
			} else {
				redis = redisDispatch.getWritableRedis();
			}
			//
			object = method.invoke(redis, args);
		} catch (Exception e) {
			e.printStackTrace();
			borrowOrOprSuccess = false;
			if (redis != null) {
				//返回到池中
				redisPoolFactory.returnBrokenResource(redis);
			}
			throw e;
		} finally {
			if (borrowOrOprSuccess && redis != null) {
				//返回到池中
				redisPoolFactory.returnResource(redis);
			}
		}

		return object;
	}
}
