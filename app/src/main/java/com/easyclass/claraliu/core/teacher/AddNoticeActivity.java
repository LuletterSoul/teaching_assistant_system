package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.easyclass.claraliu.core.student.StuVoteActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import edu.vero.easyclass.domain.Notice;

/**
 * Created by Administrator on 2017/12/29.
 */

public class AddNoticeActivity extends AppCompatActivity {
    MyApplication application;
    Intent intent ;
    int tag ;
    protected void onCreate(Bundle savedInstanceState){
        application=(MyApplication)getApplicationContext();
        intent =getIntent();
        tag = intent.getIntExtra("tag",-1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnotice);

    }
    public void OnlickPost(View v) throws JsonProcessingException, UnsupportedEncodingException {
        EditText textView=(EditText) findViewById(R.id.textView);
        String notice1=textView.getText().toString();
        Notice notice=new Notice();
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        long currentTime=System.currentTimeMillis();
        notice.setEstablishedTime(curDate);
        notice.setContent(notice1);
        notice.setArrangement(application.getTeacherArrangements().get(tag));
        ObjectMapper mapper = new ObjectMapper();
        String noticeJackson = mapper.writeValueAsString(notice);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClientPost=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(application.getBaseURL()+"/arrangements/"+application
                        .getTeacherArrangements().get(tag).getArrangementId()+"/notices");
                HttpResponse httpResponse=null;
                try {
                    httpPost.setEntity(new StringEntity(noticeJackson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    httpResponse=httpClientPost.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==201) {
                        //跳转
                        Looper.prepare();
                        Toast.makeText(AddNoticeActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public void back(View v){
        finish();
    }
}
