package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import edu.vero.easyclass.domain.Notice;

/**
 * Created by Administrator on 2017/12/29.
 */

public class TeacherCourseActivity extends AppCompatActivity {
    ImageButton imageButton1,imageButton2,imageButton3,imageButton4;
    Button button;
    TextView announceTextView;
    MyApplication application;
    int arrangementId;
    ObjectMapper objectMapper;
    TextView classTextView;
    /**
     * class
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_main);
        Intent intent =getIntent();
        int tag = intent.getIntExtra("tag",-1);
        objectMapper=new ObjectMapper();
        application=(MyApplication) getApplicationContext();
        String className=application.getTeacherArrangements().get(tag).getCourse().getCourseName();
        classTextView=(TextView)findViewById(R.id.textView3);
        classTextView.setText((className));

        arrangementId=application.getTeacherArrangements().get(tag).getArrangementId();
       // arrangementId=application.getTeacherArrangements().get(tag).getArrangementId();
        application.setArrangementId(arrangementId);
        //显示当前最新公告
        announceTextView=(TextView)findViewById(R.id.textView24);
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpClientAnnounce = new DefaultHttpClient();
        /*        HttpGet httpGetAnnounce = new HttpGet(application.getBaseURL() + "/arrangements/" +
                        application.getTeacherArrangements().get(tag).getArrangementId()
                        + "/notices/newest");*/
                HttpGet httpGetAnnounce = new HttpGet(application.getBaseURL() + "/arrangements/" +
                        arrangementId + "/notices/newest");
                try

                {
                    HttpResponse httpResponseAnnounce = httpClientAnnounce.execute(httpGetAnnounce);
                    if (httpResponseAnnounce.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entityAnnounce = httpResponseAnnounce.getEntity();
                        String responseAnnounce = EntityUtils.toString(entityAnnounce, "utf-8");
                        Notice newestAnnounce;
                        SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                        newestAnnounce = objectMapper.readValue(responseAnnounce, Notice.class);
                        DateFormat df2 = DateFormat.getDateTimeInstance();
                        String announce = newestAnnounce.getContent() + "\n" + df2.format(newestAnnounce.getEstablishedTime());
                        Log.e("announce",announce);
                        announceTextView.setText(announce);
                    }
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void OnclickClassComm(View v){
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCourseActivity.this, TeacherCommActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void OnclickClassEva(View v){
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCourseActivity.this,ShowCourseCommentActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void OnclickClassFile(View v){
        Intent intent = new Intent(TeacherCourseActivity.this,LoginActivity.class);
        startActivity(intent);
    }
    public void OnclickClassWork(View v){
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCourseActivity.this,ShowHomeworkActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void OnclickCreateAnnounce(View v){
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCourseActivity.this, AddNoticeActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void back(View v){
        finish();
    }
}
