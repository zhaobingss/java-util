package com.zbss.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zbss
 * @desc 时间工具类
 * @date 2016-4-23 下午12:54:54
 */
public class DateUtils {

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String HH_MM_SS = "HH:mm:ss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String HHMMSS = "HHmmss";

	/**
	 * 获取pattern
	 * @param pattern 日期的格式
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	/**
	 * 把日期格式化为字符串
	 * @param date 要格式化的日期
	 * @param pattern 日期的格式
	 * @return
	 */
	public static String formatDateToString(Date date, String pattern) {
		if (pattern == null || "".equals(pattern))
			pattern = YYYY_MM_DD_HH_MM_SS;
		if (date == null)
			return null;
		SimpleDateFormat sdf = getSimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 把日期格式化为字符串，使用默认pattern（年月日时分秒）
	 * @param date 要格式化的日期
	 * @return
	 */
	public static String formatDateToString(Date date) {
		if (date == null)
			return null;
		SimpleDateFormat sdf = getSimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		return sdf.format(date);
	}
	
	/**
	 * 把字符串按strPattern格式化为日期
	 * @param str 要转换为日期的字符串
	 * @param strPattern 日期的格式
	 * @return 有可能返回NULL 注意NPE的处理
	 * @throws Exception
	 */
	public static Date formatStringToDate(String str, String strPattern) throws Exception {
		if (isStringEmpty(str))
			return null;
		if (isStringEmpty(strPattern))
			strPattern = YYYY_MM_DD_HH_MM_SS;
		SimpleDateFormat sdf = getSimpleDateFormat(strPattern);
		return sdf.parse(str);
	}

	/**
	 * 把字符串格式化为日期
	 * @param str 要转换为日期的字符串
	 * @param strPattern 日期的格式
	 * @return 有可能返回NULL 注意NPE的处理
	 * @throws Exception
	 */
	public static Date formatStringToDate(String str) throws Exception {
		if (isStringEmpty(str))
			return null;
		SimpleDateFormat sdf = getSimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		return sdf.parse(str);
	}
	
	/**
	 * 转换两个字符串的格式
	 * @param str 要转换的字符串
	 * @param pattern 字符串的模式
	 * @param patternTo 要转换的模式
	 * @return 有可能返回NULL 注意NPE的处理
	 * @throws Exception 
	 */
	public static String exchaneDateString(String str, String pattern, String patternTo) throws Exception {
		if (isStringEmpty(str) || isStringEmpty(pattern) || isStringEmpty(patternTo)){
			return str;
		}
		Date date = formatStringToDate(str, pattern);
		return formatDateToString(date, patternTo);
	}

	/**
	 * 获取当天指定的时间
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @return
	 */
	public static Date getDateTimeOfDay(Integer hour, Integer minute, Integer second) {
		Calendar ca = Calendar.getInstance();

		hour = hour == null ? ca.get(Calendar.HOUR_OF_DAY) : hour;
		minute = minute == null ? ca.get(Calendar.MINUTE) : minute;
		second = second == null ? 0 : second;

		ca.set(Calendar.HOUR_OF_DAY, hour);
		ca.set(Calendar.MINUTE, minute);
		ca.set(Calendar.SECOND, second);
		return ca.getTime();
	}

	/**
	 * 在给定日期基础上添加天数
	 * @param date 要操作的日期
	 * @param num 添加的天数
	 * @return
	 */
	public static Date addDay(Date date, int num) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, num);
		return calendar.getTime();
	}

	/**
	 * 获取下一天
	 * @return
	 */
	public static Date getNextDay(){
		return addDay(new Date(), 1);
	}
	
	/**
	 * 获取前一天
	 * @return
	 */
	public static Date getPrevDay(){
		return addDay(new Date(), -1);
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str 要操作的字符串
	 * @return
	 */
	private static boolean isStringEmpty(String str) {
		if (str == null || "".equals(str) || "null".equals(str))
			return true;
		return false;
	}

	public static void main(String[] args) throws Exception {
		Date d = addDay(new Date(), 66);
		System.out.println(formatDateToString(d, "yyyy/MM/dd HH:mm:ss"));
		System.out.println(exchaneDateString("20170421", "yyyyMMdd", "yyyy-MM-dd"));
	}
}
