package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import edu.vero.easyclass.domain.ClassSchedule;

public class StudentMainActivity extends AppCompatActivity
                                implements View.OnClickListener {
    private ImageButton byyl;
    private TextView txByyl;
    private MyApplication application;
    private List<ClassSchedule> classSchedule;
    final static int WEIGHT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        //修改：设置学生姓名
        Intent it=getIntent();
        TextView txv=(TextView)findViewById(R.id.textView3);
        txv.setText(it.getStringExtra("studentName"));

        application= (MyApplication) this.getApplicationContext();
        initJson();
        while (true) {
            if(classSchedule!=null) {
                LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main);
                // 动态添加圆形图片
                LinearLayout rowLayout = null;
                for (int i = 0; i < classSchedule.size(); i++) {
                    if (i % WEIGHT == 0) {
                        // LinearLayout不能换行,增加布局完成。
                        rowLayout = createImageLayout();
                        mainLayout.addView(rowLayout);
                    }
                    final View columnLinearLayout = createImage(WEIGHT,classSchedule.get(i),i);
                    rowLayout.addView(columnLinearLayout);
                }
                break;
            }
        }
    }

    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                //Log.e("UserId",application.getUserId());
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/students/"+application.getUserId()+"/schedules");
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);

                        Gson gson=new Gson();
                        //获取课表信息
                        classSchedule = gson.fromJson(jsonString,new TypeToken<List<ClassSchedule>>(){}.getType());
                        for (ClassSchedule cs:classSchedule){
                            System.out.println("把JSON格式的字符串转为List///" + cs.getTeacherArrangement().getCourse().getCourseName());
                        }
                        //将请求得到的classSchedule信息存为全局变量
                        application.setStudentClassSchedules(classSchedule);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 每4个图片一行
    public LinearLayout createImageLayout() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        return linearLayout;
    }

    public View createImage(int weight,ClassSchedule classSchedule,int tag) {
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
        //新创建一个按钮
        Button btn = new Button(this);
        btn.setLayoutParams(new LinearLayout.LayoutParams(cricleRadius, cricleRadius));
        btn.setBackgroundResource(R.drawable.a1104241);
        //为按钮设置一个标记，来确认是按下了哪一个按钮
        btn.setTag(tag);
        //设置按钮监听
        btn.setOnClickListener(StudentMainActivity.this);
        TextView textView = new TextView(this);
        //在这里修改课程名字
        textView.setText(classSchedule.getTeacherArrangement().getCourse().getCourseName());
        textView.setGravity(Gravity.CENTER);
        //设置文字监听
        //textView.setOnClickListener(StudentMainActivity.this);
        linearLayout.setTag(textView.getText());
        linearLayout.addView(btn);
        linearLayout.addView(textView);
        return linearLayout;
    }

    @Override
    public void onClick(View view) {
        int i= (int) view.getTag();
        Log.e("ID:", String.valueOf(i));
        Intent it=new Intent(StudentMainActivity.this,StudentCourseActivity.class);
        it.putExtra("tag", i);
        startActivity(it);
    }
    public void back(View v){
        finish();
    }
}