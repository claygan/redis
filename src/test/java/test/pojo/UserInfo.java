package test.pojo;

import java.io.Serializable;

/**
 * @Title: UserInfo.java
 * @Package test.pojo
 * @Description: TODO(用一句话描述该文件做什么)
 * @author luog
 * @date 2014-3-19 下午3:02:55
 * @version V1.0
 */
public class UserInfo implements Serializable {

	/**
	 * @Description:
	 */
	private static final long serialVersionUID = -6628641591715814733L;

	private long userId = 1;

	private String username = "骆庚";

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
