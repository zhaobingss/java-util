package com.zbss.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtils {

	public static String encode(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), ivSpec);
		return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
	}

	public static String decode(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] iv = new byte[16];
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"), ivSpec);
		return new String(cipher.doFinal(Base64.decodeBase64(str)), "UTF8");
	}

	public static void main(String[] args) throws Exception {
		String a = AESUtils.encode("0123456789012345", "E3SdR678WsTKlQhA");
		System.out.println(a);
		String b = AESUtils.decode(a, "E3SdR678WsTKlQhA");
		System.out.println(b);
	}
}
