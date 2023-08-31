package cn.aradin.spring.core.net.http.code;

/**
 * 错误码
 * @author daliu
 *
 */
public enum AradinCodedEnum implements CodedEnum {
	/**
	 * OK
	 */
	OK(0),
	/**
	 * ERROR
	 */
	ERROR(-1), 
	/**
	 * PARTIAL
	 */
	PARTIAL(206),
	/**
	 * BADREQUEST
	 */
	BADREQUEST(400), 
	/**
	 * NOAUTH
	 */
	NOAUTH(401),
	/**
	 * FORBBIDEN
	 */
	FORBBIDEN(403),
	/**
	 * NOTFOUND
	 */
	NOTFOUND(404),
	/**
	 * CONFLICT
	 */
	CONFLICT(409),
	/**
	 * TOOLARGE
	 */
	TOOLARGE(413),
	/**
	 * TOOFAST
	 */
	TOOFAST(429),
	/**
	 * INTERNAL
	 */
	INTERNAL(500),
	/**
	 * MQERROR
	 */
	MQERROR(600);
	
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
