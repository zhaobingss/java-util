package com.zbss.util;

import java.security.MessageDigest;

/**
 * MD5加密
 * @author zhaobing
 * @Date 2017年3月31日 下午12:35:29
 */
public class MD5Utils {

	/**
	 * md5加密
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0){
					i += 256;
				}
				if (i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args) {
		String str = "test" + "jp150107113" + "RTEHKA" + "4";
		System.out.println(str);
		System.out.println(md5(str));
	}
}