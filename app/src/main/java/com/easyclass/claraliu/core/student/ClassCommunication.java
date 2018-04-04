package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import edu.vero.easyclass.domain.ClassTime;
import edu.vero.easyclass.domain.ClassTimeComment;

public class ClassCommunication extends AppCompatActivity {
    private RadioGroup state;
    private List<ClassTime> classTimes;
    private MyApplication application;
    private int timeId=-2;
    private int scheduleId;
    private CalculateTime calculateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_communication);
        application=(MyApplication)getApplicationContext();
        calculateTime=new CalculateTime();
        Log.e("weekday",calculateTime.getWeekday());
        Log.e("term",calculateTime.getTerm());
        Log.e("week",calculateTime.getWeek());

        getTimes();
    }

   private void getTimes() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                for(int i=0;i<application.getStudentClassSchedules().size();i++){
                    //定位到学生点击的那门课
                    if(application.getStudentClassSchedules().get(i).getTeacherArrangement().getCourse().getCourseId()==application.getStudentOnclickCourseId()){
                        ClassSchedule classSchedule=application.getStudentClassSchedules().get(i);
                        scheduleId=classSchedule.getScheduleId();
                        int arrangementId=classSchedule.getTeacherArrangement().getArrangementId();
                        //获取该门课的所有上课时间
                        HttpGet httpGet=new HttpGet(application.getBaseURL()+"/arrangements/"+arrangementId+"/times");
                        try {
                            HttpResponse httpResponse=httpClient.execute(httpGet);
                            if(httpResponse.getStatusLine().getStatusCode()==200){
                                HttpEntity entity=httpResponse.getEntity();
                                String jsonString= EntityUtils.toString(entity,"utf-8");
                                //用此方法在日志中打印信息
                                Log.e("tag",jsonString);

                                Gson gson=new Gson();
                                //获取时间信息
                                classTimes = gson.fromJson(jsonString,new TypeToken<List<ClassTime>>(){}.getType());
                                for (ClassTime ct:classTimes){
                                    System.out.println("把JSON格式的字符串转为List///" + ct.getWeekday());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                CalculateTime calculateTime=new CalculateTime();
                String term=calculateTime.getTerm();
                String week=calculateTime.getWeek();
                String weekday=calculateTime.getWeekday();
                for(int i=0;i<classTimes.size();i++){
                    if(classTimes.get(i).getWeekday().equals(weekday)
                            &&classTimes.get(i).getWeek().equals(week)
                            &&classTimes.get(i).getTerm().equals(term))
                        //获取到此时此刻这堂课的timeId
                       timeId=classTimes.get(i).getTimeId();
                }
            }
        }).start();
    }

    //onClick事件对应的方法必须是public
    public void getResult(View v){
        state=(RadioGroup)findViewById(R.id.radioGroup);
        float result;
        if(state.getCheckedRadioButtonId()== R.id.satisfy)
            result=100;
        else
            result=0;
        Log.e("tag", String.valueOf(result));
        if(calculateTime.getWeekday().equals("周末")) {
            Toast.makeText(ClassCommunication.this, "周末不开放！", Toast.LENGTH_SHORT).show();
        }
        else if(timeId==-2){
            Toast.makeText(ClassCommunication.this, "评教失败！", Toast.LENGTH_SHORT).show();
        }
        else {
            onSuccess(result);
        }
    }

    private void onSuccess(float result){
        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url=application.getBaseURL()+"/class_comments";
                ClassTimeComment classComment=new ClassTimeComment();
                classComment.setScore(result);
                //将timeId和scheduleId作为url参数传递给服务器
                Map<String, Object> params=new HashMap<String, Object>();
                params.put("timeId",timeId);
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
                HttpClient httpClient=new DefaultHttpClient();
                //发起post请求
                HttpPost httpPost=new HttpPost(url);
                Gson gson = new Gson();
                String json = gson.toJson(classComment);

                HttpResponse httpResponse=null;
                try {
                    httpPost.setEntity(new StringEntity(json));
                    httpResponse=httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(httpResponse.getStatusLine().getStatusCode()==200) {
                   //跳转
                    Intent intent = new Intent(ClassCommunication.this,ClassCommentSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }

    public void back(View v){
        finish();
    }
    public void test(View v){
        Intent intent1 = getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(ClassCommunication.this,StuClassTestMain.class);
        intent.putExtra("tag",i);
        startActivity(intent);
    }
    public void vote(View v){
        Intent intent1 = getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(ClassCommunication.this,StuVoteActivity.class);
        intent.putExtra("tag",i);
        startActivity(intent);
    }
    public void attendance(View v){
        Intent intent1 = getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent intent = new Intent(ClassCommunication.this,AttendanceActivity.class);
        intent.putExtra("tag",i);
        startActivity(intent);
    }
}
