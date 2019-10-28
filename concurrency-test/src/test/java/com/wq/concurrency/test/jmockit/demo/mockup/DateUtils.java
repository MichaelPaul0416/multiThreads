package com.wq.concurrency.test.jmockit.demo.mockup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 *
 * @author xuqw
 * @date 2019-07-30 17:03:01
 */
public class DateUtils {

    /**
     *
     */
    private DateUtils() {}

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");

    /**
     * 获取指定日期的年月日
     *
     * @return 返回当天日期,int类型
     */
    public static Integer getDay(LocalDate localDate) {
        return Integer.parseInt(getDayStr(localDate));
    }

    public static Integer getDay(LocalDateTime localDateTime) {
        return Integer.parseInt(getDayStr(localDateTime.toLocalDate()));
    }

    /**
     * 获取当天的年月日
     *
     * @return 返回当天日志，int类型
     */
    public static Integer getDay() {
        return getDay(LocalDate.now());
    }

    /**
     * 获取指定日期的时间
     *
     * @return 获取当天时间,int 类型
     */
    public static Integer getTime(LocalTime localTime) {
        return Integer.parseInt(getTimeStr(localTime));
    }

    public static Integer getTime(LocalDateTime localDateTime) {
        return Integer.parseInt(getTimeStr(localDateTime.toLocalTime()));
    }

    /**
     * 获取当前时间
     *
     * @return int 表示
     */
    public static Integer getTime() {
        return getTime(LocalTime.now());
    }

    /**
     * 获取当前时间
     *
     * @param localDateTime
     * @return 获取当天日期时间,Long类型
     */
    public static Long getDayAndTime(LocalDateTime localDateTime) {
        return Long.parseLong(getDayTime(localDateTime));
    }

    /**
     * 获取当前时间
     *
     * @param localDateTime
     * @return 获取当天日期时间,String
     */
    public static String getDayTime(LocalDateTime localDateTime) {
        LocalDateTime dateTime = localDateTime == null ? LocalDateTime.now() : localDateTime;
        return getDayStr(dateTime.toLocalDate()) + getTimeStr(dateTime.toLocalTime());

    }

    /**
     * 获取格式化之后的时间格式
     *
     * @param day
     *            日期
     * @param time
     *            时间
     * @return 返回格式化之后的日期 20160908 12:11:10
     */
    public static String getWholeDateTime(Integer day, Integer time) {
        StringBuilder result = new StringBuilder(String.valueOf(day)).append(" ");
        for (int i = time.toString().length(); i < 6; i++) {
            result.append("0");
        }
        result.append(time);
        return result.toString();
    }

    public static String getTimeStr(LocalTime localTime) {
        LocalTime time = localTime == null ? LocalTime.now() : localTime;
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();
        return cover(hour) + cover(minute) + cover(second);
    }

    public static String getDayStr(LocalDate localDate) {
        LocalDate today = null == localDate ? LocalDate.now() : localDate;
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        return cover(year) + cover(month) + cover(day);
    }

    private static String cover(Integer value) {
        return value < 10 ? "0" + value : value + "";
    }

    /**
     * 判断是否为同一天 比较年月日
     *
     */
    public static boolean isSameDay(Integer param1, Integer param2) {
        return param1.equals(param2);
    }

    /**
     * 把数字转为日期时间格式
     *
     * @param day
     * @param time
     * @since 5.1.0.007
     * @return
     */
    public static LocalDateTime convertDateTime(Integer day, Integer time) {
        return LocalDateTime.parse(DateUtils.getWholeDateTime(day, time), DateUtils.DATETIME_FORMATTER);
    }

    /**
     * 获取当前时间的恒生日期时间
     *
     * @return
     */
    public static long getHsDateTime() {
        return getHsDateTime(LocalDateTime.now());
    }

    /**
     * 获取当前时间的恒生日期时间
     *
     * @return
     */
    public static long getHsDateTime(LocalDateTime localDateTime) {
        localDateTime = localDateTime == null ? LocalDateTime.now() : localDateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return Long.parseLong(localDateTime.format(formatter));
    }

    public static long getHsDateTime(LocalDateTime localDateTime, String pattern) {
        localDateTime = localDateTime == null ? LocalDateTime.now() : localDateTime;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return Long.parseLong(localDateTime.format(formatter));
    }

    public static int getHsDate() {
        return (int)getHsDateTime(LocalDateTime.now(), "yyyyMMdd");
    }

}
