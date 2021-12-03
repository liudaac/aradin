package cn.aradin.spring.core.net.http.body;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

import cn.aradin.spring.core.net.http.code.AradinCodedEnum;
import cn.aradin.spring.core.net.http.code.CodedEnum;
import cn.aradin.spring.core.net.http.error.HttpError;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Resp<C extends CodedEnum, T> implements Serializable {
	private C code;
	private String msg;
	private T data;
	public Resp<C, T> data(T data) {
		this.data = data;
		return this;
	}
	public Resp<C, T> code(C code) {
		this.code = code;
		return this;
	}
	public Resp<C, T> msg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public boolean ifok() {
		return code.getCode()==0;
	}
	
	public String toString() {
		return JSONObject.toJSONString(this);
	}
	public static Resp<AradinCodedEnum, Object> ok() {
		return new Resp<AradinCodedEnum, Object>().code(AradinCodedEnum.OK);
	}
	public static <A> Resp<AradinCodedEnum, A> ok(A data) {
		return new Resp<AradinCodedEnum, A>().data(data).code(AradinCodedEnum.OK);
	}
	public static <C extends CodedEnum> Resp<C, Object> ok(C coded) {
		return new Resp<C, Object>(coded, null, null);
	}
	public static <C extends CodedEnum, A> Resp<C, A> ok(C coded, A data) {
		return new Resp<C, A>(coded, null, data);
	}
	public static Resp<CodedEnum, Object> error(HttpError error) {
		return error(error, null);
	}
	public static Resp<CodedEnum, Object> error(HttpError error, String alias) {
		return new Resp<CodedEnum, Object>()
			.code(error.getCoded())
			.msg(StringUtils.isNotBlank(alias)?alias:error.getMsg());
	}
}
