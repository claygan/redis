package test;

import com.zzh.tool.redis.Redis;
import com.zzh.tool.redis.config.RedisConfiguration;
import com.zzh.tool.redis.impl.RedisFactory;

import test.pojo.UserInfo;

/**
 * @Title: PutTest.java
 * @Package test
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-19 下午3:02:36
 * @version V1.0
 */
public class PutTest {

	public static void main(String[] args) {
		RedisConfiguration redisConfiguration = new RedisConfiguration("192.168.2.10:6379", "192.168.2.10:6379");
		RedisFactory redisFactory = new RedisFactory(redisConfiguration);
		Redis redis = redisFactory.getRedis();
		//
		UserInfo userInfo = new UserInfo();
		long beginTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - beginTime);
	}
}
