package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

import edu.vero.easyclass.domain.Notice;
import edu.vero.easyclass.domain.OnlineClassTest;
import edu.vero.easyclass.domain.QuestionOption;

public class StudentCourseActivity extends AppCompatActivity {
    private MyApplication application;
    private TextView txv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course);
        Intent it=getIntent();
        int i=it.getIntExtra("tag",-1);
        Log.e("ID",String.valueOf(i));
        application=(MyApplication)getApplicationContext();
        txv=(TextView)findViewById(R.id.textView2);
       // int arrangementId=application.getStudentClassSchedules().get(i).getTeacherArrangement().getArrangementId();
        int arrangementId=application.getStudentClassSchedules().get(i).getTeacherArrangement().getArrangementId();
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(application.getBaseURL() + "/arrangements/" + arrangementId + "/notices/newest");
                try

                {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                         ObjectMapper objectMapper=new ObjectMapper();
                        SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                      //   List<Notice> notice=objectMapper.readValue(response, new TypeReference<List<Notice>>() {
                      //   });
                        Notice notice=objectMapper.readValue(response,Notice.class);
                        DateFormat df2 = DateFormat.getDateTimeInstance();

                         String newestNotice=notice.getContent()+"       "+df2.format(notice.getEstablishedTime());
                         TextView textView24=(TextView)findViewById(R.id.textView24);
                          textView24.setText(newestNotice);
                    }
                } catch (IOException e)

                {
                    e.printStackTrace();
                }
            }
        }).start();

        String courseName=application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseName();
        txv.setText(courseName);
        //将学生点击进入的这门课的courseId存入全局变量中
        int courseId=application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId();
        Log.e("courseId", String.valueOf(courseId));
        application.setStudentOnclickCourseId(courseId);
    }

    public void classCommunication(View v){
        Intent intent1 = this.getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent it=new Intent(StudentCourseActivity.this,ClassCommunication.class);
        it.putExtra("tag",i);
        startActivity(it);
    }

    public void courseComment(View v){
        Intent intent1 = this.getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent it=new Intent(StudentCourseActivity.this,CourseCommentActivity.class);
        it.putExtra("tag",i);
        startActivity(it);
    }

    public void homework(View v){
        Intent it=new Intent(StudentCourseActivity.this,StudentHomeworkActivity.class);
        startActivity(it);
    }

    public void back(View v){
        finish();
    }
    public void moreNotice(View v){
        Intent intent1 = this.getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent it=new Intent(StudentCourseActivity.this,StuAnnounceActivity.class);
        it.putExtra("tag",i);
        startActivity(it);
    }

}
