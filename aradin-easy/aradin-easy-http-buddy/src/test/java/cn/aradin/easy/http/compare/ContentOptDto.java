package cn.aradin.easy.http.compare;

import java.io.Serializable;

public class ContentOptDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6684688966305280812L;
	private String recommendId;
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
}
