package net.ahzz.share.sharejpa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 * 
 * @author 呼唤
 * 
 */
public class TimeUtil extends BaseUtil {

	public transient final static SimpleDateFormat hmsformat = new SimpleDateFormat(
			"hh:mm:ss");
	public transient final static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");
	public transient final static SimpleDateFormat simpleFormat = new SimpleDateFormat(
			"yyyyMMddhhmmss");
	public transient final static SimpleDateFormat sssFormat = new SimpleDateFormat(
			"yyyyMMddhhmmssSSS");
	public transient final static SimpleDateFormat dayformat = new SimpleDateFormat(
			"yyyy-MM-dd");
	public transient final static SimpleDateFormat monthFormat = new SimpleDateFormat(
			"yyyy-MM");
	public transient final static SimpleDateFormat format1 = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public transient final static SimpleDateFormat format2 = new SimpleDateFormat(
			"yyyyMMdd");
	public transient final static SimpleDateFormat format3=new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss:SSS");
    private static final String UTC_FORMATTER_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static Map<String, String> patterns;
    public static String PT_timelong = "timelong";
    public static String PT_D1 = "yyyy-MM-dd";
    public static String PT_D2 = "yyyy.MM.dd";
    public static String PT_D3 = "yyyy/MM/dd";
    public static String PT_M1 = "yyyy-MM";
    public static String PT_M2 = "yyyy/MM";
    public static String PT_T1 = "yyyy-MM-dd hh:mm:ss";
    public static String PT_T2 = "yyyy-MM-dd HH:mm:ss";
    public static String PT_T3 = "yyyyMMddHHmmss";
    public static String PT_T4 = "yyyy/MM/dd hh:mm:ss";
    public static String PT_T5 = "hh:mm:ss";
    public static String PT_T6 = "hh:mm";

    static {
        patterns = new HashMap<String, String>();
        patterns.put(PT_timelong, "");
        patterns.put(PT_D1, "(\\d){4}-(\\d){2}-(\\d){2}");
        patterns.put(PT_D2, "(\\d){4}.(\\d){2}.(\\d){2}");
        patterns.put(PT_D3, "(\\d){4}/(\\d){2}/(\\d){2}");

        patterns.put(PT_M1, "(\\d){4}-(\\d){2}");
        patterns.put(PT_M2, "(\\d){4}/(\\d){2}");

        patterns.put(PT_T1,
                "(\\d){4}-(\\d){2}-(\\d){2}\\s(\\d){2}:(\\d){2}:(\\d){2}");
        patterns.put(PT_T3,	"(\\d){4}(\\d){2}(\\d){2}(\\d){2}(\\d){2}(\\d){2}");
        patterns.put(PT_T4,
                "(\\d){4}/(\\d){2}/(\\d){2}\\s(\\d){2}:(\\d){2}:(\\d){2}");
        patterns.put(PT_T5,
                "(\\d){2}:(\\d){2}:(\\d){2}");
        patterns.put(PT_T6,
                "(\\d){2}:(\\d){2}");

    }
	
	public static String getCurrentTimeYYYYMMDDStr() {
		return format.format(new Date());
	}

	public static String getCurrentDate() {
		return dayformat.format(new Date());
	}

	public static String getCurrentDayYYYYMMMDDStr() {
		return format2.format(new Date());
	}

	/**
	 * 得到两个时间的毫秒差
	 * 
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getMillisDiff(String stime, String etime, String format)
			throws Exception {
		if (!empty(format) && !empty(stime) && !empty(etime)) {
			SimpleDateFormat df = new SimpleDateFormat(format);
			Date d1 = df.parse(stime);
			Date d2 = df.parse(etime);
			Calendar c1 = Calendar.getInstance();
			c1.setTime(d1);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(d2);
			long diff = c2.getTimeInMillis() - c1.getTimeInMillis();
			return diff;
		}
		return 0;
	}

	/**
	 * 得到两个时间的分钟差
	 *
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getMinuteDiff(String stime, String etime, String format)
			throws Exception {
		long millisDiff = getMillisDiff(stime, etime, format);
		long munuteDiff = (millisDiff / new Long("60000"));
		return munuteDiff;
	}

	/**
	 * 得到指定时间与当前时间的分钟差
	 *
	 * @param stime
	 *            起始时间
	 * @return 返回毫秒
	 */
	public static long getCurrentMinuteDiff(String stime)
			throws Exception {
		String etime = getCurrentTime();
		long millisDiff = getMillisDiff(stime, etime, "yyyy-MM-dd HH:mm:ss");
		long munuteDiff = (millisDiff / new Long("60000"));
		return munuteDiff;
	}

	/**
	 * 得到两个时间的日期差
	 * 
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getHourDiff(String stime, String etime, String format)
			throws Exception {
		long millisDiff = getMillisDiff(stime, etime, format);
		long hourDiff = (millisDiff / new Long("3600000"));
		return hourDiff;
	}

	/**
	 * 得到两个时间的日期差
	 * 
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getDayDiff(String stime, String etime, String format)
			throws Exception {
		long millisDiff = getMillisDiff(stime, etime, format);
		long dayDiff = (millisDiff / new Long("86400000"));
		return dayDiff;
	}

	/**
	 * 得到两个时间的月份差
	 * 
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getMonthDiff(String stime, String etime, String format)
			throws Exception {
		long millisDiff = getMillisDiff(stime, etime, format);
		long dayDiff = (millisDiff / new Long("2592000000"));
		return dayDiff;
	}

	/**
	 * 得到两个时间的月份差
	 * 
	 * @param stime
	 *            起始时间
	 * @param etime
	 *            结束时间
	 * @param format
	 *            时间格式
	 * @return 返回毫秒
	 */
	public static long getYearDiff(String stime, String etime, String format)
			throws Exception {
		long millisDiff = getMillisDiff(stime, etime, format);
		long dayDiff = (millisDiff / new Long("31104000000"));
		return dayDiff;
	}

	public static void main(String s[]) {
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sevenDay = TimeUtil.getTimeNdaysAgoForyyyyMMdd(sdf.format(new Date()), 7);
		String thisMonth = TimeUtil.getCurrentTimeYYYYMMStr();
		String lastMonth = TimeUtil.getLastMonth();
		String beforeLastMonth = TimeUtil.getBeforeLastMonth();
		System.out.println("sevenDay="+sevenDay);
		System.out.println("thisMonth="+thisMonth);
		System.out.println("lastMonth="+lastMonth);
		System.out.println("beforeLastMonth="+beforeLastMonth);
		String stime = "2012-12-26 10";
		String etime = "2012-12-28 13";
		String format = "yyyy-MM-dd hh";
		long dayDiff;
		try {
			dayDiff = getHourDiff(stime, etime, format);
			System.out.println(" dayDiff " + dayDiff);
			Str
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		try{
		//	String time = "20200825155744";
		//	System.out.println(format(time,PT_T3,""));


		}catch (Exception e){
			e.printStackTrace();
		}

	}

	/**
	 * 获取当前年月，格式yyyy-MM
	 * 
	 * @return
	 */
    public static String getCurrentTimeYYYYMMStr() {
        return monthFormat.format(new Date());
    }

	/**
	 * 获取当前年月，格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTimeYYYYMMDDHHMMSSStr() {
		return format1.format(new Date());
	}
	/**
	 * 获取当前年月，格式yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrentTime() {
		return format1.format(new Date());
	}
	/**
 * 获取当前年月精确到毫秒，格式yyyy-MM-dd HH:mm:ss:SSS
 */
	public static String getCurrentTimeYYYYMMDDHHMMSSS() {
		return simpleFormat.format(new Date());
	}
    public static String getCurrentTimeYYYYMMDDHHMMSSSSS() {
        return sssFormat.format(new Date());
    }
	/**
	 * 获取当前月最后一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public  static String getMonthLastDay(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		Date lastDate = cal.getTime();
		return dayformat.format(lastDate);
	}

	/**
	 * 获取上个月
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.add(Calendar.MONTH, -2);
		Date date = cal.getTime();
		return monthFormat.format(date);
	}
	/**
	 * 得到上上个月
	 * @return
	 */
	public static String getBeforeLastMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -2);
		Date date = cal.getTime();
		return monthFormat.format(date);
	}

	/**
	 * 获取yyyy-MM的Date型日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date getYYYYMMDDHHMMSSDate() throws ParseException {
		String strDate = monthFormat.format(new Date());
		Date date = monthFormat.parse(strDate);
		return date;
	}

	/**
	 * 获取yyyy-mm-dd昨天日期
	 * 
	 * @return
	 */
	public static String getYesterday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dayformat.format(cal.getTime());
	}

	/**
	 * 得到12个小时以前的时间
	 * 
	 * @return
	 */
	public static String get12HoursAgoTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -12);
		return format1.format(cal.getTime());
	}
	
	/**得到3分钟以前的时间点*/
	public  static String getThreeMinutesAgoTime(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE,-3);
		return format1.format(cal.getTime());
	}

	/**
	 * 得到time时间点n天以前的时间点yyyy-MM-dd HH:mm:ss
	 * @param time
	 * @param n
	 * @return
	 */
	public static String getTimeNdaysAgoByTime(String time,int n) {
		Date date = new Date();
		try {
			date = format1.parse(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -n);
			date = calendar.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return format1.format(date);
	}
	
	/**
	 * 得到time时间点n天以前的时间点yyyy-MM-dd
	 * @param time
	 * @param n
	 * @return
	 */
	public static String getTimeNdaysAgoForyyyyMMdd(String time,int n) {
		Date date = new Date();
		try {
			date = dayformat.parse(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, -n);
			date = calendar.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayformat.format(date);
	}
	
	/**
	 * 得到time时间点n月以前的时间点yyyy_MM
	 * @param time
	 * @param n
	 * @return
	 */
	public static String getTimeNmonthsAgoByTime(String time,int n) {
		Date date = new Date();
		try {
			date = monthFormat.parse(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -n);
			date = calendar.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return monthFormat.format(date);
	}

	public static String getTimeThreeDaysAgoByNow() {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -3);
		date = calendar.getTime();
		return format1.format(date);
	}
	
	/**
	 * 显示两个时间段的每一天 格式yyyy-MM-dd
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> displayBetweenTwoDay(String startTime,String endTime){
		List<String> dates = new ArrayList<String>();
		try{
			Date start = dayformat.parse(startTime);
			Date end = dayformat.parse(endTime);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(start);
			while(calendar.getTime().before(end)){
				dates.add(dayformat.format(calendar.getTime()));
				calendar.add(Calendar.DAY_OF_MONTH, 1);
			}
			dates.add(dayformat.format(end));
		}catch(Exception e){
			e.printStackTrace();
		}
		return dates;
	}
	
	/**
	 * 尝试将字符串转换成时间类型，目前只做简单的时间格式处理
	 * 
	 * @param str
	 * @return
	 */
	public static Date parse(String str) throws Exception {
		Date date = new Date();
		String pattern = getMatchPattern(str);
		if (!empty(pattern)) {
			if (PT_timelong.equals(pattern)) {
				date.setTime(Long.valueOf(str));
			} else {
				System.out.println("pattern " + pattern);
				DateFormat format = new SimpleDateFormat(pattern);
				date = format.parse(str);
			}
		}
		return date;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String getMatchPattern(String str) {
		boolean checkIsTimelong = _checkIsTimelong(str);
		if (checkIsTimelong) {
			return PT_timelong;
		}
		Iterator<String> it = patterns.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String pattern = patterns.get(key);
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			boolean match = m.matches();
			if (match) {
				return key;
			}
		}
		return null;
	}

	/**
	 * 这个判断不精准，待改善
	 * 
	 * @param str
	 * @return
	 */
	private static boolean _checkIsTimelong(String str) {
		Pattern p = Pattern.compile("^\\d{12,}$");
		Matcher m = p.matcher(str);
		boolean match = m.matches();
		if (match) {
			return true;
		}
		return false;
	}

	/**
	 * 计算两个时间的日期差,先格式化解决时间格式可能不同的问题。
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算两个时间的小时差,先格式化解决时间格式可能不同的问题。
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int hoursBetween(Date smdate, Date bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	
	public static String format(long time){
		Date date = new Date(time);
		return format1.format(date);
	}

	public static String format(Date time,String format){
	    if(!BaseUtil.empty(format)){
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
	        return simpleDateFormat.format(time);
        }
		return format1.format(time);
	}

	/**
	 * 得到当前时间指定小时后的时间
	 * @param hours
	 * @return
	 */
	public static Date  getTimeAfterHours(int hours){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY,hours);
		return  cal.getTime();
	}

	/**
	 * 判断当前时间与指定时间,是否过期
	 * @param date
	 * @return
	 */
	public static boolean expried(Date date){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        return  c1.compareTo(c2)>0;
	}

	/**
	 * 判断当前时间与指定时间,是否过期
	 * @param time
	 * @return
	 */
	public static boolean expried(String time) throws Exception{
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Date date = parse(time);
		c2.setTime(date);
		return  c1.compareTo(c2)>0;
	}


    /**
     * 得到当前的星期，一年中的
     * @return
     */
	public static int getCurrentWeek(){
        Calendar c1 = Calendar.getInstance();
        return c1.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 得到当前年份
     * @return
     */
    public static int getCurrentYear(){
        Calendar c1 = Calendar.getInstance();
        return c1.get(Calendar.YEAR);
    }

	/**
	 * 得到周几，周日为0
	 * @return
	 */
	public static int getCurrentDay(){
		Calendar c1 = Calendar.getInstance();
		return c1.get(Calendar.DAY_OF_WEEK)-1;
	}

	/**
	 * 得到周几，周日为0
	 * @return
	 */
	public static String getCurrentDayStr(){
		Calendar c1 = Calendar.getInstance();
		int day =  c1.get(Calendar.DAY_OF_WEEK)-1;
		String s ="";
		switch (day){
            case 0:
                s =  "星期天";break;
            case 1:
                s= "星期一";break;
            case 2:
                s = "星期二";break;
            case 3:
                s = "星期三";break;
            case 4:
                s = "星期四";break;
            case 5:
                s =  "星期五";break;
            case 6:
                s = "星期六";break;
            default:
                s = "";
        }
        return s;
	}

    /**
     * 得到日期的星期中文文字
     * @param dateStr
     * @return
     * @throws Exception
     */
	public static String getDayStr(String dateStr) throws Exception{
		Date date = parse(dateStr);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		int day =  c1.get(Calendar.DAY_OF_WEEK)-1;
		String s ="";
		switch (day){
			case 0:
				s =  "星期天";break;
			case 1:
				s= "星期一";break;
			case 2:
				s = "星期二";break;
			case 3:
				s = "星期三";break;
			case 4:
				s = "星期四";break;
			case 5:
				s =  "星期五";break;
			case 6:
				s = "星期六";break;
			default:
				s = "";
		}
		return s;
	}

	public static int getDay(String  date) throws Exception{
        Calendar c1 = Calendar.getInstance();
        Date date1 = parse(date);
        c1.setTime(date1);
        return c1.get(Calendar.DAY_OF_WEEK)-1;
    }

	/**
	 * 将字符串的时间，转成其他格式的时间
	 * @param date
	 * @param sformat 时间源格式
     * @param tformat 目标格式，默认使用yyyy-MM-dd hh:mm:ss
	 * @return
	 * @throws Exception
	 */
	public static String format(String  date,String sformat,String tformat) throws Exception{
		if(BaseUtil.empty(tformat)){
			tformat = PT_T2;
		}
		Date date1 = new SimpleDateFormat(sformat).parse(date);
		return format(date1,tformat);
	}

}
