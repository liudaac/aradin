package cn.aradin.spring.core.net.http.code;

public enum AradinCodedEnum implements CodedEnum {
	
	OK(0),
	ERROR(-1),
	TOOFAST(429);
	
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
