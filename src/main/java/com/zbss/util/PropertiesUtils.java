package com.zbss.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * @author zbss<br>
 * @desc 属性工具类
 * @date 2016-5-9 下午3:43:16
 */
public class PropertiesUtils {
	
	public static Properties getProperties(String filePath) throws Exception{
		Properties p = new Properties();
		p.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(filePath));
		return p;
	}
	
	public static Properties getAbsolutePathProperties(String filePath) throws Exception{
		Properties p = new Properties();
		p.load(new FileInputStream(filePath));
		return p;
	}
}
