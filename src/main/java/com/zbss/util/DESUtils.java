package com.zbss.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DESUtils {

	/**
	 * 加密
	 * @param str 要加密的字符串
	 * @param key 秘钥
	 * @return
	 * @throws Exception
	 */
	public static String encode(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		byte[] iv = new byte[8];
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"), ivSpec);
		return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
	}

	/**
	 * 解密
	 * @param str 要解密的字符串
	 * @param key 秘钥
	 * @return
	 * @throws Exception
	 */
	public static String decode(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		byte[] iv = new byte[8];
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "DES"), ivSpec);
		return new String(cipher.doFinal(Base64.decodeBase64(str)), "UTF-8");
	}

	public static void main(String[] args) throws Exception {
		String a = DESUtils.encode("0123456789012345", "E3SdR6FG");
		System.out.println(a);
		String b = DESUtils.decode(a, "E3SdR6FG");
		System.out.println(b);
	}
}