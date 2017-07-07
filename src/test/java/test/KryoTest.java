/**
 * Project Name: zzh.tool.redis
 * File Name: KryoTest.java
 * Package Name: test
 * Date: 2015-7-10下午7:15:45 
 * Copyright (c) 2015, www.zhongzhihui.com All Rights Reserved. 
 */

package test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.zzh.tool.redis.serializer.KryoSerializer;
import com.zzh.tool.redis.serializer.ObjectSerializer;

/** 
 * ClassName: KryoTest
 * Description: TODO(用一句话描述这个类)
 * 
 * @author jupiter@zhongzhihui.com
 * @date: 2015-7-10 下午7:15:45
 */
public class KryoTest {
	private static KryoSerializer kryoSerializer = new KryoSerializer();
	private static String str = "大家都有过复制一个大文件时，久久等待却不见结束，明明很着急却不能取消的情况吧——一旦取消，一切都要从头开始！大家都有过复制一个大文件时，久久等待却不见结束，明明很着急却不能取消的情况吧——一旦取消，一切都要从头开始！大家都有过复制一个大文件时大家都有过复制一个大文件时";  
	private static ObjectSerializer objectSerializer = new ObjectSerializer();
	public static void main(String[] args) throws InterruptedException {
		long beginTime = System.currentTimeMillis();
		final DataObject obj = DataObject.getDataObject();
		ExecutorService executor = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 1000000; i++) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						kryoSerializer.serialize(obj);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.DAYS);
		
		long endTime = System.currentTimeMillis();
		System.out.println("cost " + (endTime - beginTime));
	}
	
	public static class DataObject {
		
		private String id;
		
		private String name;
		
		private Date time;
		
		private Map<String,Object> attributes = new HashMap<String,Object>();
		
		private List<Set<String>> list = new ArrayList<Set<String>>();

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

		public List<Set<String>> getList() {
			return list;
		}

		public void setList(List<Set<String>> list) {
			this.list = list;
		}
		
		public Date getTime() {
			return time;
		}

		public void setTime(Date time) {
			this.time = time;
		}

		public static DataObject getDataObject(){
			DataObject obj = new DataObject();
			
			obj.setId(UUID.randomUUID().toString());
			obj.setName(str);
			obj.getAttributes().put("someTime", new Date());
			obj.getAttributes().put("someId", new BigDecimal(1000000));
			obj.getAttributes().put("someName", UUID.randomUUID().toString());
			
			Set<String> set1 = new TreeSet<String>();
			set1.add( UUID.randomUUID().toString());
			set1.add( UUID.randomUUID().toString());
			set1.add( UUID.randomUUID().toString());
			set1.add( UUID.randomUUID().toString());
			set1.add( UUID.randomUUID().toString());
			
			Set<String> set2 = new TreeSet<String>();
			set2.add( UUID.randomUUID().toString());
			set2.add( UUID.randomUUID().toString());
			set2.add( UUID.randomUUID().toString());
			set2.add( UUID.randomUUID().toString());
			set2.add( UUID.randomUUID().toString());
			
			obj.getList().add(set1);
			obj.getList().add(set2);
			
			return obj;
		}
		
	}
}
