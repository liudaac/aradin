package cn.aradin.spring.core.net.http.code;

public enum AradinCodedEnum implements CodedEnum {
	
	OK(0),
	ERROR(-1),
	PARTIAL(206),
	BADREQUEST(400),
	NOAUTH(401),
	FORBBIDEN(403),
	NOTFOUND(404),
	CONFLICT(409),
	TOOLARGE(413),
	TOOFAST(429),
	INTERNAL(500);
	
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
