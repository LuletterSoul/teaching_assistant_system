package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import edu.vero.easyclass.domain.Notice;
import edu.vero.easyclass.domain.Question;

/**
 * Created by Psj on 2017/12/23.
 */

public class StuAnnounceActivity extends AppCompatActivity {
    private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private List<Notice> noticeList;
    private int temp=0;
    private ObjectMapper objectMapper;
    private MyApplication application;
    private int tag;
    private String noticeString="\n";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         objectMapper = new ObjectMapper();
         application = (MyApplication) getApplicationContext();
        Intent intent = getIntent();
         tag = intent.getIntExtra("tag",-1);
       // noticeList = new ArrayList<Notice>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_announce);
        new Thread(new Runnable() {
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                System.out.println("tag:"+tag);
                HttpGet httpGet = new HttpGet(application.getBaseURL() + "/arrangements/" +
                        application.getStudentClassSchedules().get(tag).getTeacherArrangement().getArrangementId()
                        + "/notices");
                try
                {
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                        noticeList = objectMapper.readValue(response, new TypeReference<List<Notice>>() {
                        });
                         for(int i=0;i<noticeList.size();i++){
                             DateFormat df2 = DateFormat.getDateTimeInstance();
                             noticeString+="第"+(i+1)+"次公告"+"    "+noticeList.get(i).getContent()+"\n"
                             +"       "+df2.format(noticeList.get(i).getEstablishedTime())+"\n\n";
                         }
                        temp=1;
                    }
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        }).start();

                //把输入的行和列转为整形
   /*     while(true) {
            if(temp==1) {
                int row_int = 3;
                int col_int = 1;
                //获取控件tableLayout
                TableLayout tableLayout = (TableLayout) findViewById(R.id.table1);
                //清除表格所有行
                tableLayout.removeAllViews();
                //全部列自动填充空白处
                tableLayout.setStretchAllColumns(true);
                //生成X行，Y列的表格
                for (int i = 1; i <= noticeList.size(); i++) {
                    TableRow tableRow = new TableRow(StuAnnounceActivity.this);
                    for (int j = 1; j <= 1; j++) {
                        //tv用于显示
                        TextView tv = new TextView(StuAnnounceActivity.this);
                        //Button bt=new Button(MainActivity.this);
                        tv.setText(noticeList.get(i).getContent());
                        tableRow.addView(tv);
                    }
                    //新建的TableRow添加到TableLayout
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(MP, WC, 1));
                }
            }
            break;
        }*/
   while(true){
       if(temp==1){
           TextView textView25=(TextView)findViewById(R.id.textView25);
           textView25.setText(noticeString);
           break;
       }
   }
    }
    public void back(View v){
finish();
    }
}
