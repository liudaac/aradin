package cn.aradin.spring.core.net.http.error;

import java.io.Serializable;

import cn.aradin.spring.core.net.http.code.CodedEnum;
import lombok.Builder;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@Builder
public class HttpError implements Serializable {
	private CodedEnum coded;
	private Integer code;
	private String msg;

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
}
