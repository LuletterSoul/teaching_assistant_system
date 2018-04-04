package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.vero.easyclass.domain.ClassSchedule;
import edu.vero.easyclass.domain.CourseComment;

public class CourseCommentActivity extends AppCompatActivity {
    private RadioGroup Q;
    private RadioGroup Q2;
    private RadioGroup Q4;
    private RadioGroup Q5;
    private MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_comment);
        Q=(RadioGroup)findViewById(R.id.radioGroup);
        Q2=(RadioGroup)findViewById(R.id.radioGroup2);
        Q4=(RadioGroup)findViewById(R.id.radioGroup4);
        Q5=(RadioGroup)findViewById(R.id.radioGroup5);
        application=(MyApplication)getApplicationContext();
    }

    public void getResult(View v){
        float score=0;
        if(Q.getCheckedRadioButtonId()== R.id.radioButton3)
            score+=25;
        else if(Q.getCheckedRadioButtonId()== R.id.radioButton4)
            score+=0;
        if(Q2.getCheckedRadioButtonId()== R.id.radioButton)
            score+=25;
        else if(Q2.getCheckedRadioButtonId()== R.id.radioButton2)
            score+=0;
        else if(Q2.getCheckedRadioButtonId()== R.id.radioButton5)
            score+=15;
        if(Q4.getCheckedRadioButtonId()== R.id.radioButton9)
            score+=25;
        else if(Q4.getCheckedRadioButtonId()== R.id.radioButton10)
            score+=0;
        else if(Q4.getCheckedRadioButtonId()== R.id.radioButton11)
            score+=15;
        if(Q5.getCheckedRadioButtonId()== R.id.radioButton12)
            score+=25;
        else if(Q5.getCheckedRadioButtonId()== R.id.radioButton13)
            score+=0;
        else if(Q5.getCheckedRadioButtonId()== R.id.radioButton14)
            score+=15;
        Toast.makeText(CourseCommentActivity.this, "总得分："+score, Toast.LENGTH_SHORT).show();
        onSuccess(score);
    }

    private void onSuccess(float score) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                String url=application.getBaseURL()+"/course_comments";
                int scheduleId=0;
                int arrangementId = 0;
                CourseComment courseComment=new CourseComment();
                courseComment.setScore(score);
                for(int i=0;i<application.getStudentClassSchedules().size();i++) {
                    //定位到学生点击的那门课
                    if (application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId() == application.getStudentOnclickCourseId()) {
                        ClassSchedule classSchedule = application.getStudentClassSchedules().get(i);
                        scheduleId = classSchedule.getScheduleId();
                        arrangementId = classSchedule.getTeacherArrangement().getArrangementId();
                        break;
                    }
                }
                //将arrangeId和scheduleId作为url参数传递给服务器
                Map<String, Object> params=new HashMap<String, Object>();
                params.put("arrangeId",arrangementId);
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

                //发起post请求
                HttpPost httpPost=new HttpPost(url);
                Gson gson = new Gson();
                String json = gson.toJson(courseComment);

                HttpResponse httpResponse=null;
                try {
                    httpPost.setEntity(new StringEntity(json));
                    httpResponse=httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(httpResponse.getStatusLine().getStatusCode()==201) {
                    //跳转到评教成功界面
                    Intent intent = new Intent(CourseCommentActivity.this, CourseCommentSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public void back(View v){
        finish();
    }
}
