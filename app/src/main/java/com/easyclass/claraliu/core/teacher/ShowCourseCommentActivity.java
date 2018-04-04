package com.easyclass.claraliu.core.teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.text.DecimalFormat;
import java.util.List;

import edu.vero.easyclass.domain.CourseComment;

public class ShowCourseCommentActivity extends AppCompatActivity {
    private TextView score;
    private MyApplication application;
    private List<CourseComment> courseComments;
    private int temp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course_comment);
        score=(TextView)findViewById(R.id.score);
        application=(MyApplication)getApplicationContext();
        initJson();
        while(true){
            if(temp==1){
                claculate();
                break;
            }
        }
    }

    private void claculate() {
        int num=courseComments.size();
        if(num==0)
            score.setText("还未评教！");
        else {
            int totalScore = 0;
            for (int i = 0; i < num; i++) {
                totalScore += courseComments.get(i).getScore();
            }
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format((float) totalScore / num);
            Log.e("CommentResult", result);
            String a="您的得分是：" + result + " 分！";
            Log.e("a", a);
            score.setText(a);
        }
    }

    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //修改
                int arrangementId=application.getArrangementId();
                Log.e("arrangementId", String.valueOf(arrangementId));
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/arrangements/"+arrangementId+"/comments");
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);

                        Gson gson=new Gson();
                        //把JSON格式的字符串转为List
                        courseComments = gson.fromJson(jsonString,new TypeToken<List<CourseComment>>(){}.getType());
                        for (CourseComment cc: courseComments){
                            System.out.println("把JSON格式的字符串转为List///" + cc.getScore());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                temp=1;
            }
        }).start();
    }
    public void back(View v){
        finish();
    }
}
