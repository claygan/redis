package com.zzh.tool.redis.impl;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

import com.zzh.tool.redis.Redis;

/**
 * @Title: RedisPool.java
 * @Package: com.zzh.tool.redis.impl
 * @Description: TODO(用一句话描述该文件做什么)
 * @Author: noriko
 * @Date: 2014-12-15 下午11:26:03
 */
public class RedisPool extends Pool<Redis> {

	private static final int DEFAULT_TIMEOUT = 5000;

	public RedisPool(final Config poolConfig, final String address) {
		int index = address.indexOf(':');
		String host = address.substring(0, index);
		String port = address.substring(index + 1);
		int iPort = Integer.parseInt(port);
		//
		initPool(poolConfig, new RedisCustomFactory(host, iPort, DEFAULT_TIMEOUT, null, Protocol.DEFAULT_DATABASE));
	}

	public RedisPool(final Config poolConfig, final String host, int port, int timeout, final String password, final int database) {
		super(poolConfig, new RedisCustomFactory(host, port, timeout, password, database));
	}

	public RedisPool(final Config poolConfig, final String host, final int port, final int timeout) {
		this(poolConfig, host, port, timeout, null, Protocol.DEFAULT_DATABASE);
	}

	public void returnBrokenResource(final Redis resource) {
		returnBrokenResourceObject(resource);
	}

	public void returnResource(final Redis resource) {
		returnResourceObject(resource);
	}
	
	public int getNumActive(){
		return this.internalPool.getNumActive();
	}
	
	public int getNumIdle(){
		return this.internalPool.getNumIdle();
	}

}

/**
 * PoolableObjectFactory custom impl.
 */
class RedisCustomFactory extends BasePoolableObjectFactory {
	private final String host;

	private final int port;

	private final int timeout;

	private final String password;

	private final int database;

	public RedisCustomFactory(final String host, final int port, final int timeout, final String password, final int database) {
		super();
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.password = password;
		this.database = database;
	}

	public Object makeObject() throws Exception {
		final RedisImpl redis = new RedisImpl(this.host, this.port, this.timeout);

		redis.connect();
		if (null != this.password) {
			redis.auth(this.password);
		}
		if (database != 0) {
			redis.select(database);
		}

		return (Redis) redis;
	}

	@Override
	public void activateObject(Object obj) throws Exception {
		if (obj instanceof RedisImpl) {
			final RedisImpl jedis = (RedisImpl) obj;
			if (jedis.getDB() != database) {
				jedis.select(database);
			}
		}
	}

	public void destroyObject(final Object obj) throws Exception {
		if (obj instanceof RedisImpl) {
			final RedisImpl jedis = (RedisImpl) obj;
			if (jedis.isConnected()) {
				try {
					try {
						jedis.quit();
					} catch (Exception e) {
					}
					jedis.disconnect();
				} catch (Exception e) {

				}
			}
		}
	}

	public boolean validateObject(final Object obj) {
		if (obj instanceof RedisImpl) {
			final RedisImpl jedis = (RedisImpl) obj;
			try {
				return jedis.isConnected() && jedis.ping().equals("PONG");
			} catch (final Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}
}