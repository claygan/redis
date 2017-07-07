package com.zzh.tool.redis;

import redis.clients.jedis.AdvancedJedisCommands;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.MultiKeyCommands;
import redis.clients.jedis.ScriptingCommands;

/**
 * @Title: Redis.java
 * @Package com.zzh.tool.redis
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-18 下午5:30:22
 * @version V1.0
 */
public interface Redis extends JedisCommands, BinaryJedisCommands, MultiKeyCommands,AdvancedJedisCommands, ScriptingCommands {
	
	public boolean isMaster() ;
	
	public void setMaster(boolean master);
	
}
