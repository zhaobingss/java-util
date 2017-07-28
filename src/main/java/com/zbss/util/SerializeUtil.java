package com.zbss.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 序列化类
 *
 * @author zhaobing
 * @Date 2017年3月31日 下午12:47:46
 */
public class SerializeUtil {

	/**
	 * 序列化对象
	 *
	 * @param object 要序列化的对象
	 * @return 注意NPE的处理
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream os = null;
		ByteArrayOutputStream bs = null;
		try {
			bs = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bs);
			os.writeObject(object);
			byte[] bytes = bs.toByteArray();
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 反序列化
	 *
	 * @param bytes 字节数组
	 * @return 注意NPE 的处理
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bs = null;
		try {
			bs = new ByteArrayInputStream(bytes);
			ObjectInputStream os = new ObjectInputStream(bs);
			return os.readObject();
		} catch (Exception e) {
			return null;
		}
	}
}
