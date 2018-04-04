package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.easyclass.claraliu.core.student.StudentCourseActivity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.vero.easyclass.domain.ClassSchedule;
import edu.vero.easyclass.domain.Question;
import edu.vero.easyclass.domain.Teacher;
import edu.vero.easyclass.domain.TeacherArrangement;
import edu.vero.easyclass.domain.User;

/**
 * Created by Administrator on 2017/12/26.
 */

public class TeacherMain extends AppCompatActivity  implements View.OnClickListener {
    // 每行图片的个数
    final static int WEIGHT = 3;
    private MyApplication application;
    private List<TeacherArrangement> teacherArrangements;
    private String teacherName;
    private Teacher teacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_teacher);
        teacher=new Teacher();
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main);
        // 动态添加圆形图片
        LinearLayout rowLayout = null;
        application=(MyApplication)getApplicationContext();

       new Thread(new Runnable() {
           @Override
           public void run() {
               HttpClient httpClient=new DefaultHttpClient();

               HttpGet httpGet=new HttpGet(application.getBaseURL()+"/teachers/"+application.getUserId());

               try {
                   HttpResponse httpResponse=httpClient.execute(httpGet);
                   if(httpResponse.getStatusLine().getStatusCode()==200){
                       HttpEntity entity=httpResponse.getEntity();
                       String jsonString= EntityUtils.toString(entity,"utf-8");
                       //用此方法在日志中打印信息
                       //System.out.println(jsonString);
                       ObjectMapper objectMapper=new ObjectMapper();
                       teacher=objectMapper.readValue(jsonString,Teacher.class);
               //        System.out.println(teacherArrangements.size());
                      teacherName=teacher.getTeacherName();
                  Log.e("teacherName",teacherName);

                       TextView textView3 = (TextView) findViewById(R.id.textView3);
                       textView3.setText(teacherName);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();



       // teacherArrangements=new ArrayList<TeacherArrangement>();
        initJson();
    //    System.out.println("num"+teacherArrangements.size());
        while (true) {
            if(teacherArrangements!=null) {
                for (int i = 0; i < teacherArrangements.size(); i++) {
                    if (i % WEIGHT == 0) {
                        // LinearLayout不能换行,增加布局完成。
                        rowLayout = createImageLayout();
                        mainLayout.addView(rowLayout);
                    }
                    final View columnLinearLayout = createImage(WEIGHT, teacherArrangements.get(i), i);
                    rowLayout.addView(columnLinearLayout);
                }
                break;
            }
        }
    }


    // 每4个图片一行
    public LinearLayout createImageLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }


    public View createImage(int weight,TeacherArrangement teacherArrangement,int tag) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        // 获取屏幕宽度
        int W = this.getWindowManager().getDefaultDisplay().getWidth();
        // 根据每行个数设置布局大小
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(W / weight, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setPadding(5, 5, 5, 5);
        linearLayout.setGravity(Gravity.CENTER);
        // 设置图片大小
        int cricleRadius = W / (weight +1);
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(cricleRadius, cricleRadius));
        btn.setBackgroundResource(R.drawable.a1104242);
        btn.setTag(tag);
        btn.setOnClickListener(TeacherMain.this);
        TextView textView = new TextView(this);
        textView.setText(teacherArrangement.getCourse().getCourseName());
        textView.setGravity(Gravity.CENTER);
        linearLayout.setTag(textView.getText());
        linearLayout.addView(btn);
        linearLayout.addView(textView);
        return linearLayout;
    }
    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();

                //指定访问的服务器地址
                //Log.e("UserId",application.getUserId());
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/teachers/"+application.getUserId()
                        +"/arrangements");

                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        //System.out.println(jsonString);
                        ObjectMapper objectMapper=new ObjectMapper();
                        teacherArrangements = objectMapper.readValue(jsonString,
                         new TypeReference<List<TeacherArrangement>>(){});
System.out.println(teacherArrangements.size());


                        //获取课表信息
                        //将请求得到的classSchedule信息存为全局变量
                        application.setTeacherArrangements(teacherArrangements);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void onClick(View view) {
        int i= (int) view.getTag();
        Log.e("ID:", String.valueOf(i));
        Intent it=new Intent(TeacherMain.this, TeacherCourseActivity.class);
        it.putExtra("tag", i);
        startActivity(it);
    }
    public void back(View v){
        finish();
    }
}


