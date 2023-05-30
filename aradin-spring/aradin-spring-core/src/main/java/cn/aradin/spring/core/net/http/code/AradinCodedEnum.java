package cn.aradin.spring.core.net.http.code;

/**
 * 错误码
 * @author daliu
 *
 */
public enum AradinCodedEnum implements CodedEnum {
	
	OK(0), //OK
	ERROR(-1), //ERROR
	PARTIAL(206), //PARTIAL
	BADREQUEST(400), //BADREQUEST
	NOAUTH(401), //NOAUTH
	FORBBIDEN(403), //FORBBIDEN
	NOTFOUND(404), //NOTFOUND
	CONFLICT(409), //CONFLICT
	TOOLARGE(413), //TOOLARGE
	TOOFAST(429), //TOOFAST
	INTERNAL(500), //INTERNAL
	MQERROR(600); //MQERROR
	
	/**
	 * 错误码对应的int值
	 */
	private final int code;

	AradinCodedEnum (int code) {
        this.code = code;
    }
	
	/**
	 * 获取int错误码
	 */
	@Override
	public int getCode() {
		// TODO Auto-generated method stub
		return code;
	}
}
