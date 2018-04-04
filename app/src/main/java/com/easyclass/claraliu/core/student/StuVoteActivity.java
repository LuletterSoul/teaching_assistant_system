package com.easyclass.claraliu.core.student;

/**
 * Created by Psj on 2017/12/21.
 */
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.vero.easyclass.domain.ClassTime;
import edu.vero.easyclass.domain.Question;
import edu.vero.easyclass.domain.Vote;
import edu.vero.easyclass.domain.VoteOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/*
attendanceId改
 */
public class StuVoteActivity extends AppCompatActivity{
    private Button mBtn;
    RadioGroup radiogroup;
    RadioButton radio1,radio2;
    MyApplication application;
    ObjectMapper objectMapper;
    Vote newestVote;
    Set<VoteOption> voteOption;
    Set<VoteOption> voteOption1;
    public void onCreate(Bundle savedInstanceState){
        application=(MyApplication)getApplicationContext();
        voteOption=new HashSet<>();
        voteOption1=new HashSet<>();
        newestVote=new Vote();
        objectMapper=new ObjectMapper();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_vote);
    }
    //onClick事件对应的方法必须是public
    public void getResult(View v){
        radiogroup=(RadioGroup)findViewById(R.id.radiogroup);
        String result;
        if(radiogroup.getCheckedRadioButtonId()==R.id.radio_yes)
            result="懂了";
        else
            result="不懂";
        Log.d("tag",result);
        onSuccess(result);
    }
    private void onSuccess( String result){
        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication application=(MyApplication)getApplicationContext();
                Intent intent = getIntent();
                int tag = intent.getIntExtra("tag",-1);
                //int attendanceId=application.getAttendanceId();
                int attendanceId=application.getAttendanceId();
        //返回attendanceId的最新的VoteId

                HttpClient httpClientVote=new DefaultHttpClient();
                HttpGet httpGetVote=new HttpGet(application.getBaseURL()+"/attendances/"
                +attendanceId+"/newest_vote");
                HttpResponse httpResponseVote= null;
                try {
                    httpResponseVote = httpClientVote.execute(httpGetVote);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    HttpEntity entityVote = httpResponseVote.getEntity();
                    objectMapper=new ObjectMapper();
                    try {
                        String responseVote = EntityUtils.toString(entityVote, "utf-8");
                       //System.out.println("123:"+responseVote);
                        SimpleDateFormat myDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                        newestVote = objectMapper.readValue(responseVote, Vote.class);
                        Log.e("tag",String.valueOf(newestVote.getVoteId()));
                        //System.out.println(newestVote);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                //发送VoteOption

                int voteOptionId=0;
                int count=0;
              //  voteOption=newestVote.getOptions();
                if(result=="懂了") {
                    Iterator<VoteOption> it = newestVote.getOptions().iterator();
                    while (it.hasNext()) {
                        VoteOption option = it.next();
                        if ( option.getOptionContent().equals("懂了") ) {
                            voteOptionId = option.getVoteOptionId();
                            count=option.getOptionCount()+1;
                        }
                    }
                }
                voteOption1=newestVote.getOptions();
                if(result=="不懂"){
                    Iterator<VoteOption> it1 = newestVote.getOptions().iterator();
                    while(it1.hasNext()){
                        VoteOption option = it1.next();
                        if (option.getOptionContent().equals("不懂")) {
                            voteOptionId = option.getVoteOptionId();
                            count= option.getOptionCount()+1;
                        }
                        }
                }
                ObjectMapper mapper = new ObjectMapper();
                VoteOption voteOption2 = new VoteOption();
                if(result=="懂了"){
                    voteOption2.setOptionContent("懂了");
                    voteOption2.setVote(newestVote);
                    voteOption2.setVoteOptionId(voteOptionId);
                    voteOption2.setOptionCount(count);
                }else{
                    voteOption2.setOptionContent("不懂");
                    voteOption2.setVote(newestVote);
                    voteOption2.setVoteOptionId(voteOptionId);
                    voteOption2.setOptionCount(count);
                }
                HttpResponse httpResponseVoteOption=null;
                try {
                    SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    mapper.setDateFormat(myDateFormat);
                    String voteOptionJson = mapper.writeValueAsString(voteOption2);
                    HttpClient httpClientVoteOption=new DefaultHttpClient();
                    //发起post请求
                    HttpPut httpPutVoteOption=new HttpPut(application.getBaseURL()+
                            "/vote_options");

                    httpPutVoteOption.setEntity(new StringEntity(voteOptionJson,"UTF-8"));
                    try {
                        httpResponseVoteOption=httpClientVoteOption.execute(httpPutVoteOption);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                Log.e("status code:", String.valueOf(httpResponseVoteOption.getStatusLine().getStatusCode()));
if(httpResponseVoteOption.getStatusLine().getStatusCode()==200) {
    //跳转
    Looper.prepare();
    Toast.makeText(StuVoteActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
    Looper.loop();
}
            }
        }).start();
    }
    public void back(View v){
finish();
    }
}
