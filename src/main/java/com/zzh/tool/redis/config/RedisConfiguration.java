package com.zzh.tool.redis.config;

/**
 * @Title: RedisConfiguration.java
 * @Package com.zzh.tool.redis.config
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-19 上午9:32:44
 * @version V1.0
 */
public class RedisConfiguration {

	/**
	 * @Description:主机地址
	 */
	private String masterAddress = "127.0.0.0:6379";

	/**
	 * @Description:备机地址
	 */
	private String slaverAddress = "127.0.0.0:6379";

	/**
	 * @Description:超时时间
	 */
	private int timeout = 2000;

	public RedisConfiguration() {

	}

	public RedisConfiguration(String masterAddress, String slaverAddress) {
		this.masterAddress = masterAddress;
		this.slaverAddress = slaverAddress;
	}

	public RedisConfiguration(String masterAddress, String slaverAddress, int timeout) {
		this.masterAddress = masterAddress;
		this.slaverAddress = slaverAddress;
		this.timeout = timeout;
	}

	public String getMasterAddress() {
		return masterAddress;
	}

	public void setMasterAddress(String masterAddress) {
		this.masterAddress = masterAddress;
	}

	public String getSlaverAddress() {
		return slaverAddress;
	}

	public void setSlaverAddress(String slaverAddress) {
		this.slaverAddress = slaverAddress;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
