package com.yofc.trace.config;

/**
 * 异常枚举类
 *
 */
public enum ExceptionEnum {
	UNKNOW_ERROR(-1, "未知异常"),
	PARAM_NULL(400, "参数不能为空"),
	ERROR(4000,"Error"),
	PARAM_ERROR(1400, "参数错误");
	private Integer code;

	private String msg;

	ExceptionEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
