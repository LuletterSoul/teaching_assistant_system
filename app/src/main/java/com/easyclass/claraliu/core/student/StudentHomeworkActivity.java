package com.easyclass.claraliu.core.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.vero.easyclass.domain.ClassSchedule;
import edu.vero.easyclass.domain.Homework;
import edu.vero.easyclass.domain.Student;

public class StudentHomeworkActivity extends AppCompatActivity
                                    implements View.OnClickListener{
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private TextView t6;
    private TextView t7;
    private TextView t8;
    private MyApplication application;
    private List<Homework> homeworks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework);
        application=(MyApplication)this.getApplicationContext();
        initJson();
        //修改：setState()是新写的一个函数，函数体在第105行
        while(true){
            if(homeworks!=null) {
                setState();
                break;
            }
        }

        b1=(Button)findViewById(R.id.button10);
        b2=(Button)findViewById(R.id.button11);
        b3=(Button)findViewById(R.id.button12);
        b4=(Button)findViewById(R.id.button13);
        b5=(Button)findViewById(R.id.button14);
        b6=(Button)findViewById(R.id.button15);
        b7=(Button)findViewById(R.id.button16);
        b8=(Button)findViewById(R.id.button17);
        b1.setOnClickListener(StudentHomeworkActivity.this);
        b2.setOnClickListener(StudentHomeworkActivity.this);
        b3.setOnClickListener(StudentHomeworkActivity.this);
        b4.setOnClickListener(StudentHomeworkActivity.this);
        b5.setOnClickListener(StudentHomeworkActivity.this);
        b6.setOnClickListener(StudentHomeworkActivity.this);
        b7.setOnClickListener(StudentHomeworkActivity.this);
        b8.setOnClickListener(StudentHomeworkActivity.this);
        t1=(TextView)findViewById(R.id.b1);
        t2=(TextView)findViewById(R.id.b2);
        t3=(TextView)findViewById(R.id.b3);
        t4=(TextView)findViewById(R.id.b4);
        t5=(TextView)findViewById(R.id.b5);
        t6=(TextView)findViewById(R.id.b6);
        t7=(TextView)findViewById(R.id.b7);
        t8=(TextView)findViewById(R.id.b8);

        Intent it=getIntent();
        int state=it.getIntExtra("state",0);
    }

    private void setState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                int arrangementId=0;
                for(int i=0;i<application.getStudentClassSchedules().size();i++) {
                    //定位到学生点击的那门课
                    if (application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId() == application.getStudentOnclickCourseId()) {
                        ClassSchedule classSchedule = application.getStudentClassSchedules().get(i);
                        arrangementId = classSchedule.getTeacherArrangement().getArrangementId();
                    }
                }
                String url=application.getBaseURL()+"/arrangements/"+arrangementId+"/homeworks/"+"submitted_students";
                for(int i=0;i<homeworks.size();i++) {
                    try {
                        int homeworkId=homeworks.get(i).getHomeworkId();
                        //将homeworkId作为url参数传递给服务器
                        Map<String, Object> params=new HashMap<String, Object>();
                        params.put("homeworkId",homeworkId);
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
                        HttpGet httpGet=new HttpGet(url);
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            HttpEntity entity = httpResponse.getEntity();
                            String jsonString = EntityUtils.toString(entity, "utf-8");
                            //用此方法在日志中打印信息
                            Log.e("tag", jsonString);

                            Gson gson = new Gson();
                            //把JSON格式的字符串转为List
                            List<Student> students = gson.fromJson(jsonString, new TypeToken<List<Student>>() {
                            }.getType());
                            //判断当前学生是否交了当前homeworkId对应的作业
                            for (Student s : students) {
                                System.out.println("把JSON格式的字符串转为List///" + s.getStudentId());
                                if(s.getStudentId().equals(application.getStudentId())){
                                    if(i==0){
                                        t1.setText("已提交");
                                    }
                                    if(i==1){
                                        t2.setText("已提交");
                                    }
                                    if(i==2){
                                        t3.setText("已提交");
                                    }
                                    if(i==3){
                                        t4.setText("已提交");
                                    }
                                    if(i==4){
                                        t5.setText("已提交");
                                    }
                                    if(i==5){
                                        t6.setText("已提交");
                                    }
                                    if(i==6){
                                        t7.setText("已提交");
                                    }
                                    if(i==7){
                                        t8.setText("已提交");
                                    }
                                }
                            }

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                int arrangementId=0;
                for(int i=0;i<application.getStudentClassSchedules().size();i++) {
                    //定位到学生点击的那门课
                    if (application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId() == application.getStudentOnclickCourseId()) {
                        ClassSchedule classSchedule = application.getStudentClassSchedules().get(i);
                        arrangementId = classSchedule.getTeacherArrangement().getArrangementId();
                    }
                }
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/arrangements/"+arrangementId+"/homeworks");
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);

                        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
                        //把JSON格式的字符串转为List
                        homeworks = gson.fromJson(jsonString,new TypeToken<List<Homework>>(){}.getType());
                        for (Homework h: homeworks){
                            System.out.println("把JSON格式的字符串转为List///" + h.getHomeworkId());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //点击之前要判断homework布置了多少次，不然出错！！！！！！
    @Override
    public void onClick(View view) {
        //获取作业布置的次数
        int size=0;
        if(homeworks!=null) {
            size = homeworks.size();
            if (view.getId() == R.id.button10) {
                int homeworkId = homeworks.get(0).getHomeworkId();
                Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                it.putExtra("homeworkId", homeworkId);
                startActivityForResult(it, 1);
            }
            if (view.getId() == R.id.button11) {
                if(size>=2) {
                    int homeworkId = homeworks.get(1).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 2);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第二次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button12) {
                if(size>=3) {
                    int homeworkId = homeworks.get(2).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 3);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第三次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button13) {
                if(size>=4) {
                    int homeworkId = homeworks.get(3).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 4);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第四次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button14) {
                if(size>=5) {
                    int homeworkId = homeworks.get(4).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 5);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第五次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button15) {
                if(size>=6) {
                    int homeworkId = homeworks.get(5).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 6);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第六次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button16) {
                if(size>=7) {
                    int homeworkId = homeworks.get(6).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 7);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第七次作业", Toast.LENGTH_SHORT).show();
            }
            if (view.getId() == R.id.button17) {
                if(size>=8) {
                    int homeworkId = homeworks.get(7).getHomeworkId();
                    Intent it = new Intent(StudentHomeworkActivity.this, HomeworkUploadActivity.class);
                    it.putExtra("homeworkId", homeworkId);
                    startActivityForResult(it, 8);
                }
                else
                    Toast.makeText(StudentHomeworkActivity.this, "还未布置第八次作业", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(StudentHomeworkActivity.this, "作业还没有布置！", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode== Activity.RESULT_OK) {
            Toast.makeText(StudentHomeworkActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
            if (requestCode == 1) {
                t1.setText("已提交");
            }
            if (requestCode == 2) {
                t2.setText("已提交");
            }
            if (requestCode == 3) {
                t3.setText("已提交");
            }
            if (requestCode == 4) {
                t4.setText("已提交");
            }
            if (requestCode == 5) {
                t5.setText("已提交");
            }
            if (requestCode == 6) {
                t6.setText("已提交");
            }
            if (requestCode == 7) {
                t7.setText("已提交");
            }
            if (requestCode == 8) {
                t8.setText("已提交");
            }
        }
        else{
            Toast.makeText(StudentHomeworkActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View v){
        finish();
    }
}
