package cn.aradin.version.core.properties;

import java.io.Serializable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class VersionZookeeper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2115211896602769800L;
	private String addressId;
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
}
