package cn.aradin.spring.core.net.http.error;

import java.io.Serializable;

import cn.aradin.spring.core.net.http.code.CodedEnum;

public class HttpError<C extends CodedEnum> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5427190417681581618L;
	private C coded;
	private Integer code;
	private String msg;

	public HttpError() {
		
	}
	
	public HttpError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public HttpError(C coded, String msg) {
		this.coded = coded;
		this.msg = msg;
	}
	
	public HttpError(C coded, Integer code, String msg) {
		this.coded = coded;
		this.msg = msg;
	}
	
	public CodedEnum getCoded() {
		return coded;
	}

	public void setCoded(C coded) {
		this.coded = coded;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static <C extends CodedEnum> HttpError<C> instance(C coded, String msg) {
		return new HttpError<C>(coded, msg);
	}
}
