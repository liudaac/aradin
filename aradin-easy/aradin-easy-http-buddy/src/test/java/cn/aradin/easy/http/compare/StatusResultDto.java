package cn.aradin.easy.http.compare;

import java.io.Serializable;

public class StatusResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8320910320083105962L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
