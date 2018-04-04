package com.easyclass.claraliu.core.student;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Clara liu on 2017/12/21.
 */

public class CalculateTime {
    private String term;
    private String week;
    private String weekday=null;

    public String getWeekday(){
        Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case 2:weekday="星期一";
                break;
            case 3:weekday="星期二";
                break;
            case 4:weekday="星期三";
                break;
            case 5:weekday="星期四";
                break;
            case 6:weekday="星期五";
                break;
            default:weekday="周末";
                break;
        }
        return weekday;
    }

    public String getTerm(){
        Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if((c.get(Calendar.MONTH)+1)<7&&(c.get(Calendar.MONTH)+1)>=2){
            term=String.valueOf(c.get(Calendar.YEAR))+"-2";
        }
        else if((c.get(Calendar.MONTH)+1)>=7&&(c.get(Calendar.MONTH)+1)<=12) {
            term = String.valueOf(c.get(Calendar.YEAR)) + "-1";
        }
        else{
            term = String.valueOf(c.get(Calendar.YEAR)-1) + "-1";
        }
        return term;
    }

    public String getWeek(){
        Calendar c=Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        Date endDate=new Date();

        String date=dateFormat.format(endDate);
        String s2 = "02-25";
        String s1 = "08-26";
        String m = "12-31";
        Date startDate2 = null;
        Date startDate1 = null;
        Date mDate=null;
        try{
            endDate =dateFormat.parse(date);
            startDate2=dateFormat.parse(s2);
            startDate1=dateFormat.parse(s1);
            mDate=dateFormat.parse(m);
        }catch(ParseException e){

        }

        if((c.get(Calendar.MONTH)+1)<7&&(c.get(Calendar.MONTH)+1)>=2){
            //确保startDate在endDate之前 
            if(startDate2.after(endDate)){
                Date cal=startDate2;
                startDate2=endDate;
                endDate=cal;
            }
            //分别得到两个时间的毫秒数 
            long sl=startDate2.getTime();
            long el=endDate.getTime();
            long ei=el-sl;
            week= "第"+String.valueOf((int)(ei/(1000*60*60*24)/7))+"周";
        }
        else if((c.get(Calendar.MONTH)+1)>=7&&(c.get(Calendar.MONTH)+1)<=12) {
            //确保startDate在endDate之前 
            if(startDate1.after(endDate)){
                Date cal=startDate1;
                startDate1=endDate;
                endDate=cal;
            }
            //分别得到两个时间的毫秒数 
            long sl=startDate1.getTime();
            long el=endDate.getTime();
            long ei=el-sl;
            week= "第"+String.valueOf((int)(ei/(1000*60*60*24)/7))+"周";
        }
        else{
            //分别得到两个时间的毫秒数 
            long sl=startDate1.getTime();
            long el=endDate.getTime();
            long ml=mDate.getTime();
            long ei=el+(ml-sl);
            week= "第"+String.valueOf((int)(ei/(1000*60*60*24)/7))+"周";
        }
        Log.e("week: ",week);
        return week;
    }
}
