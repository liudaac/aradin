package org.aradin.spring.swagger.starter.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.aradin.spring.core.net.http.code.AradinCodedEnum;
import org.aradin.spring.core.net.http.code.CodedEnum;
import org.aradin.spring.core.net.http.error.HttpError;

import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "返回协议结构")
public class Resp<C extends CodedEnum, T> implements Serializable{
	@ApiModelProperty(value="操作结果", name="code", required = true)
	private C code;
	@ApiModelProperty(value="错误描述信息", name="msg", required = false)
	private String msg;
	@ApiModelProperty(value="返回数据", name="data", required = false)
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
	public static Resp<CodedEnum, Object> error(HttpError error) {
		return error(error, null);
	}
	public static Resp<CodedEnum, Object> error(HttpError error, String alias) {
		return new Resp<CodedEnum, Object>()
			.code(error.getCoded())
			.msg(StringUtils.isNotBlank(alias)?alias:error.getMsg());
	}
}
