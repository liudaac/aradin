package cn.aradin.easy.http.compare;

import java.io.Serializable;

public class StatusDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7103631209895603465L;
	private String recommendId;
	private String status;
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
