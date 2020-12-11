package org.aradin.spring.core.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Date Transfer Module
 * @author daliu
 *
 */
public class DateUtil {

	// 格式：年－月－日 小时：分钟：秒
	public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

	// 格式：年－月－日 小时：分钟
	public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

	// 格式：年月日 小时分钟秒
	public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

	public static final String FORMAT_FOUR = "yyyyMMddHHmmss";

	public static final String FORMAT_FIVE = "yyyy.MM.dd HH:mm:ss";

	public static final String FORMAT_SIX = "yyyy-MM-dd HH:mm:ss.SSS";

	// 格式：年－月－日
	public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";

	public static final String LONG_DATE_FORMAT_1 = "yyyy/MM/dd";

	public static final String LONG_DATE_FORMAT_2 = "yyyy.MM.dd";

	// 格式：年(2位).月.日
	public static final String LONG_DATE_FORMAT_3 = "yy.MM.dd";

	// 格式：月－日
	public static final String SHORT_DATE_FORMAT = "MM-dd";

	// 格式：小时：分钟：秒
	public static final String SHORT = "HH:mm:ss";

	// 格式：小时：分钟
	public static final String LONG_TIME_FORMAT = "HH:mm";

	// 格式：年-月
	public static final String MONTG_DATE_FORMAT = "yyyy-MM";

	// 格式：年月日
	public static final String SIMPLE_DATE_FORMAT = "yyyyMMdd";

	// 年的加减
	public static final int SUB_YEAR = Calendar.YEAR;

	// 月加减
	public static final int SUB_MONTH = Calendar.MONTH;

	// 天的加减
	public static final int SUB_DAY = Calendar.DATE;

	// 小时的加减
	public static final int SUB_HOUR = Calendar.HOUR;

	// 分钟的加减
	public static final int SUB_MINUTE = Calendar.MINUTE;

	// 秒的加减
	public static final int SUB_SECOND = Calendar.SECOND;

	public DateUtil() {
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return
	 */
	public static java.util.Date stringtoDate(String dateStr, String format) {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.setLenient(false);
			d = formater.parse(dateStr);
		} catch (Exception e) {
			// log.error(e);
			d = null;
		}
		return d;
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 */
	public static java.util.Date stringtoDate(String dateStr, String format, ParsePosition pos) {
		Date d = null;
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			formater.setLenient(false);
			d = formater.parse(dateStr, pos);
		} catch (Exception e) {
			// log.error(e);
			d = null;
		}
		return d;
	}

	/**
	 * 两个date数据比较大小
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int isSameDate(Date date1, Date date2) {
		int date1num = Integer.valueOf(dateToString(date1, SIMPLE_DATE_FORMAT));
		int date2num = Integer.valueOf(dateToString(date2, SIMPLE_DATE_FORMAT));
		if (date1num == date2num) {
			return 0;
		} else if (date1num > date2num) {
			return 1;
		} else {
			return -1;
		}
	}

	/**
	 * 将日期格式为为字符串，格式为 yyyy-MM-dd。如果出错，返回空串。
	 */
	public static String dateToStringLong(java.util.Date date) {
		return dateToString(date, LONG_DATE_FORMAT);
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(java.util.Date date, String format) {
		if (date == null) {
			return null;
		}
		String result = "";
		if (StringUtils.isBlank(format)) {
			format = FORMAT_ONE;
		}
		SimpleDateFormat formater = new SimpleDateFormat(format);
		try {
			result = formater.format(date);
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	/**
	 * 获取两个日期中段的日期
	 * 
	 * @param startDate
	 * @param endDate
	 * @param format
	 * @return
	 * @author chenyz
	 */
	public static List<String> getMidpieceDates(String startDate, String endDate, String format) {
		List<String> list = new ArrayList<String>();
		while (!startDate.equals(endDate)) {
			list.add(startDate);
			startDate = DateUtil.dateToString(DateUtil.nextDay(DateUtil.stringtoDate(startDate, format), 1), format);
		}
		return list;
	}

	/**
	 * 获取当前时间的指定格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrDate(String format) {
		return dateToString(new Date(), format);
	}

	/**
	 * 获取距离当天指定天数（月、年。。。）的日期
	 * 
	 * @param field
	 *            Calendar类的指定日历字段（例如Calendar.DATE, Calendar.MONTH）
	 * @param amount
	 *            数量，正数为未来，负数为过去
	 * @return 如果field不是Calendar类的指定日历字段，返回null
	 */
	public static Date dateSub(int field, int amount) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(field, amount);
			return cal.getTime();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 
	 * @param dateStr
	 * @param amount
	 * @return
	 */
	public static String dateSub(int dateKind, String dateStr, int amount) {
		Date date = stringtoDate(dateStr, FORMAT_ONE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(dateKind, amount);
		return dateToString(calendar.getTime(), FORMAT_ONE);
	}

	/**
	 * 两个日期相减
	 * 
	 * @param firstTime
	 * @param secTime
	 * @return 相减得到的秒数
	 */
	public static long timeSub(String firstTime, String secTime) {
		long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
		long second = stringtoDate(secTime, FORMAT_ONE).getTime();
		return (second - first) / 1000;
	}

	/**
	 * 获得某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getDaysOfMonth(String year, String month) {
		int days = 0;
		if (month.equals("1") || month.equals("3") || month.equals("5") || month.equals("7") || month.equals("8")
				|| month.equals("10") || month.equals("12")) {
			days = 31;
		} else if (month.equals("4") || month.equals("6") || month.equals("9") || month.equals("11")) {
			days = 30;
		} else {
			if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
					|| Integer.parseInt(year) % 400 == 0) {
				days = 29;
			} else {
				days = 28;
			}
		}

		return days;
	}

	/**
	 * 获取某年某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int 月份[1-12]
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前日期
	 * 
	 * @return int
	 */
	public static int getToday() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获得当前月份
	 * 
	 * @return int
	 */
	public static int getToMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得当前年份
	 * 
	 * @return int
	 */
	public static int getToYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的天
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 返回该日期为一周中的哪一天（周日为1，周一为2，依此类推）
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 返回该日期为当月第几天
	 */
	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回该日期为当年第几天
	 */
	public static int getDayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 返回当月最后一天
	 */
	public static Date getLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DATE, 0);
		return calendar.getTime();
	}

	/**
	 * 返回日期的年
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的月份，1-12
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return long
	 */
	public static long dayDiff(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / 86400000;
	}

	/**
	 * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return long
	 */
	public static long dayDiff(String date1, String date2, String format) {
		return DateUtil.dayDiff(DateUtil.stringtoDate(date1, format), DateUtil.stringtoDate(date2, format));
	}

	public static long secondDiff(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime());
	}

	public static long secondDiff(String date1, String date2, String format) {
		return DateUtil.secondDiff(DateUtil.stringtoDate(date1, format), DateUtil.stringtoDate(date2, format));
	}

	/**
	 * 比较两个日期的年差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 */
	public static int yearDiff(String before, String after) {
		Date beforeDay = stringtoDate(before, LONG_DATE_FORMAT);
		Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
		return getYear(afterDay) - getYear(beforeDay);
	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 */
	public static int yearDiffCurr(String after) {
		Date beforeDay = new Date();
		Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
		return getYear(beforeDay) - getYear(afterDay);
	}

	/**
	 * 获取指定日期距离当天的天数。如果指定日期在未来，则返回负数。
	 * 
	 * @param dateStr
	 *            指定日期，格式为"yyyy-MM-dd"
	 * @return 天数。如果dateStr格式错误，返回0
	 */
	public static long dayDiffCurr(String dateStr) {
		// Date currDate = DateUtil.stringtoDate(currDay(), LONG_DATE_FORMAT);
		Date currDate = new Date();
		Date date = stringtoDate(dateStr, LONG_DATE_FORMAT);
		return date == null ? 0 : (currDate.getTime() - date.getTime()) / 86400000;
	}

	public static int getFirstWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static int getLastWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, getDaysOfMonth(year, month));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getNow() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), FORMAT_ONE);
	}

	/**
	 * 根据生日获取星座
	 * 
	 * @param birth
	 *            YYYY-mm-dd
	 * @return
	 */
	public static String getAstro(String birth) {
		if (!isDate(birth)) {
			birth = "2000" + birth;
		}
		if (!isDate(birth)) {
			return "";
		}
		int month = Integer.parseInt(birth.substring(birth.indexOf("-") + 1, birth.lastIndexOf("-")));
		int day = Integer.parseInt(birth.substring(birth.lastIndexOf("-") + 1));
		// logger.debug(month + "-" + day);
		String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		int start = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(start, start + 2) + "座";
	}

	/**
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer("^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

	/**
	 * 取得指定日期过 months 月后的日期 (当 months 为负数表示指定月之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param month
	 *            相加(相减)的月数
	 */
	public static Date nextMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 取得指定日期过 day 天后的日期 (当 day 为负数表示指定天之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param day
	 *            相加(相减)的天数
	 */
	public static Date nextDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * 取得指定日期过 day 周后的日期 (当 day 为负数表示指定月之前)
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 */
	public static Date nextWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.WEEK_OF_MONTH, week);
		return cal.getTime();
	}

	/**
	 * 获取当前的日期，格式为"yyyy-MM-dd"
	 */
	public static String currDay() {
		return DateUtil.dateToString(new Date(), DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 获取当前的时间，格式为HH:mm
	 * 
	 * @return
	 */
	public static String currTime() {
		return DateUtil.dateToString(new Date(), DateUtil.LONG_TIME_FORMAT);
	}

	/**
	 * 获取昨天的日期，格式为"yyyy-MM-dd"
	 */
	public static String befoDay() {
		return befoDay(DateUtil.LONG_DATE_FORMAT);
	}

	public static String befoDay(String format) {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), -1), format);
	}

	/**
	 * 获取明天的日期
	 */
	public static String afterDay() {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), 1), DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 取得当前时间距离1900/1/1的天数
	 * 
	 * @return
	 */
	public static int getDayNum() {
		int daynum = 0;
		GregorianCalendar gd = new GregorianCalendar();
		Date dt = gd.getTime();
		GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
		Date dt1 = gd1.getTime();
		daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));
		return daynum;
	}

	/**
	 * getDayNum的逆方法(用于处理Excel取出的日期格式数据等)
	 * 
	 * @param day
	 * @return
	 */
	public static Date getDateByNum(int day) {
		GregorianCalendar gd = new GregorianCalendar(1900, 1, 1);
		Date date = gd.getTime();
		date = nextDay(date, day);
		return date;
	}

	/** 针对yyyy-MM-dd HH:mm:ss格式,显示yyyymmdd */
	public static String getYmdDateCN(String datestr) {
		if (datestr == null)
			return "";
		if (datestr.length() < 10)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7)).append(datestr.substring(8, 10));
		return buf.toString();
	}

	/**
	 * 根据days天数计算出从****日到昨日为days天的日期组
	 * 
	 * @param days
	 * @return
	 * @author chenyz
	 */
	public static Map<String, String> getTimeslice(int days) {
		Map<String, String> map = new HashMap<String, String>();
		String beforeday = DateUtil.dateToString(DateUtil.nextDay(new Date(), -(days + 1)), DateUtil.LONG_DATE_FORMAT);
		String afterday = DateUtil.befoDay();
		map.put("beforeday", beforeday);
		map.put("afterday", afterday);
		return map;
	}

	/**
	 * 获取最近datenum天的开始日期和结束日期，结束日期从前一天开始。
	 * 
	 * @param datenum
	 *            最近天数，必须大于0
	 * @return 长度为2的数组，第一个元素为开始日期，第二个元素为结束日期，格式都为"yyyy-MM-dd"。datenum出错时返回null。
	 */
	public static String[] getDateRange(int datenum) {
		if (datenum < 1) {
			return null;
		}
		String startDate = DateUtil.dateToString(DateUtil.dateSub(Calendar.DATE, -datenum), DateUtil.LONG_DATE_FORMAT);
		String endDate = DateUtil.befoDay();
		return new String[] { startDate, endDate };
	}

	/**
	 * 获取近n天的日期数组
	 * 
	 * @param datenum
	 * @param format
	 * @return
	 */
	public static String[] getDates(int datenum, String format) {
		if (StringUtils.isBlank(format)) {
			format = DateUtil.LONG_DATE_FORMAT;
		}
		String[] dayrange = DateUtil.getDateRange(datenum - 1);
		String startday = dayrange[0];
		String[] xdata = new String[datenum];
		for (int i = 0; i < datenum; i++) {
			xdata[i] = String.valueOf(DateUtil.getDay(DateUtil.nextDay(DateUtil.stringtoDate(startday, format), i)));
		}
		return xdata;
	}

	/**
	 * 根据周期截取有效的时间段。
	 * 
	 * @param period
	 *            时间周期。可选值为：day（日），week（周），month（月），season（季度）
	 * @param startDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return 长度为2的数组，分别为开始日期和结束日期，格式为"yyyy-MM-dd"。<br>
	 *         如果period参数错误，或者指定时间段不满一个周期，返回null。
	 */
	public static String[] getDateRangeByPeriod(String period, String startDate, String endDate) {
		Date start = stringtoDate(startDate, LONG_DATE_FORMAT);
		Date end = stringtoDate(endDate, LONG_DATE_FORMAT);
		// 验证参数
		if (start == null || end == null || !("day".equals(period) || "week".equals(period) || "month".equals(period)
				|| "season".equals(period))) {
			return null;
		}

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(start);
		endCal.setTime(end);

		if ("day".equals(period)) {
			return new String[] { startDate, endDate };
		} else if ("week".equals(period)) {
			startCal.add(Calendar.DATE, (9 - startCal.get(Calendar.DAY_OF_WEEK)) % 7); // 开始日期设置为下周一（如果当天已经是周一，则不变）
			endCal.add(Calendar.DATE, 1 - endCal.get(Calendar.DAY_OF_WEEK)); // 结束日期设置为上周日（如果当天已经是周日，则不变）
		} else if ("month".equals(period)) {
			// 开始日期设置为下个月第一天（如果当天已经是当月第一天，则不变）
			if (!(startCal.get(Calendar.DATE) == 1)) {
				startCal.add(Calendar.MONTH, 1);
				startCal.set(Calendar.DATE, 1);
			}
			// 结束日期设置为上个月最后一天（如果当天已经是当月最后一天，则不变）
			endCal.add(Calendar.DATE, 1);
			endCal.set(Calendar.DATE, 0);
		} else if ("season".equals(period)) {
			// 设置开始日期为下季度第一天（如果当天已经是当季度第一天，则不变）
			if (!(startCal.get(Calendar.DATE) == 1 && startCal.get(Calendar.MONTH) % 3 == 0)) {
				startCal.set(Calendar.DATE, 1);
				startCal.add(Calendar.MONTH, 3 - startCal.get(Calendar.MONTH) % 3);
			}
			// 结束日期设置为上季度最后一天（如果当天已经是当季度最后一天，则不变）
			endCal.add(Calendar.DATE, 1);
			endCal.set(Calendar.DATE, 1);
			endCal.add(Calendar.MONTH, -endCal.get(Calendar.MONTH) % 3);
			endCal.add(Calendar.DATE, -1);
		}

		if (startCal.compareTo(endCal) > 0) { // 如果开始时间迟于结束时间，表示不满一个周期，返回null
			return null;
		}
		return new String[] { dateToString(startCal.getTime(), LONG_DATE_FORMAT),
				dateToString(endCal.getTime(), LONG_DATE_FORMAT) };
	}

	public static Date nextMinute(Date date, int min) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.MINUTE, min);
		return cal.getTime();
	}

	/**
	 * 毫秒数转时间
	 * 
	 * @param seconds
	 * @return
	 */
	public static String getDateByMillis(long millis, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return (dateToString(calendar.getTime(), format));
	}

	/**
	 * 获取指定日期所在月份的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date) {
		String dateStr = dateToString(date, LONG_DATE_FORMAT) + " 00:00:00";
		Date day = stringtoDate(dateStr, FORMAT_ONE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(day);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天最小的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayMin(Date date) {
		String dateStr = dateToString(date, LONG_DATE_FORMAT) + " 00:00:00";
		Date day = stringtoDate(dateStr, FORMAT_ONE);
		return day;
	}

	/**
	 * 获取某天的最大时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayMax(Date date) {
		String dateStr = dateToString(date, LONG_DATE_FORMAT) + " 23:59:59";
		Date day = stringtoDate(dateStr, FORMAT_ONE);
		return day;
	}

	public static Date getYesterday() {
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth - 1);
		return calendar.getTime();
	}

	/**
	 * 获取近n天的日期数组
	 * 
	 * @param datenum
	 * @param format
	 * @return
	 */
	public static String[] getRecentDates(int datenum, String format) {
		if (StringUtils.isBlank(format)) {
			format = DateUtil.LONG_DATE_FORMAT;
		}
		String[] dayrange = DateUtil.getDateRange(datenum - 1);
		String startday = dayrange[0];
		String[] xdata = new String[datenum];
		for (int i = 0; i < datenum; i++) {
			Date date = DateUtil.stringtoDate(startday, LONG_DATE_FORMAT);
			Date nextdate = DateUtil.nextDay(date, i);
			xdata[i] = dateToString(nextdate, format);
		}
		return xdata;
	}

	/**
	 * 获取指定日期所在年的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfYear(Date date) {
		Date dayMax = getDayMax(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dayMax);
		calendar.set(Calendar.MONTH, Calendar.DECEMBER);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static long getMillisByDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	public static int getHour(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMiniute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	public static Date getFirstDateOfLastMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int lastmonth = calendar.get(Calendar.MONTH) - 1;
		calendar.set(Calendar.MONTH, lastmonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public static long getDaysApart(Date date1, Date date2) {
		String date1String = dateToString(date1, LONG_DATE_FORMAT);
		String date2String = dateToString(date2, LONG_DATE_FORMAT);
		Date fstDate = stringtoDate(date1String, LONG_DATE_FORMAT);
		Date sndDate = stringtoDate(date2String, LONG_DATE_FORMAT);
		return (sndDate.getTime() - fstDate.getTime()) / 86400000;
	}
	
	/**
	 * 获取近n个月的值
	 * 
	 * @param datenum
	 * @param format
	 * @return
	 */
	public static String[] getRecentMonths(int monthnum, String format) {
		if (StringUtils.isBlank(format)) {
			format = DateUtil.LONG_DATE_FORMAT;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -(monthnum + 1));
		String[] months = new String[monthnum];
		for(int i = 0; i < monthnum; i++) {
			calendar.add(Calendar.MONTH, 1);
			Date month = calendar.getTime();
			months[i] = DateUtil.dateToString(month, format);
		}
		return months;
	}
}
