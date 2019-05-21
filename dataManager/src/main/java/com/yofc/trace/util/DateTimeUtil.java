package com.yofc.trace.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public class DateTimeUtil {

	public static String format(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	public static Date parse(String dateStr, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算2个时间差
	 * 
	 * @param date1
	 * @param date2
	 * @param day
	 * @return
	 */
	public static int diffDate(Date date1, Date date2, int day) {
		if (Calendar.DAY_OF_MONTH == day) {
			int days = (int) (date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24);
			return days;
		} else {
			return -1;
		}
	}

	/**
	 * 给一个日期加多少天
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDaysForDate(Date date, int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	/**
	 * 给一个日期加多少月
	 * 
	 * @param date
	 * @param mons
	 * @return
	 */
	public static Date addMonsForDate(Date date, int mons) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, mons);
		return c.getTime();
	}

	/**
	 * 给今天加多少天
	 * 
	 * @param days
	 * @return
	 */
	public static Date addDaysForToday(int days) {
		return addDaysForDate(new Date(), days);
	}

	/**
	 * 给指定时间加多少天
	 * 
	 * @param days
	 * @return
	 */
	public static Date addDaysForToday(Date date, int days) {
		return addDaysForDate(date, days);
	}

	/**
	 * 昨天到前30天的日期
	 * 
	 * @return
	 */
	public static String[] getMondayBefore(Date date1) {
		String[] date = new String[30];
		for (int i = -29; i <= 0; i++) {
			date[i + 29] = formatDate(addDaysForDate(date1, i));
		}
		return date;
	}

	/**
	 * 当天到前30天的日期
	 * 
	 * @return
	 */
	public static String[] getMonday() {
		String[] date = new String[30];
		for (int i = -29; i <= 0; i++) {
			date[i + 29] = formatDate(addDaysForToday(i));
		}
		return date;
	}

	/**
	 * 设置表单参数起始时间，设置时分秒均为0
	 * 
	 * @param date
	 * @return
	 */
	public static Date setBeginTimeInDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 设置表单参数起始时间，秒为0
	 * 
	 * @param date
	 * @return
	 */
	public static Date setSecond(Date date, int s) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.SECOND, s);
		return c.getTime();
	}

	/**
	 * 设置表单参数结束时间，设置时分秒为235959
	 * 
	 * @param date
	 * @return
	 */
	public static Date setEndTimeInDate(Date date) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	/**
	 * 获取今天开始时间
	 * 
	 * @return
	 */
	public static Date getTodayBeginTime() {
		Calendar c = Calendar.getInstance();
		return setBeginTimeInDate(c.getTime());
	}

	/**
	 * 获取今天结束时间
	 * 
	 * @return
	 */
	public static Date getTodayEndTime() {
		Calendar c = Calendar.getInstance();
		return setEndTimeInDate(c.getTime());
	}

	/**
	 * 获取昨日开始时间
	 * 
	 * @return
	 */
	public static Date getYesterDayBeginTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		return setBeginTimeInDate(c.getTime());
	}

	/**
	 * 获取明天开始时间
	 * 
	 * @return
	 */
	public static Date getTomoryBeginTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);
		return setBeginTimeInDate(c.getTime());
	}

	public static Date getYesterDayEndTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		return setEndTimeInDate(c.getTime());
	}

	/**
	 * 获取本月开始时间
	 * 
	 * @return
	 */
	public static Date getThisMonthBeginTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		return setBeginTimeInDate(c.getTime());
	}

	/**
	 * 获取本月结束时间
	 * 
	 * @return
	 */
	public static Date getThisMonthEndTime() {
		Calendar c = Calendar.getInstance();
		return setEndTimeInDate(c.getTime());
	}

	/**
	 * 获取上月开始时间
	 * 
	 * @return
	 */
	public static Date getLastMonthBeginTime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return setBeginTimeInDate(c.getTime());
	}

	/**
	 * 获取上月结束时间
	 * 
	 * @return
	 */
	public static Date getLastMonthEndTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 0);
		return setEndTimeInDate(c.getTime());
	}

	public static Date getOneMonthAfterDateTime(int days) {
		Date now = new Date();
		return setBeginTimeInDate(DateUtils.addDays(now, days));
	}

	public static Date parseDateTime(String dateStr) {
		return parse(dateStr, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 显示月日: 12.22
	 * 
	 * @param date
	 * @return
	 */
	public static String monthDay(Date date) {
		if (date == null) {
			return "";
		}
		return format(date, "MM-dd");
	}

	/**
	 * 显示月日: 12月22日
	 * 
	 * @param date
	 * @return
	 */
	public static String monthDayStr(Date date) {
		if (date == null) {
			return "";
		}
		return format(date, "MM月dd日");
	}

	/**
	 * 获取今天日期 yyyyMMdd
	 * 
	 * @return
	 */
	public static String getTodayStr() {
		Date date = new Date();
		return format(date, "yyyy-MM-dd");
	}

	/**
	 * 获取今天日期 yyyyMMdd
	 * 
	 * @return
	 */
	public static String getTodayMM() {
		Date date = new Date();
		return format(date, "yyyy-MM");
	}

	/**
	 * 获取日期 yyyyMM
	 * 
	 * @return
	 */
	public static String getDateYY(Date date) {
		return format(date, "yyyy年MM月");
	}

	/**
	 * 获取今天日期 yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeStr() {
		Date date = new Date();
		return formatDateTime(date);
	}

	/**
	 * 获取明天日期
	 * 
	 * @return
	 */
	public static String getTomorrowStr() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return format(calendar.getTime(), "yyyy-MM-dd");
	}

	/**
	 * 获取本月日期
	 * 
	 * @return
	 */
	public static String getThisMonthDate() {
		Date date = new Date();
		return format(date, "yyyyMM");
	}

	/**
	 * 获取上月日期
	 * 
	 * @return
	 */
	public static String getLastMonthDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		return format(calendar.getTime(), "yyyyMM");
	}

	/**
	 * 获取指定的时间,通过offset来指定当前小时或者前几个小时 通过isTheHour指定是否整点 返回格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param offset
	 *            0:当前小时;-1是上一个小时;依次类推
	 * @param isTheHour
	 *            是否整点
	 * @return
	 */
	public static String getSpecialTime(int offset, boolean isTheHour) {
		Calendar calendar = Calendar.getInstance();
		//

		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + offset);
		if (isTheHour) {
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
		}

		return format(calendar.getTime(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 根据时间控件传过来的时间进行格式化 timeStr = 2017-04-09T08:23:05.199Z 讲改时间格式化为yyyy-MM-dd
	 * 
	 * @param timeStr
	 *            时间空间传过来的时间格式.
	 */
	public static String getFmtTimeFromDatePicker(String timeStr) {

		LocalDateTime dateInstance = ZonedDateTime.parse(timeStr).toInstant().atZone(ZoneOffset.systemDefault())
				.toLocalDateTime();

		return dateInstance.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 *
	 * @param strDate
	 * @return
	 */
	public static Date strToDateTime(String strDate) {
		if (StringUtils.isEmpty(strDate)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date date = format.parse(strDate, pos);
		return date;
	}

	/**
	 * 计算将来某个日期距离今天的天数
	 * 
	 * @param futureDate
	 */
	public static int calculationDaysFromNow(Date futureDate) {
		if (null == futureDate) {
			return 0;
		}
		Date time1 = new Date();
		Date time2 = futureDate;
		time1 = DateUtils.round(time1, Calendar.DAY_OF_MONTH);
		time2 = DateUtils.ceiling(time2, Calendar.DAY_OF_MONTH);
		int days = (int) ((time2.getTime() - time1.getTime()) / (1000 * 60 * 60 * 24));
		return days < 0 ? 0 : days;
	}

	/**
	 * 计算将来某个日期距离今天结束的天数
	 * 
	 * @param futureDate
	 */
	public static int dayToToday(Date futureDate) {
		if (null == futureDate) {
			return 0;
		}
		Calendar a = Calendar.getInstance();
		a.setTime(futureDate);
		a.set(Calendar.HOUR_OF_DAY, 23);
		a.set(Calendar.MINUTE, 59);
		a.set(Calendar.SECOND, 59);
		a.set(Calendar.MILLISECOND, 0);

		Calendar b = Calendar.getInstance();
		b.setTime(new Date());
		b.set(Calendar.HOUR_OF_DAY, 23);
		b.set(Calendar.MINUTE, 59);
		b.set(Calendar.SECOND, 59);
		b.set(Calendar.MILLISECOND, 0);

		int days = (int) ((a.getTime().getTime() - b.getTime().getTime()) / (1000 * 60 * 60 * 24));
		return days < 0 ? 0 : days;
	}

	/**
	 * 计算两个日期差距
	 * 
	 * @param time1,time2
	 * @return
	 */
	public static long betweenTwoDay(Date time1, Date time2) {
		long day;
		try {
			long t1 = time1.getTime() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset()
					+ 24 * 60 * 60 * 1000 - 1000;// 设置目标时间为那天的23:59:59
			long t2 = time2.getTime() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset()
					+ 24 * 60 * 60 * 1000 - 1000;// 设置目标时间为那天的23:59:59
			day = (t2 - t1) / (1000 * 60 * 60 * 24) - 1;
		} catch (Exception e) {
			day = 0;
		}
		day = day < 0 ? day : day + 1;
		return day;
	}

	public static Date getDateByLong(Long date) {
		if (null == date) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		return calendar.getTime();
	}

	/**
	 * 取得当月天数
	 */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		new Date(1557906203410L);

		System.out.println(format(new Date(),"HH:mm:ss"));
	}

	/**
	 * 得到指定月的天数
	 */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}
}
