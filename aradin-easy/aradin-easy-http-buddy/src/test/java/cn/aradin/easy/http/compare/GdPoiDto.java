package cn.aradin.easy.http.compare;

import java.io.Serializable;

public class GdPoiDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7631243123093713455L;
	private String id;
	private String name;
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
}
