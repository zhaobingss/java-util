package com.diandong.util;

import java.util.UUID;

/**
 * @author zbss
 * @desc uuid 工具类
 * @date 2016-4-25 上午11:09:11
 */
public class UUIDUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().trim().replaceAll("-", "");
	}
	
	public static String getOriginUUID(){
		return UUID.randomUUID().toString();
	}

	public static void main(String[] args) {
		System.out.println(UUIDUtil.getUUID());
	}
}

