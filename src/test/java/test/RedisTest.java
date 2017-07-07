package test;

import redis.clients.jedis.Jedis;

/**
 * @Title: RedisTest.java
 * @Package com.zzh.tool.redis.test
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-18 下午5:01:23
 * @version V1.0
 */
public class RedisTest {

	public static void main(String[] args) {
		Jedis jedis = new Jedis("192.168.2.10");
		long beginTime = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			jedis.get("n" + i);
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime - beginTime);
	}
}
