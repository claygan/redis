package com.zzh.tool.redis.impl;

import java.lang.reflect.Proxy;

import com.zzh.tool.redis.Redis;
import com.zzh.tool.redis.config.RedisConfiguration;
import com.zzh.tool.redis.proxy.RedisProxyHandler;

public class RedisFactory {

	private RedisConfiguration redisConfiguration;

	private RedisPoolFactory redisPoolFactory;

	public RedisFactory() {
	}

	public RedisFactory(RedisConfiguration redisConfiguration) {
		this.redisConfiguration = redisConfiguration;
	}

	/**
	 * @Title: getRedis
	 * @Description: 返回redis动态代理对象
	 * @return Redis
	 * @return
	 */
	public Redis getRedis() {
		//
//		String masterAddress = redisConfiguration.getMasterAddress();
//		String slaverAddress = redisConfiguration.getSlaverAddress();
//		int timeout = redisConfiguration.getTimeout();
		//
		return (Redis) Proxy.newProxyInstance(Redis.class.getClassLoader(), new Class[] { Redis.class }, new RedisProxyHandler(redisPoolFactory));
	}

	public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
		this.redisConfiguration = redisConfiguration;
	}

	public void setRedisPoolFactory(RedisPoolFactory redisPoolFactory) {
		this.redisPoolFactory = redisPoolFactory;
	}
}
