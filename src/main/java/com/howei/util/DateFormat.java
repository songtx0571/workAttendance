package com.howei.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {

    /**
     * 获取当前时间
     * yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getYMDHMS(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String created=sdf.format(date);
        return created;
    }

    /**
     * 获取当前时间
     * yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String getYMDHM(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String created=sdf.format(date);
        return created;
    }

    /**
     * 当前时间加上Hour
     * @param Hour
     * @return
     */
    public static String getBehindTime(String Hour){
        long currentTime = System.currentTimeMillis() ;
        currentTime +=Integer.parseInt(Hour)*60*60*1000;//小时
        Date date=new Date(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String created=dateFormat.format(date);
        return created;
    }

    /**
     * 指定时间加上Hour
     * @param Hour
     * @return
     */
    public static String getBehindTime2(String time,String Hour) throws ParseException{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bt=sdf.parse(time);
        long currentTime =bt.getTime();
        currentTime +=Integer.parseInt(Hour)*60*60*1000;//小时
        Date date=new Date(currentTime);
        String created=sdf.format(date);
        return created;
    }

    /**
     * 当前时间加上minute
     * @param minute
     * @return
     */
    public static String getBehindTime3(String minute){
        long currentTime = System.currentTimeMillis() ;
        currentTime +=Integer.parseInt(minute)*60*1000;//分钟
        Date date=new Date(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String created=dateFormat.format(date);
        return created;
    }

    /**
     * 比较两个时间大小
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean comparetoTime(String beginTime,String endTime) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date bt=sdf.parse(beginTime);
        Date et=sdf.parse(endTime);
        if (bt.before(et)){
            return true;
        }else{
            if(beginTime.equals(endTime)){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * 获取格式：yyyy-MM-dd
     * @return
     */
    public static String getYMD(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String created=sdf.format(new Date());
        return created;
    }


    public static String getBothDate(String beginTime,String endTime)throws ParseException{
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long bt=sdf.parse(beginTime).getTime();
        long et=sdf.parse(endTime).getTime();
        long diff=(et-bt);
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        String result="";
        if(day<1&&hour>1){
            result=hour + "时" + min + "分钟";
        }else if(day<1&&hour<1){
            result=min + "分钟";
        }else{
            result=day + "天" + hour + "时" + min + "分钟";
        }
        return result;
    }

    /**
     * 根据条件返回当月的第一天/最后一天
     * @param day
     * @return
     */
    public static String ThisMonthDay(String day){
        Calendar cale = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday, lastday;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime());
        if(day.equals("frist")){
            return firstday;
        }else if(day.equals("end")){
            return lastday;
        }
        return "";
    }

    /**
     * 获取本月的总天数
     * @return
     */
    public static int ThisMonthDay(){
        Calendar cal = java.util.Calendar.getInstance();
        int maxDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    /**
     * 获取指定月份的天数
     * @param datetime
     * @return
     */
    public static int getDaysOfMonth(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date=sdf.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前月份:YYMM
     * @return
     */
    public static String ThisMonth(){
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        return month<10?year+"-0"+month:year+"-"+month;
    }

    /**
     * 获取当前月份:MM
     * @return
     */
    public static String ThisMonthOnly(){
        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH)+1;
        return month<10 ? "-0"+month : "-"+month;
    }

    /**
     * 获取上一月份
     * @return
     */
    public static String beforeMonth(){
        Calendar calendar=Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        if(month==0){
            year--;
            month=12;
        }
        return month<10?year+"-0"+month:year+"-"+month;
    }

    /**
     * 返回傳入年月的前或后第n个月的年月, 如果 lisdateYearmonth 为now,则代表当前年月
     * eg: ("202012", -1) -> 202011 ; ("202012", 2) -> 202102
     */
    public static String getYearMonthByMonth(String lisdateYearmonth, int interval) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        if (!"now".equals(lisdateYearmonth)) {
            try{
                date = format2.parse(lisdateYearmonth);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM");
        c.add(Calendar.MONTH, interval);
        String time = format.format(c.getTime());
        return time;
    }

}
