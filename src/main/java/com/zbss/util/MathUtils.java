package com.zbss.util;

public class MathUtils {

	public static Double EPS = 0.00000000001;

	public static Double abs(Double num){
		if (num > 0){
			return num;
		}
		return -num;
	}

	/**
	 * 牛顿迭代法求平方根近似值
	 * @param num
	 * @return
	 */
	public static Double sqrt(Double num){
		Double result = num;
		while (true){
			Double last = result;
			result = result - result/2.0 + num/2.0/result;
			if (abs(result - last) < EPS){
				break;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(sqrt(4.0));
	}

}
