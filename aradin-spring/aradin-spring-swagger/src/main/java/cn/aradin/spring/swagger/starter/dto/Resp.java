package cn.aradin.spring.swagger.starter.dto;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson2.JSONObject;

import cn.aradin.spring.core.net.http.code.AradinCodedEnum;
import cn.aradin.spring.core.net.http.code.CodedEnum;
import cn.aradin.spring.core.net.http.error.HttpError;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(description = "返回协议结构")
public class Resp<C extends CodedEnum, T> implements Serializable{
	@ApiModelProperty(value="操作结果", name="code", required = true)
	private C code;
	@ApiModelProperty(value="错误描述信息", name="msg", required = false)
	private String msg;
	@ApiModelProperty(value="返回数据", name="data", required = false)
	private T data;
	
	public C getCode() {
		return code;
	}
	public void setCode(C code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
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
		Resp<C, Object> resp = new Resp<C, Object>();
		resp.setCode(coded);
		return resp;
	}
	public static <C extends CodedEnum, A> Resp<C, A> ok(C coded, A data) {
		Resp<C, A> resp = new Resp<C, A>();
		resp.setCode(coded);
		resp.setData(data);
		return resp;
	}
	public static <C extends CodedEnum> Resp<C, Object> error(HttpError<C> error) {
		return error(error, null);
	}
	public static <C extends CodedEnum> Resp<C, Object> error(HttpError<C> error, String alias) {
		return new Resp<C, Object>()
			.code(error.getCoded())
			.msg(StringUtils.isNotBlank(alias)?alias:error.getMsg());
	}
}
