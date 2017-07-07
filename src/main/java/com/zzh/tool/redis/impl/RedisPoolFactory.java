package com.zzh.tool.redis.impl;

import java.util.concurrent.atomic.AtomicLong;

import com.zzh.tool.redis.Redis;

public class RedisPoolFactory {
	
    private final AtomicLong newInstanceCounter = new AtomicLong(0);
    private final AtomicLong returnInstanceCounter = new AtomicLong(0);

    public long newInstanceCounter() {
        return newInstanceCounter.getAndIncrement();
    }
    public long returnInstanceCounter() {
        return returnInstanceCounter.getAndIncrement();
    }
    

	private RedisPool masterRedisPool;

	private RedisPool slaveRedisPool;

	public void returnBrokenResource(Redis resource) {
		if (resource.isMaster()) {
			masterRedisPool.returnBrokenResource(resource);
		} else {
			slaveRedisPool.returnBrokenResource(resource);
		}
	}

	public void returnResource(Redis resource) {
		if (resource.isMaster()) {
			masterRedisPool.returnResource(resource);
		} else {
			slaveRedisPool.returnResource(resource);
		}
	}
	
	public int getMasterNumActive(){
		return masterRedisPool.getNumActive();
	}

	public int getSlaveNumActive(){
		return slaveRedisPool.getNumActive();
	}
	
	public int getMasterNumIdle(){
		return masterRedisPool.getNumIdle();
	}

	public int getSlaveNumIdle(){
		return slaveRedisPool.getNumIdle();
	}
	
	public RedisPool getMasterRedisPool() {
		return masterRedisPool;
	}

	public void setMasterRedisPool(RedisPool masterRedisPool) {
		this.masterRedisPool = masterRedisPool;
	}

	public RedisPool getSlaveRedisPool() {
		return slaveRedisPool;
	}

	public void setSlaveRedisPool(RedisPool slaveRedisPool) {
		this.slaveRedisPool = slaveRedisPool;
	}

	
}
