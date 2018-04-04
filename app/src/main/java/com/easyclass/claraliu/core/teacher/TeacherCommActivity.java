package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.easyclass.claraliu.core.student.StuVoteActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import edu.vero.easyclass.domain.Attendance;
import edu.vero.easyclass.domain.Vote;
import edu.vero.easyclass.domain.VoteOption;

/**
 * Created by Administrator on 2017/12/28.
 */

public class TeacherCommActivity extends AppCompatActivity {
    MyApplication application;
    public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_comm);
    }
    public void createAttendance(View v) throws JsonProcessingException {
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCommActivity.this, AttendanceConfirm.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void createVote(View v) throws JsonProcessingException, UnsupportedEncodingException {
         application=(MyApplication)getApplicationContext();
        Vote vote=new Vote();
        vote.setVoteContent("当前知识点是否明白");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        long currentTime=System.currentTimeMillis();
        vote.setEstablishedTime(curDate);
        currentTime +=5*60*1000;
        Date dateline=new Date(currentTime);
        vote.setDeadline(dateline);
        vote.setClosed(false);
        vote.setAttendance(application.getAttendance());
        VoteOption voteOption1=new VoteOption();
        VoteOption voteOption2=new VoteOption();
        voteOption1.setOptionCount(0);
        voteOption1.setOptionContent(new String("懂了".getBytes("UTF-8")));
        voteOption2.setOptionCount(0);
        voteOption2.setOptionContent(new String("不懂".getBytes("UTF-8")));
         Set<VoteOption> voteOptions=new HashSet<>();
         voteOptions.add(voteOption1);
         voteOptions.add(voteOption2);
         vote.setOptions(voteOptions);
        ObjectMapper mapper = new ObjectMapper();
        String voteJackson = mapper.writeValueAsString(vote);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClientPost=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(application.getBaseURL()+"/attendances/"+application.getAttendanceId()+"/votes");
                HttpResponse httpResponse=null;
                httpPost.setEntity(new StringEntity(voteJackson,"UTF-8"));
                try {
                      httpResponse=httpClientPost.execute(httpPost);
                       if(httpResponse.getStatusLine().getStatusCode()==201) {
                           Looper.prepare();
                           Toast.makeText(TeacherCommActivity.this, "创建成功！", Toast.LENGTH_SHORT).show();
                           Looper.loop();
                       }
                    } catch (ClientProtocolException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }).start();

    }
    public void checkVote(View v){
        Intent intent1 =getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCommActivity.this, CheckVoteActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void createTest(View v){
        Intent intent1=getIntent();
        int tag = intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(TeacherCommActivity.this, CreateTestActivity.class);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }
    public void back(View v){
        finish();
    }
}
