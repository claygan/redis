package com.zzh.tool.redis.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptyMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsEmptySetSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonListSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonMapSerializer;
import com.esotericsoftware.kryo.serializers.DefaultSerializers.CollectionsSingletonSetSerializer;

import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.CopyForIterateCollectionSerializer;
import de.javakaffee.kryoserializers.CopyForIterateMapSerializer;
import de.javakaffee.kryoserializers.EnumMapSerializer;
import de.javakaffee.kryoserializers.EnumSetSerializer;
import de.javakaffee.kryoserializers.GregorianCalendarSerializer;
import de.javakaffee.kryoserializers.JdkProxySerializer;
import de.javakaffee.kryoserializers.KryoReflectionFactorySupport;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;

/**
 * @Title: KryoSerializer.java
 * @Package com.aaron.tool.cache.serializer
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2013-10-30 下午4:59:08
 * @version V1.0
 */
public class KryoSerializer implements Serializer {

	public interface KryoPool {

		Kryo get();

		void yield(Kryo kryo);

	}
	public static class KryoPoolImpl implements KryoPool {

		private final Queue<Kryo> objects = new ConcurrentLinkedQueue<Kryo>();

		public Kryo get() {
			Kryo kryo;
			if ((kryo = objects.poll()) == null) {
				kryo = createInstance();
			}

			return kryo;
		}

		public void yield(Kryo kryo) {
			objects.offer(kryo);
		}

		/**
		 * Sub classes can customize the Kryo instance by overriding this method
		 * 
		 * @return create Kryo instance
		 */
		protected Kryo createInstance() {
			Kryo kryo = new KryoReflectionFactorySupport() {
				@SuppressWarnings("rawtypes")
				@Override
				public com.esotericsoftware.kryo.Serializer<?> getDefaultSerializer(final Class type) {
					if (EnumSet.class.isAssignableFrom(type)) {
						return new EnumSetSerializer();
					}
					if (EnumMap.class.isAssignableFrom(type)) {
						return new EnumMapSerializer();
					}
					if (Map.class.isAssignableFrom(type)) {
						return new CopyForIterateMapSerializer();
					}
					if (Collection.class.isAssignableFrom(type)) {
						return new CopyForIterateCollectionSerializer();
					}
					return super.getDefaultSerializer(type);
				}
			};
			kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
			kryo.register(Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
			kryo.register(Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
			kryo.register(Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
			kryo.register(Collections.singletonList("").getClass(), new CollectionsSingletonListSerializer());
			kryo.register(Collections.singleton("").getClass(), new CollectionsSingletonSetSerializer());
			kryo.register(Collections.singletonMap("", "").getClass(), new CollectionsSingletonMapSerializer());
			kryo.register(GregorianCalendar.class, new GregorianCalendarSerializer());
			kryo.register(InvocationHandler.class, new JdkProxySerializer());
			kryo.register(UUID.class, new de.javakaffee.kryoserializers.UUIDSerializer());
			UnmodifiableCollectionsSerializer.registerSerializers(kryo);
			SynchronizedCollectionsSerializer.registerSerializers(kryo);
			kryo.setReferences(false);
			return kryo;
		}

	}
	
	private final KryoPool kryoPool;
	
	public KryoSerializer() {
		this(new KryoPoolImpl());
	}
	
	public KryoSerializer(KryoPoolImpl kryoPoolImpl) {
		this.kryoPool = kryoPoolImpl;
	}

	@Override
	public void init() throws Exception {
		
	}

	@Override
	public byte[] serialize(Object object) throws Exception {
		Kryo kryo = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Output output = new Output(baos);
			kryo = kryoPool.get();
			kryo.writeClassAndObject(output, object);
			output.close();
			return baos.toByteArray();
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw e;
		} finally {
			if (kryo != null) {
				kryoPool.yield(kryo);
			}
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws Exception {
		Kryo kryo = null;
		try {
			kryo = kryoPool.get();
			return kryo.readClassAndObject(new Input(new ByteArrayInputStream(bytes, 0, bytes.length)));
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			throw e;
		} finally {
			if (kryo != null) {
				kryoPool.yield(kryo);
			}
		}
	}

	@Override
	public void register(Class<?> clazz) {
		// 
	}

}