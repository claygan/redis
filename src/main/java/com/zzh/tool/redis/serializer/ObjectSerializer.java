package com.zzh.tool.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Title: ObjectSerializer.java
 * @Package com.aaron.tool.cache.serializer
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2013-10-30 下午5:00:44
 * @version V1.0
 */
public class ObjectSerializer implements Serializer {
	public void init() throws Exception {
	}

	public byte[] serialize(Object object) throws Exception {
		ByteArrayOutputStream outputStream = null;
		ObjectOutputStream oos = null;
		try {
			outputStream = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(outputStream);
			oos.writeObject(object);

			return outputStream.toByteArray();
		} finally {
			if (oos != null) {
				oos.close();
			}
			if (outputStream != null)
				outputStream.close();
		}
	}

	public Object deserialize(byte[] bytes) throws Exception {
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new ByteArrayInputStream(bytes));

			return ois.readObject();
		} finally {
			if (ois != null)
				ois.close();
		}
	}

	public void register(Class<?> class1) {
	}
}