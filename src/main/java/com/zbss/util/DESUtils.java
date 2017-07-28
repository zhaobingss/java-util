package com.zbss.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesUtils {
	public static final String key = "Y*1J0#9d";
	public static final String key1 = "D.Ene#*1";

	/**
	 * 加密
	 * @param str 要加密的字符串
	 * @param key 秘钥
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes("UTF-8"))),
				new IvParameterSpec(key.getBytes("UTF-8")));
		return DesUtils.encode(cipher.doFinal(str.getBytes("UTF-8")));
	}

	private static String encode(byte[] arg1) {
		return Base64Android.encodeToString(arg1, 2);
	}

	/**
	 * 解密
	 * @param str 要解密的字符串
	 * @param key 秘钥
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String str, String key) throws Exception {
		byte[] bytes = DesUtils.decode(str);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes("UTF-8"))),
				new IvParameterSpec(key.getBytes("UTF-8")));
		return new String(cipher.doFinal(bytes));
	}

	private static byte[] decode(String arg1) {
		return Base64Android.decode(arg1, 2);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(DesUtils.decrypt("ViRQ1ytphW0LsRnhguQrkyGK8jwby/ZamOxYwsOxQ6GfNJ1z5SBgFRePX6I3rVSvjeMMk4ZV0/2lXK9M/MOAnI6iCsxGlG/9InGs4urgr8m6eVUK4Gv7iA==", DesUtils.key));
		System.out.println(DesUtils.decrypt("RzRbFoLFHet77evgHBASzHoN0R/xjez5KFWyakygSQRYUQC0Y8Wj170QvHUQ4Lt6BB3g4ImdRDztX4wA3KK8DDQAUfX5brgIhkv779F/1GhDrEM2DV4Q1A==", DesUtils.key));
		System.out.println(DesUtils.encrypt("{\"org\":\"KMG\",\"dst\":\"LJG\",\"flightDate\":\"2017-03-29\",\"tripType\":\"OW\",\"version\":\"3.0.0\"}", DesUtils.key));
		System.out.println(DesUtils.decrypt("MCOFNirW4EnYboV6Yb7R4q04rs+1Q8y0+8vbNFm8PT89kRIp6/8c6MjAyWph2Wfwn+eT+7HgIWWnUKbpLlQ/uc2OFf5frzUH", DesUtils.key1));
	}
}