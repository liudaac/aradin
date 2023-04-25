package cn.aradin.easy.http.compare;

import java.io.Serializable;

public class ContentResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5857869397900244688L;
	private String recommendId;
	public String getRecommendId() {
		return recommendId;
	}
	public void setRecommendId(String recommendId) {
		this.recommendId = recommendId;
	}
}
