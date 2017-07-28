package com.zbss.util;

/**
 * Base64加解密 （可以变换字符的顺序实现自定义的Base64）
 *
 * @author zhaobing
 * @Date 2017-03-31
 */
public class Base64Utils {

	private final static String encodingChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

	/**
	 * 加密字符串
	 *
	 * @param source
	 * @return
	 */
	public static String encode(String source) {
		char[] sourceBytes = getPaddedBytes(source);
		int numGroups = (sourceBytes.length + 2) / 3;
		char[] targetBytes = new char[4];
		char[] target = new char[4 * numGroups];

		for (int group = 0; group < numGroups; group++) {
			convert3To4(sourceBytes, group * 3, targetBytes);
			for (int i = 0; i < targetBytes.length; i++) {
				target[i + 4 * group] = encodingChar.charAt(targetBytes[i]);
			}
		}

		int numPadBytes = sourceBytes.length - source.length();
		for (int i = target.length - numPadBytes; i < target.length; i++) {
			target[i] = '=';
		}

		return new String(target);
	}

	/**
	 * 解密字符串
	 *
	 * @param source 要解密的字符串
	 * @return
	 */
	public static String decode(String source) {
		if (source.length() % 4 != 0)
			throw new RuntimeException("valid Base64 codes have a multiple of 4 characters");
		int numGroups = source.length() / 4;
		int numExtraBytes = source.endsWith("==") ? 2 : (source.endsWith("=") ? 1 : 0);
		byte[] targetBytes = new byte[3 * numGroups];
		byte[] sourceBytes = new byte[4];
		for (int group = 0; group < numGroups; group++) {
			for (int i = 0; i < sourceBytes.length; i++) {
				sourceBytes[i] = (byte) Math.max(0, encodingChar.indexOf(source.charAt(4 * group + i)));
			}
			convert4To3(sourceBytes, targetBytes, group * 3);
		}
		return new String(targetBytes, 0, targetBytes.length - numExtraBytes);
	}

	/**
	 * padding字节
	 *
	 * @param source
	 * @return
	 */
	private static char[] getPaddedBytes(String source) {
		char[] converted = source.toCharArray();
		int requiredLength = 3 * ((converted.length + 2) / 3);
		char[] result = new char[requiredLength];
		System.arraycopy(converted, 0, result, 0, converted.length);
		return result;
	}

	/**
	 * 将3个8位字节转换为4个6位字节
	 *
	 * @param source
	 * @param sourceIndex
	 * @param target
	 */
	private static void convert3To4(char[] source, int sourceIndex, char[] target) {
		target[0] = (char) (source[sourceIndex] >>> 2);
		target[1] = (char) (((source[sourceIndex] & 0x03) << 4) | (source[sourceIndex + 1] >>> 4));
		target[2] = (char) (((source[sourceIndex + 1] & 0x0f) << 2) | (source[sourceIndex + 2] >>> 6));
		target[3] = (char) (source[sourceIndex + 2] & 0x3f);
	}

	/**
	 * 将4个6位字节转换为3个8位字节
	 *
	 * @param source
	 * @param target
	 * @param targetIndex
	 */
	private static void convert4To3(byte[] source, byte[] target, int targetIndex) {
		target[targetIndex] = (byte) ((source[0] << 2) | (source[1] >>> 4));
		target[targetIndex + 1] = (byte) (((source[1] & 0x0f) << 4) | (source[2] >>> 2));
		target[targetIndex + 2] = (byte) (((source[2] & 0x03) << 6) | (source[3]));
	}

	public static void main(String[] args) {
		String test = "1234";
		test = Base64Utils.encode(test);
		System.out.println(test);
		System.out.println(Base64Utils.decode(test));
	}
}
