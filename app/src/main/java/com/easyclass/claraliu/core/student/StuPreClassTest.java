package com.easyclass.claraliu.core.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import edu.vero.easyclass.domain.Notice;
import edu.vero.easyclass.domain.OnlineClassTest;
import edu.vero.easyclass.domain.TestRecord;

/**
 * Created by Administrator on 2017/12/24.
 */

public class StuPreClassTest extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private MyApplication application;
    private int tag;
    private ObjectMapper objectMapper;
    private List<TestRecord> testList;
    private String test="\n";
    private int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_previous_test);
        application= (MyApplication) this.getApplicationContext();
        Intent intent=this.getIntent();
        tag=intent.getIntExtra("tag",-1);
        testList=new ArrayList<TestRecord>();
        init();
        //把输入的行和列转为整形
   /*    int row_int = testList.size();
        int col_int = 1;
        //获取控件tableLayout
        TableLayout tableLayout = (TableLayout) findViewById(R.id.table1);
        //清除表格所有行
        tableLayout.removeAllViews();
        //全部列自动填充空白处
        tableLayout.setStretchAllColumns(true);
        //生成X行，Y列的表格
        for (int i = 1; i <= row_int; i++) {
            TableRow tableRow = new TableRow(StuPreClassTest.this);
            for (int j = 1; j <= col_int; j++) {
                //tv用于显示
                TextView tv = new TextView(StuPreClassTest.this);
                //Button bt=new Button(MainActivity.this);
                tv.setText(testList.get(i-1).getTest().getEstablishedTime().toString() + "          "
                                + String.valueOf(testList.get(i-1).getScore()));
                tableRow.addView(tv);
            }
            //新建的TableRow添加到TableLayout
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));
        }*/
   while(true){
       if(temp==1){
           TextView textView26=(TextView)findViewById(R.id.textView26);
           textView26.setText(test);
           break;
       }
   }
    }
    public void init(){
       new Thread(new Runnable() {
           @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(application.getBaseURL() + "/schedules/" +
                      application.getStudentClassSchedules().get(tag).getScheduleId()
                        + "/test_records");

               try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                objectMapper=new ObjectMapper();
                SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                objectMapper.setDateFormat(myDateFormat);
                testList = objectMapper.readValue(response, new TypeReference<List<TestRecord>>() {});
                Log.e("tag",String.valueOf(testList.size()));
                for(int i=0;i<testList.size();i++){
                    test+="第"+(i+1)+"次测试"+"     "+testList.get(i).getScore()+"\n\n";
                }
                temp=1;
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}).start();

    }
    public void back(View v){
        finish();
    }
}
