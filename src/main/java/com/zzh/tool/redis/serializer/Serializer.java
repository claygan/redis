package com.zzh.tool.redis.serializer;

/**
 * @Title: Serializer.java
 * @Package com.aaron.tool.cache.serializer
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2013-10-30 下午5:00:08
 * @version V1.0
 */
public interface Serializer {

	public abstract void init() throws Exception;

	public abstract byte[] serialize(Object paramObject) throws Exception;

	public abstract Object deserialize(byte[] paramArrayOfByte) throws Exception;

	public abstract void register(Class<?> paramClass);
}
