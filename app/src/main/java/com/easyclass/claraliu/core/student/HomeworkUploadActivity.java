package com.easyclass.claraliu.core.student;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.vero.easyclass.domain.ClassSchedule;

public class HomeworkUploadActivity extends AppCompatActivity {
    private Button button;
    private MyApplication application;
    private int temp=0;
    private int homeworkId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_upload);
        Intent it=getIntent();
        homeworkId=it.getIntExtra("homeworkId",0);
        application=(MyApplication) this.getApplicationContext();
        button=(Button)findViewById(R.id.upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/plain");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode==Activity.RESULT_OK){
            if(requestCode==1){
                Uri uri=data.getData();
                Toast.makeText(this,"文件路径："+uri.getPath().toString(),Toast.LENGTH_SHORT).show();
                String url=application.getBaseURL()+"/homeworks/"+homeworkId;

                //修改
                int scheduleId=0;
                for(int i=0;i<application.getStudentClassSchedules().size();i++) {
                    //定位到学生点击的那门课
                    if (application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId() == application.getStudentOnclickCourseId()) {
                        ClassSchedule classSchedule = application.getStudentClassSchedules().get(i);
                        scheduleId = classSchedule.getScheduleId();
                    }
                }
                //将scheduleId作为url参数传递给服务器
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

                File file= new File(uri.getPath());
                String fileAbsolutePath=file.getAbsolutePath();
                UploadThread thread=new UploadThread(fileAbsolutePath,url);
                thread.start();
                temp=1;
            }
        }
    }

    public void submit(View v){
        if(temp==1){
            //如果上传了作业，就跳转回原先界面
            setResult(RESULT_OK);
            finish();
        }
        else{
            setResult(RESULT_CANCELED);
            finish();
        }
    }
    public void back(View v){
        finish();
    }
}
