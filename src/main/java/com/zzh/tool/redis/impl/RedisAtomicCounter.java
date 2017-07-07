package com.zzh.tool.redis.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.zzh.tool.redis.utils.LangUtil;

import redis.clients.jedis.JedisCommands;

public class RedisAtomicCounter {
	private static ConcurrentHashMap<String, RedisAtomicCounter> instanceMap = new ConcurrentHashMap<String, RedisAtomicCounter>();

	private ConcurrentHashMap<String, AtomicLong> local = new ConcurrentHashMap<String, AtomicLong>();
	private ConcurrentHashMap<String, AtomicLong> delta = new ConcurrentHashMap<String, AtomicLong>();

	// 计数器命名空间
	private String namespace;
	// 子计数器最大个数
	private int maxSize = 100;
	// 提交周期
	private int commitInterval = 100;
	// redis客户端工厂
	private RedisFactory redisFactory;

	private static int maxSizeDefault = Integer.MAX_VALUE;
	private static int commitIntervalDefault = 100;

	private RedisAtomicCounter(String namespace, int maxSize, int commitInterval, RedisFactory redisFactory) {
		this.namespace = namespace;
		this.maxSize = maxSize == -1 ? Integer.MAX_VALUE : maxSize;
		this.commitInterval = commitInterval;
		this.redisFactory = redisFactory;
	}

	/**
	 * 返回指定 namespace的RedisAtomicCounter实例，计数器个数和同步间隔取默认值，如配置文件未指定，计数器个数大小不限， 同步间隔值为100
	 * 
	 * @param namespace
	 * @return
	 */
	public static RedisAtomicCounter getInstance(String namespace) {
		return getInstance(namespace, maxSizeDefault);
	}

	/**
	 * 返回指定 namespace的RedisAtomicCounter实例，同步间隔取默认值，如配置文件未指定，值为100
	 * 
	 * @param namespace
	 * @param maxSize
	 *            该namespace下计数器的最大个数，-1个数不限
	 * @return
	 */
	public static RedisAtomicCounter getInstance(String namespace, int maxSize) {
		return getInstance(namespace, maxSize, commitIntervalDefault, new RedisFactory());
	}

	/**
	 * 返回指定 namespace的RedisAtomicCounter实例
	 * 
	 * @param namespace
	 * @param maxSize
	 *            该namespace下计数器的最大个数，-1个数不限
	 * @param commitInterval
	 *            同步间隔，达到此值时去数据库同步计数器值，若每次调用incrementAndGet希望得到准确值，将该值设为1
	 * @param redisFactory
	 *            redis客户端工厂实例
	 * @return
	 */
	public static RedisAtomicCounter getInstance(String namespace, int maxSize, int commitInterval,
			RedisFactory redisFactory) {
		RedisAtomicCounter counter = instanceMap.get(namespace);
		if (counter != null) {
			return counter;
		}
		RedisAtomicCounter temp = new RedisAtomicCounter(namespace, maxSize, commitInterval, redisFactory);
		counter = instanceMap.putIfAbsent(namespace, temp);
		return counter == null ? temp : counter;
	}

	/**
	 * 
	 * @param name
	 *            计数器的名字
	 * @return 计数器的值
	 */
	public long incrementAndGet(String name) {

		AtomicLong incr = delta.get(name);
		if (incr == null) {
			if (!checkSize()) {
				throw new RuntimeException(namespace + "计数器个数超限");
			}
			AtomicLong temp = new AtomicLong();
			incr = delta.putIfAbsent(name, temp);
			incr = incr == null ? temp : incr;
		}
		long value = incr.incrementAndGet();
		AtomicLong current = local.get(name);
		if (current == null) {
			JedisCommands jedis = redisFactory.getRedis();
			long remoteValue = jedis.hincrBy(namespace, name, value);
			AtomicLong temp = new AtomicLong(remoteValue);
			current = local.putIfAbsent(name, temp);
			incr.getAndSet(0);
			if (current == null) {
				current = temp;
			} else {
				current.addAndGet(value);
			}
			return remoteValue;
		}

		if (value >= commitInterval) {
			JedisCommands jedis = redisFactory.getRedis();
			long remoteValue = jedis.hincrBy(namespace, name, value);
			incr.getAndSet(0);
			current.getAndSet(remoteValue);
			return remoteValue;
		} else {
			return current.get() + value;
		}
	}

	/**
	 * 
	 * @param name
	 *            计数器的名字
	 * @return 计数器的值
	 */
	public long decrementAndGet(String name) {

		AtomicLong incr = delta.get(name);
		if (incr == null) {
			if (!checkSize()) {
				throw new RuntimeException(namespace + "计数器个数超限");
			}
			AtomicLong temp = new AtomicLong();
			incr = delta.putIfAbsent(name, temp);
			incr = incr == null ? temp : incr;
		}
		long value = incr.incrementAndGet();
		AtomicLong current = local.get(name);
		if (current == null) {
			JedisCommands jedis = redisFactory.getRedis();
			long remoteValue = jedis.hincrBy(namespace, name, -value);
			AtomicLong temp = new AtomicLong(remoteValue);
			current = local.putIfAbsent(name, temp);
			incr.getAndSet(0);
			if (current == null) {
				current = temp;
			} else {
				current.addAndGet(-value);
			}
			return remoteValue;
		}

		if (value >= commitInterval) {
			JedisCommands jedis = redisFactory.getRedis();
			long remoteValue = jedis.hincrBy(namespace, name, -value);
			incr.getAndSet(0);
			current.getAndSet(remoteValue);
			return remoteValue;
		} else {
			return current.get() - value;
		}
	}

	/**
	 * 
	 * @param name
	 *            计数器的名字
	 * @return 计数器的值
	 */
	public long get(String name) {
		AtomicLong current = local.get(name);
		Long value = 0L;
		if (current == null) {
			Long remoteValue = LangUtil.parseLong(redisFactory.getRedis().hget(namespace, name));
			if (remoteValue != null) {
				current = new AtomicLong();
				current.set(remoteValue.longValue());
				current = local.putIfAbsent(name, current);
				if (current == null) {
					value = remoteValue;
				} else {
					value = current.get();
				}
			}
		} else {
			value = current.get();
		}
		return value;
	}

	/**
	 * 
	 * @param name
	 *            计数器的名字
	 * @param newValue
	 *            计数器新值
	 * @return 计数器的原值
	 */
	public long getAndSet(String name, long newValue) {
		// 获取原值
		long value = get(name);
		// 放入新的值
		redisFactory.getRedis().hdel(namespace, name);
		delta.remove(name);
		local.remove(name);
		redisFactory.getRedis().hincrBy(namespace, name, newValue);
		//
		return value;
	}

	/**
	 * 检查该namespace下的计数器个数是否超限，不超限返回true，超限时返回false
	 * 
	 * @return
	 */
	public boolean checkSize() {
		return maxSize > delta.size();
	}
}
