package test;

import com.zzh.tool.redis.Redis;
import com.zzh.tool.redis.config.RedisConfiguration;
import com.zzh.tool.redis.impl.RedisFactory;

/**
 * @Title: CacheTest.java
 * @Package com.zzh.tool.redis.test
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-19 上午10:14:42
 * @version V1.0
 */
public class CacheTest {

	public static void main(String[] args) {
		//
		RedisConfiguration redisConfiguration = new RedisConfiguration("192.168.1.11:6379", "192.168.1.10:6379");
		RedisFactory redisFactory = new RedisFactory(redisConfiguration);
		//
		Redis redis = redisFactory.getRedis();
		redis.set("kyo", "luo");
		String value = redis.get("kyo");
		//
		System.out.println(value);
	}

}
