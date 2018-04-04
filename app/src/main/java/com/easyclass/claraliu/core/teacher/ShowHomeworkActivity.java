package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

import edu.vero.easyclass.domain.Homework;

public class ShowHomeworkActivity extends AppCompatActivity
                                    implements View.OnClickListener{
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private List<Homework> homeworks;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_homework);
        application=(MyApplication)getApplicationContext();
        initJson();
        b1=(Button)findViewById(R.id.button);
        b2=(Button)findViewById(R.id.button3);
        b3=(Button)findViewById(R.id.button4);
        b4=(Button)findViewById(R.id.button5);
        b5=(Button)findViewById(R.id.button2);
        b6=(Button)findViewById(R.id.button6);
        b7=(Button)findViewById(R.id.button8);
        b8=(Button)findViewById(R.id.button9);
        //登录监听
        b1.setOnClickListener(ShowHomeworkActivity.this);
        b2.setOnClickListener(ShowHomeworkActivity.this);
        b3.setOnClickListener(ShowHomeworkActivity.this);
        b4.setOnClickListener(ShowHomeworkActivity.this);
        b5.setOnClickListener(ShowHomeworkActivity.this);
        b6.setOnClickListener(ShowHomeworkActivity.this);
        b7.setOnClickListener(ShowHomeworkActivity.this);
        b8.setOnClickListener(ShowHomeworkActivity.this);
    }

    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/arrangements/"+application.getArrangementId()+"/homeworks");
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

    @Override
    public void onClick(View view) {
        if(homeworks.size()==0)
            Toast.makeText(ShowHomeworkActivity.this, "还未布置作业！", Toast.LENGTH_SHORT).show();
        else {
            if (view.getId() == R.id.button) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(0).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button3) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(1).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button4) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(2).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button5) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(3).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button2) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(4).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button6) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(5).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button8) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(6).getHomeworkId());
                startActivity(it);
            }
            if (view.getId() == R.id.button9) {
                Intent it = new Intent(ShowHomeworkActivity.this, StudentHomeworkConditionActivity.class);
                it.putExtra("homeworkId", homeworks.get(7).getHomeworkId());
                startActivity(it);
            }
        }
    }

    public void back(View v){
        finish();
    }
}
