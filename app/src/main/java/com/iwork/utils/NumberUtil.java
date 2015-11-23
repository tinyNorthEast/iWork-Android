package com.iwork.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author hbbsoft@gmail.com
 * @since 14-8-7
 */
public class NumberUtil {

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Double strToDouble(String str) {
		try {
			if (TextUtil.isEmpty(str))
				return 0.0;
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
		}
		return 0.0;
	}

	public static int strToInt(String str) {
		try {
			if (TextUtil.isEmpty(str))
				return 0;
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
		}
		return 0;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static Float strToFloat(String str) {
		try {
			if (TextUtil.isEmpty(str))
				return 0.00f;
			return Float.parseFloat(str);
		} catch (NumberFormatException e) {
		}
		return 0.00f;
	}

	/**
	 * 返回保留小数点后几位的数值，其中舍入不进位
	 * 
	 * @param value
	 *            需舍入的数值
	 * @param scale
	 *            保留小数点后的位数
	 * @return
	 */
	public static float roundByDownMode(float value, int scale) {
		if (Float.isNaN(value)) {
			value = 0;
		}
		return roundByMode(value, scale, RoundingMode.DOWN).floatValue();
	}

	public static float roundByHALF_UP(float value, int scale) {
		if (Float.isNaN(value)) {
			value = 0;
		}
		return roundByMode(value, scale, RoundingMode.HALF_UP).floatValue();
	}

	/**
	 * 
	 * @param value
	 *            需舍入的数值
	 * @param scale
	 *            保留小数点后的位数
	 * @param mode
	 *            舍入的模式，可参考 {@link RoundingMode}
	 * @return
	 */
	public static BigDecimal roundByMode(float value, int scale,
			RoundingMode mode) {
		BigDecimal bigDecimal = new BigDecimal(value);
		return bigDecimal.setScale(scale, mode);
	}

	public static String changeF2Y(int value) {
		if (value == 0) {
			return "0.0";
		}
		String valueString = value + "";
	    if (valueString.length() == 1){
			return "0.0" + value % 10;
		}else if((value % 100)<10){
			return value / 100 + ".0" + value % 100;
		}else{
			return value / 100 + "." + value % 100;
		}
	}

}
