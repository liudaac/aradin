package cn.aradin.spring.core.net.http.body;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.aradin.spring.core.net.http.code.AradinCodedEnum;
import cn.aradin.spring.core.net.http.error.HttpError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Deprecated
public class Resp<T> implements Serializable {
	private int code = 0;
	private String msg;
	private T data;

	public Resp<T> data(T data) {
		this.data = data;
		return this;
	}

	public Resp<T> code(int code) {
		this.code = code;
		return this;
	}

	public Resp<T> msg(String msg) {
		this.msg = msg;
		return this;
	}

	public boolean ifok() {
		return code == AradinCodedEnum.OK.getCode();
	}

	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public static Resp<Object> ok() {
		return new Resp<Object>();
	}

	public static <A> Resp<A> ok(A data) {
		return new Resp<A>().data(data);
	}

	public static Resp<Object> error(HttpError error) {
		return error(error, null);
	}

	public static Resp<Object> error(HttpError error, String alias) {
		return new Resp<Object>().code(error.getCode()!=null?error.getCode():error.getCoded().getCode()).msg(StringUtils.isNotBlank(alias) ? alias : error.getMsg());
	}
}
