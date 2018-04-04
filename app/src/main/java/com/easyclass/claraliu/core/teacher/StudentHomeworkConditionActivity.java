package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.google.gson.Gson;
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

import edu.vero.easyclass.domain.Student;

public class StudentHomeworkConditionActivity extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private TableLayout tableLayout;
    private int homeworkId;
    private MyApplication application;
    private List<Student> students;
    private List<Student> unSubmitStudents;
    private int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework_condition);
        application=(MyApplication)getApplicationContext();
        //获取教师点击的作业编号
        Intent it=getIntent();
        homeworkId=it.getIntExtra("homeworkId",-1);
        initJson();
        while (true) {
            if(temp==1) {
                //动态显示表格
                createTable();
                //createTable2();
                break;
            }
        }
    }

    private void createTable2() {
        if(unSubmitStudents!=null) {
            //获取控件tableLayout
            tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            //全部列自动填充空白处
            tableLayout.setStretchAllColumns(true);
            //生成X行，Y列的表格
            for (int i = 1; i <= unSubmitStudents.size(); i++) {
                TableRow tableRow = new TableRow(StudentHomeworkConditionActivity.this);
                for (int j = 1; j <= 3; j++) {
                    //tv用于显示
                    TextView tv = new TextView(StudentHomeworkConditionActivity.this);
                    if (j == 1)
                        tv.setText(unSubmitStudents.get(i).getStudentId());
                    if (j == 2)
                        tv.setText(unSubmitStudents.get(i).getStudentName());
                    if (j == 3)
                        tv.setText("已交");
                    //设置居中
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv);
                }
                //新建的TableRow添加到TableLayout
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));
            }
        }
    }

    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                int arrangementId=application.getArrangementId();
                String url=application.getBaseURL()+"/arrangements/"+arrangementId+"/homeworks/"+"submitted_students";
                //String url2=application.getBaseURL()+"/arrangements/"+arrangementId+"/homeworks/"+"unsubmitted_students";
                try {
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
                            //url2 += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs,"utf-8"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    HttpGet httpGet=new HttpGet(url);
                    //HttpGet httpGet2=new HttpGet(url2);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    //HttpResponse httpResponse2 = httpClient.execute(httpGet2);
                    //获取提交该次作业的学生名单
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String jsonString = EntityUtils.toString(entity, "utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag11", jsonString);

                        Gson gson = new Gson();
                        //把JSON格式的字符串转为List
                        students = gson.fromJson(jsonString, new TypeToken<List<Student>>(){}.getType());
                        for (Student s : students) {
                            System.out.println("把JSON格式的字符串转为List///" + s.getStudentId());
                        }
                    }
                    //获取未提交该次作业的学生名单
                    /*if (httpResponse2.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse2.getEntity();
                        String jsonString = EntityUtils.toString(entity, "utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag", jsonString);

                        Gson gson = new Gson();
                        //把JSON格式的字符串转为List
                        unSubmitStudents = gson.fromJson(jsonString, new TypeToken<List<Student>>(){}.getType());
                        for (Student s : unSubmitStudents) {
                            System.out.println("把JSON格式的字符串转为List///" + s.getStudentId());
                        }
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp=1;
            }
        }).start();
    }

    private void createTable() {
        //设置有多少人交作业
        TextView txv=(TextView)findViewById(R.id.textView22);
        if(students!=null) {
            String a= String.valueOf(students.size());
            txv.setText(a);
            //获取控件tableLayout
            tableLayout = (TableLayout) findViewById(R.id.tableLayout);
            //全部列自动填充空白处
            tableLayout.setStretchAllColumns(true);
            //生成X行，Y列的表格
            for (int i = 1; i <= students.size(); i++) {
                TableRow tableRow = new TableRow(StudentHomeworkConditionActivity.this);
                for (int j = 1; j <= 3; j++) {
                    //tv用于显示
                    TextView tv = new TextView(StudentHomeworkConditionActivity.this);
                    if (j == 1)
                        tv.setText(students.get(i-1).getStudentId());
                    if (j == 2)
                        tv.setText(students.get(i-1).getStudentName());
                    if (j == 3)
                        tv.setText("已交");
                    //设置居中
                    tv.setGravity(Gravity.CENTER);
                    tableRow.addView(tv);
                }
                //新建的TableRow添加到TableLayout
                tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));
            }
        }
        else
            txv.setText("0");
    }

    public void back(View v){
        finish();
    }
}
