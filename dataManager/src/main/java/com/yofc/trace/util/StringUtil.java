package com.yofc.trace.util;

import java.util.UUID;


/**
 * 字符串工具类
 * 
 * @author wm
 *
 */
public class StringUtil {
	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Integer getUUIDInteger() {
		int uuid = UUID.randomUUID().hashCode();
		// 去掉“-”符号
		return Math.abs(uuid);
	}

	/**
	 * 判断是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否不是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if ((str != null) && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 生成四位编号
	 * 
	 * @param code
	 * @return
	 */
	public static String formatCode(int code) {
		Integer num = code + 1;
		String codeNum = num.toString();
		int codeLength = codeNum.length();
		for (int i = 4; i > codeLength; i--) {
			codeNum = "0" + codeNum;
		}
		return codeNum;
	}
	
	/**
	 * 生成四位编号不加1
	 * 
	 * @param code
	 * @return
	 */
	public static String formatCode1(String code) {
		int codeLength = code.length();
		for (int i = 4; i > codeLength; i--) {
			code = "0" + code;
		}
		return code;
	}
	
	
	public static void main(String[] args) {
		String code="2220892";
		System.out.println(code.substring(1, 5));
	}
}
