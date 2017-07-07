package com.zzh.tool.redis;

/**
 * @Title: RedisDispatch.java
 * @Package com.zzh.tool.redis
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-18 下午5:52:47
 * @version V1.0
 */
public interface RedisDispatch {

	/**
	 * 返回连接到这个DB所在机器的Jedis对象(一定返回主机可写的)
	 * 
	 * @return 连接在主机的Jedis对象
	 */
	public Redis getWritableRedis();

	/**
	 * 返回连接到这个DB所在机器的Redis对象 这里返回的可能是主机也可能是备机（轮着返回）
	 * 
	 * @return 连接在主机/备机的Redis对象
	 */
	public Redis getReadableRedis();

	/**
	 * 返回连接到这个DB所在机器的Redis对象 这里返回的是主机
	 * 
	 * @return 连接在主机的Redis对象
	 */
	public Redis getMasterReids();

	/**
	 * 返回连接到这个DB所在机器的Redis对象 这里返回的是备机
	 * 
	 * @return 连接在备机的Redis对象
	 */
	public Redis getslaverRedis();

	/**
	 * close master & slaver Redis
	 */
	public void close();
}
