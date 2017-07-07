package test;

import com.zzh.tool.redis.config.RedisConfiguration;
import com.zzh.tool.redis.impl.RedisAtomicCounter;
import com.zzh.tool.redis.impl.RedisFactory;

/**
 * @Title: CounterTest.java
 * @Package com.zzh.tool.redis.test
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-19 上午10:14:28
 * @version V1.0
 */
public class CounterTest {

	public static void main(String[] args) {
		//
		RedisConfiguration redisConfiguration = new RedisConfiguration("192.168.1.10:6379", "192.168.1.10:6379");
		RedisFactory redisFactory = new RedisFactory(redisConfiguration);
		RedisAtomicCounter redisAtomicCounter = RedisAtomicCounter.getInstance("TEST_COUNTER", 100, 2, redisFactory);
		//
		// redisAtomicCounter.decrementAndGet("count");
		// long count = redisAtomicCounter.get("count");
		long count = redisAtomicCounter.getAndSet("count", 3);
		//
		System.out.println(count);
	}
}
