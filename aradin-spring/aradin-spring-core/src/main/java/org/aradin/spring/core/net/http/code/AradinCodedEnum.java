package org.aradin.spring.core.net.http.code;

public enum AradinCodedEnum implements CodedEnum {
	
	OK(0),
	EROOR(-1);
	
	private final int code;

	AradinCodedEnum (int code) {
        this.code = code;
    }
	
	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return code;
	}
}
