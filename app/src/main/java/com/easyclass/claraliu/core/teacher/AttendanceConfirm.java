package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.vero.easyclass.domain.Attendance;

/**
 * Created by Administrator on 2018/1/2.
 */

public class AttendanceConfirm extends AppCompatActivity{
    private MyApplication application;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendanceconfirm);
    }
    public void confirm(View v) throws JsonProcessingException {
        application=(MyApplication)getApplicationContext();
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Attendance attendance=new Attendance();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        long currentTime=System.currentTimeMillis();
        attendance.setEstablishedTime(curDate);
        currentTime +=5*60*1000;
        Date deadline=new Date(currentTime);
        attendance.setDeadline(deadline);
        attendance.setArrangement(application.getTeacherArrangements().get(tag));
        ObjectMapper mapper = new ObjectMapper();
        String attendanceJackson = mapper.writeValueAsString(attendance);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClientPost=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(application.getBaseURL()+"/arrangements/"+
                        application.getTeacherArrangements().get(tag).getArrangementId()+"/attendances");
                HttpResponse httpResponse=null;
                try {
                    httpPost.setEntity(new StringEntity(attendanceJackson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    httpResponse=httpClientPost.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==201) {
                        String jsonString = EntityUtils.toString(httpResponse.getEntity());
                        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        mapper.setDateFormat(myDateFormat);
                        Attendance createdAttendance = mapper.readValue(jsonString, Attendance.class);
                        application.setAttendanceId(createdAttendance.getAttendanceId());
                        Looper.prepare();
                        Toast.makeText(AttendanceConfirm.this, "创建成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    Intent intent = new Intent(AttendanceConfirm.this, TeacherCommActivity.class);
                    startActivity(intent);
                } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }).start();
    }
    public void back(){finish();}
}
