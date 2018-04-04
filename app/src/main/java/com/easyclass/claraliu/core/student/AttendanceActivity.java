package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.vero.easyclass.domain.Attendance;
import edu.vero.easyclass.domain.ClassSchedule;
import edu.vero.easyclass.domain.QuestionOption;
import edu.vero.easyclass.domain.SignRecord;
import edu.vero.easyclass.domain.Vote;

/**
 * Created by Administrator on 2018/1/2.
 */

public class AttendanceActivity extends AppCompatActivity{
    private MyApplication application;
    private Attendance attendance;
    private ObjectMapper objectMapper;
    private List<Attendance> attendancesList;
    private int tag;
    private Bitmap bitmap;
    private int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        application=(MyApplication)getApplicationContext();
        objectMapper=new ObjectMapper();
        attendance=new Attendance();
        attendancesList=new ArrayList<Attendance>();
        Intent intent=this.getIntent();
         tag=intent.getIntExtra("tag",-1);
        Log.e("tag",String.valueOf(tag));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/arrangements/"
                        +application.getStudentClassSchedules().get(tag).getTeacherArrangement()
                        .getArrangementId()+"/attendances");
                Log.e("Id:",String.valueOf(application.getStudentClassSchedules().get(tag).getTeacherArrangement()
                        .getArrangementId()));
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
//                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);
                        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                         attendancesList=objectMapper.readValue(jsonString,new TypeReference<List<Attendance>>(){});
                         Attendance attendance1=new Attendance();
                         attendance1=attendancesList.get(0);
                         for(int i=1;i<attendancesList.size();i++)
                         {
                             if(attendancesList.get(i).getEstablishedTime().after(attendance1.getEstablishedTime())
                                     ==true)
                             {
                                 attendance1=attendancesList.get(i);
                             }
                         }
                         attendance=attendance1;
                         application.setAttendanceId(attendance.getAttendanceId());
                         Log.e("tag",String.valueOf(attendance.getAttendanceId()));
//                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                HttpClient client=new DefaultHttpClient();
                String url=application.getBaseURL()+"/attendances/"+
                        attendance.getAttendanceId()+"/QR_code";
                int scheduleId=application.getStudentClassSchedules().get(tag).getScheduleId();
                Map<String, Object> params=new HashMap<String, Object>();
                params.put("scheduleId",scheduleId);
                if (params != null && !params.isEmpty()) {
                    List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                    for (String key : params.keySet()) {
                        pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                    }
                    try {
                        url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs,"utf-8"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*HttpGet httpGet1=new HttpGet(application.getBaseURL()+"/attendances/"+
                        attendance.getAttendanceId()+"/QR_code");*/
                HttpGet httpGet1=new HttpGet(url);
                InputStream is = null;
                try {
                    HttpResponse httpResponse=client.execute(httpGet1);
                    HttpEntity entity=httpResponse.getEntity();
                 //   String jsonString= EntityUtils.toString(entity,"utf-8");
                 //   Log.e("tag",jsonString);
                //    BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
                    is = entity.getContent();
                 //   Log.e("tag",is.toString());
                    if (is != null) {
                         bitmap = BitmapFactory.decodeStream(is);//转为bitmap
                        Log.e("tag",bitmap.toString());
                        temp=1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

       while(true){
            if(temp==1){
                ImageView imageView=(ImageView)findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                break;
            }
        }
    }
    public void post(View v){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignRecord signRecord=new SignRecord();
                signRecord.setAttendance(attendance);
                signRecord.setSchedule(application.getStudentClassSchedules().
                get(tag));
                SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                objectMapper.setDateFormat(myDateFormat);
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                signRecord.setSignTime(curDate);
                HttpResponse httpResponse=null;
                try {
                    String url=application.getBaseURL()+"/attendances/"+
                            attendance.getAttendanceId()+"/sign_records";
                    String jsonString = objectMapper.writeValueAsString(signRecord);
                    int scheduleId=application.getStudentClassSchedules().get(tag).getScheduleId();
                    int attendanceId=attendance.getAttendanceId();
                    Map<String, Object> params=new HashMap<String, Object>();
                    params.put("scheduleId",scheduleId);
                    params.put("attendanceId",attendanceId);
                    if (params != null && !params.isEmpty()) {
                        List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                        for (String key : params.keySet()) {
                            pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
                        }
                        try {
                            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs,"utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("url",url);
                    HttpClient httpClient=new DefaultHttpClient();
                    //发起post请求
                    HttpPost httpPost=new HttpPost(url);

                    try {
                        httpPost.setEntity(new StringEntity(jsonString));
                        try {
                            httpResponse=httpClient.execute(httpPost);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                Log.e("code",String.valueOf(httpResponse.getStatusLine().getStatusCode()));
                if(httpResponse.getStatusLine().getStatusCode()==201) {
                    //跳转
                    Looper.prepare();
                    Toast.makeText(AttendanceActivity.this, "签到成功！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if(httpResponse.getStatusLine().getStatusCode()==409){
                    Looper.prepare();
                    Toast.makeText(AttendanceActivity.this, "您已完成签到，请勿重复签到！", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        }).start();
    }
}
