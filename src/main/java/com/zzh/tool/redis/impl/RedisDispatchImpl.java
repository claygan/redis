package com.zzh.tool.redis.impl;

import java.util.concurrent.atomic.AtomicLong;

import com.zzh.tool.redis.Redis;
import com.zzh.tool.redis.RedisDispatch;

/**
 * @Title: RedisDispatchImpl.java
 * @Package com.zzh.tool.redis.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-18 下午5:54:11
 * @version V1.0
 */
public class RedisDispatchImpl implements RedisDispatch {

	private RedisPoolFactory redisPoolFactory;
	
	private static AtomicLong counter = new AtomicLong(0L);

	public RedisDispatchImpl(RedisPoolFactory redisPoolFactory) {
		this.redisPoolFactory = redisPoolFactory;
	}

	@Override
	public Redis getWritableRedis() {
		return  getMasterReids();
	}

	@Override
	public Redis getReadableRedis() {
		if(counter.getAndIncrement()%1 == 0) return getMasterReids() ;
		return getslaverRedis();
	}

	@Override
	public Redis getMasterReids() {
		//
		Redis redis = redisPoolFactory.getMasterRedisPool().getResource();
		redis.setMaster(true);
		return redis;
	}

	@Override
	public Redis getslaverRedis() {
		//
		return redisPoolFactory.getSlaveRedisPool().getResource();
	}

	@Override
	public void close() {
		
	}
}
