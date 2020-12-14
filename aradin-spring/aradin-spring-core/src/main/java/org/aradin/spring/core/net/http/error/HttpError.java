package org.aradin.spring.core.net.http.error;

import java.io.Serializable;

import org.aradin.spring.core.net.http.code.CodedEnum;
import lombok.Data;

@SuppressWarnings("serial")
@Data
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
}
