package com.zzh.tool.redis.impl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import com.zzh.tool.redis.Redis;

public class RedisImpl extends Jedis implements Redis {
	
	private boolean master;
	
	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public RedisImpl(final String host) {
		super(host);
	}

	public RedisImpl(final String host, final int port) {
		super(host, port);
	}

	public RedisImpl(final String host, final int port, final int timeout) {
		super(host, port, timeout);
	}

	public RedisImpl(JedisShardInfo shardInfo) {
		super(shardInfo);
	}
}
