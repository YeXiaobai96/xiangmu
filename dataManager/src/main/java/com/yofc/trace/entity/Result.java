package com.yofc.trace.entity;



import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4550450415023413167L;
	private boolean flag;
	private int code;
	private String msg;
	private Object data;
	
	public Result(Object data) {
		super();
		this.flag = true;
		this.code = 0;
		this.msg = "success";
		this.data = data;
	}
	public Result(String msg,Object data) {
		super();
		this.flag = true;
		this.code = 0;
		this.msg = msg;
		this.data = data;
	}
	
	public Result(Integer code,String msg,Object data) {
		super();
		this.flag = false;
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}
