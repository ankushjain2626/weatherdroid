package cn.tearcry.api.weather.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import cn.tearcry.api.weather.WeatherKey;

/* 
 * Copyright (C) 2008 Rajab Ma <majianle@gmail.com>
 * http://www.tearcry.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */

/**
 * 进行单位制式的转换
 * 
 * @author Rajab Ma<majianle@gmail.com>
 * 
 */

public class UnitConvert {

	/** 所有风向名称 */
	private static final String[] WIND_DIRECTIONS = { "N", "NNE", "NE", "ENE",
			"E", "ESE", "ES", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW",
			"NNW" };

	private static final String OUT_FORMATTER = "yyyy-MM-dd HH:mm";

	public static String convertTime(String informat, String date) {
		return convertTime(informat, OUT_FORMATTER, date);
	}

	public static String convertTime(String informat, String outformat,
			Locale locale, String date) {

		SimpleDateFormat in_formatter = new SimpleDateFormat(informat,
				Locale.US);
		SimpleDateFormat out_formatter = new SimpleDateFormat(outformat, locale);
		GregorianCalendar now = new GregorianCalendar();
		Date time = null;

		String ret = null;
		try {
			time = in_formatter.parse(date);
			ret = out_formatter.format(time);
		} catch (ParseException ex) {
			ex.printStackTrace();
			ret = out_formatter.format(now.getTime());
		}
		return ret;
	}

	public static String convertTime(String informat, String outformat,
			String date) {

		return convertTime(informat, outformat, Locale.US, date);
	}

	/**
	 * @param value
	 *            要转换的值
	 * @param unit
	 *            待转换的单位
	 * @param ratio
	 *            换算比率
	 * @param metricUnit
	 *            相应的公制单位
	 * @param standardUnit
	 *            相应的标准制单位
	 * @return 转换后的(包括单位）
	 */
	private static String convert(float value, String unit, float ratio,
			String metricUnit, String standardUnit, boolean needRound) {
		float ret = 0.0f;
		String ut = null;

		// 公制->标准制
		if (fromMetricSystem(unit)) {
			ret = value / ratio;
			ut = standardUnit;
		} else {
			// 标准制->公制
			ret = value * ratio;
			ut = metricUnit;
		}

		return needRound ? Math.round(ret) + " " + ut : (float) (Math
				.round(ret * 100))
				/ 100 + " " + ut;

	}

	/**
	 * 温度转换
	 * 
	 * @param value
	 *            待转换的温度值
	 * @param unit
	 *            待转换的温度单位
	 * @return 转换后的(包括单位）
	 */
	public static String convertTemp(String value, String unit) {
		float ret = 0.0f;
		int val = 0;

		try {
			val = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return value + " " + unit;
		}

		String ut = null;

		// 摄氏->华氏
		if (fromMetricSystem(unit)) {
			ret = 1.8f * val + 32;
			ut = "F";
		} else {
			// 华氏->摄氏
			ret = (val - 32) / 1.8f;
			ut = "℃";
		}
		return Math.round(ret) + " " + ut;
	}

	/**
	 * 转换（风）速度
	 * 
	 * @param value
	 *            待转换的速度值
	 * @param unit
	 *            待转换的速度单位
	 * @return 转换后的速度(包括单位） *
	 */
	public static String convertWind(String value, String unit) {
		int val = 0;
		try {
			val = Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return value + " " + unit;
		}
		return convert(val, unit, 1.609334f, WeatherKey.Unit.KPH,
				WeatherKey.Unit.MPH, true);
	}

	/**
	 * 转换气压
	 * 
	 * @param value
	 *            待转换的气压值（浮点）
	 * @param unit
	 *            待转换的气压单位
	 * @return 转换后的气压+单位<br>
	 *         如: 23.4 inches <I>可以使用split(" ")[0]和split(" ")[1] 分别获得气压值和单位</I>
	 */
	public static String convertPressure(String value, String unit) {
		float val = 0.0f;
		try {
			val = Float.valueOf(value).floatValue();
		} catch (NumberFormatException ex) {
			return value + " " + unit;
		}

		return convert(val, unit, 33.86389f, WeatherKey.Unit.HPA,
				WeatherKey.Unit.INCHES, false);
	}

	/**
	 * 检查是否来自公制单位系统
	 * 
	 * @param unit
	 *            待检查的单位
	 * @return 是否为公制单位系统
	 */
	public static boolean fromMetricSystem(String unit) {
		if (unit.equalsIgnoreCase(WeatherKey.Unit.KM)
				|| unit.equalsIgnoreCase(WeatherKey.Unit.Celsius)
				|| unit.equalsIgnoreCase(WeatherKey.Unit.KPH)
				|| unit.equalsIgnoreCase(WeatherKey.Unit.HPA)
				|| unit.equalsIgnoreCase(WeatherKey.Unit.METRIC))
			return true;
		else
			return false;
	}

	/**
	 * 将角度转转换为对应的风向
	 * 
	 * @param degrees
	 *            要转换的角度
	 * @return 对应的风向
	 */
	public static String converWindDirection(String degrees) {
		int deg = 0;
		try {
			deg = Integer.parseInt(degrees);
		} catch (NumberFormatException ex) {
			return WeatherKey.NA;
		}
		float stepSize = 360 / WIND_DIRECTIONS.length;
		int index = Math.round(deg / stepSize);
		return WIND_DIRECTIONS[index % 16];
	}

	/**
	 * 转换气压状态
	 * 
	 * @param state
	 *            气压数字状态0-2
	 * @return 气压状态箭头 <br>
	 *         →不变（或未知） ↑上升 ↓下降
	 */
	public static String convertPressureState(String state) {
		int st = 0;
		try {
			Integer.parseInt(state);
		} catch (NumberFormatException ex) {

		}
		String ret = "";
		switch (st) {
		case 0:
			ret = "→";
			break;
		case 1:
			ret = "↑";
			break;
		case 2:
			ret = "↓";
			break;
		default:
			ret = "→";
		}
		return ret;

	}

	public static String convertDistance(String value, String unit) {
		if (value.equals("999"))
			return WeatherKey.NA;
		float val = 0.0f;
		try {
			val = Float.valueOf(value).floatValue();
		} catch (NumberFormatException ex) {
			return value + " " + unit;
		}

		return convert(val, unit, 1.609394f, WeatherKey.Unit.KM,
				WeatherKey.Unit.MILES, true);
	}

	public static GregorianCalendar getCalendar(String informat,
			String outformat, String time) {
		GregorianCalendar cale = null;
		Date date = null;
		SimpleDateFormat in = new SimpleDateFormat(informat, Locale.US);
		SimpleDateFormat out = new SimpleDateFormat(outformat, Locale.US);
		try {
			date = in.parse(time);
			cale = new GregorianCalendar();
			cale.setTime(date);
		} catch (ParseException ex) {

		}
		return cale;
	}

	public static boolean isLate(String dateStr1, String formatter1,
			String dateStr2, String formatter2) {
		boolean ret = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat(formatter1, Locale.US);
		SimpleDateFormat sdf2 = new SimpleDateFormat(formatter2, Locale.US);

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf1.parse(dateStr1);
			date2 = sdf2.parse(dateStr2);
			if (!date1.after(date2) && !date1.before(date2))
				ret = false;
			else
				ret = date1.after(date2);
		} catch (ParseException ex) {
			ret = false;
		}
		return ret;
	}

	public static void main(String[] args) {
		System.out.println(isLate("2008-4-9 15:17:00","yyyy-MM-dd HH:mm:ss","2008-4-9 15:17:00","yyyy-MM-dd HH:mm:ss"));

	}
}
