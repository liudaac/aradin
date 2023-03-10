package cn.aradin.spring.core.net.http.error;

import java.io.Serializable;

import cn.aradin.spring.core.net.http.code.CodedEnum;

@SuppressWarnings("serial")
public class HttpError implements Serializable {
	private CodedEnum coded;
	private Integer code;
	private String msg;

	public HttpError() {
		
	}
	
	public HttpError(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public HttpError(CodedEnum coded, String msg) {
		this.coded = coded;
		this.msg = msg;
	}
	
	public HttpError(CodedEnum coded, Integer code, String msg) {
		this.coded = coded;
		this.msg = msg;
	}
	
	public CodedEnum getCoded() {
		return coded;
	}

	public void setCoded(CodedEnum coded) {
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
	
	public static HttpError instance(CodedEnum coded, String msg) {
		return new HttpError(coded, msg);
	}
}
