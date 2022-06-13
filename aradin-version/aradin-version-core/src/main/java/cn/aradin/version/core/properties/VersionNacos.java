package cn.aradin.version.core.properties;

import java.io.Serializable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VersionNacos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1534472659114596923L;
	private String group;
	private String dataId;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
}
