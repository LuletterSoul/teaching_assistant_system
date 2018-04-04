package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;

import edu.vero.easyclass.domain.OnlineClassTest;
import edu.vero.easyclass.domain.Question;
import edu.vero.easyclass.domain.QuestionOption;
import edu.vero.easyclass.domain.TestRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Psj on 2017/12/23.
 */
 /**
   学生测试
 */
public class StuNewClassTest extends AppCompatActivity {
     String answer=null;
    Button mBtn;
    RadioGroup radioGroup1;
    RadioButton radio11,radio12,radio13;
    RadioGroup radioGroup2;
    RadioButton radio21,radio22,radio23;
    RadioGroup radioGroup3;
    RadioButton radio31,radio32,radio33;
    RadioGroup radioGroup4;
    RadioButton radio41,radio42,radio43;
    RadioGroup radioGroup5;
    RadioButton radio51,radio52,radio53;
    TextView text1,text2,text3,text4,text5;
    TextView text11,text12,text13,text14,text21,text22,text23,text24,
            text31,text32,text33,text34,text41,text42,text43,text44,
            text51,text52,text53,text54;
    MyApplication application;
    int arrangementId;
    ObjectMapper objectMapper;
    int testId;
    String[] question;   //保存问题
    int[] questionId;    //保存当此测试的testId
    int[] answerOrder;   //保存当前问题的答案序号
     private OnlineClassTest newestTest;
     private List<Question> questions;
    int score;
    int tag;
     OnlineClassTest onlineClassTest1=new OnlineClassTest();
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_new_test);
        question=new String[10];
        questionId=new int[10];
        answerOrder=new int [10];
        new Thread(new Runnable() {

            public void run() {
                application = (MyApplication)

                        getApplicationContext();
                Intent intent = getIntent();
                tag = intent.getIntExtra("tag",-1);
                text1 = (TextView)

                        findViewById(R.id.text1);

                text11 = (TextView)

                        findViewById(R.id.text11);

                text12 = (TextView)

                        findViewById(R.id.text12);

                text13 = (TextView)

                        findViewById(R.id.text13);

                text14 = (TextView)

                        findViewById(R.id.text14);

                text2 = (TextView)

                        findViewById(R.id.text2);

                text21 = (TextView)

                        findViewById(R.id.text21);

                text22 = (TextView)

                        findViewById(R.id.text22);

                text23 = (TextView)

                        findViewById(R.id.text23);

                text24 = (TextView)

                        findViewById(R.id.text24);

                text3 = (TextView)

                        findViewById(R.id.text3);

                text31 = (TextView)

                        findViewById(R.id.text31);

                text32 = (TextView)

                        findViewById(R.id.text32);

                text33 = (TextView)

                        findViewById(R.id.text33);

                text34 = (TextView)

                        findViewById(R.id.text34);

                text4 = (TextView)

                        findViewById(R.id.text4);

                text41 = (TextView)

                        findViewById(R.id.text41);

                text42 = (TextView)

                        findViewById(R.id.text42);

                text43 = (TextView)

                        findViewById(R.id.text43);

                text44 = (TextView)

                        findViewById(R.id.text44);

                text5 = (TextView)

                        findViewById(R.id.text5);

                text51 = (TextView)

                        findViewById(R.id.text51);

                text52 = (TextView)

                        findViewById(R.id.text52);

                text53 = (TextView)

                        findViewById(R.id.text53);

                text54 = (TextView)

                        findViewById(R.id.text54);
                //获取arrangementId
                objectMapper=new ObjectMapper();
                arrangementId = application.getStudentClassSchedules().get(tag).getTeacherArrangement().getArrangementId();
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
                //获取当前最新的testId
                HttpClient httpClient6 = new DefaultHttpClient();
                HttpGet httpGet6 = new HttpGet(application.getBaseURL() + "/arrangements/" + String.valueOf(
                        arrangementId) + "/opening_tests");
                try

                {
                    HttpResponse httpResponse6 = httpClient6.execute(httpGet6);
                    if (httpResponse6.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity6 = httpResponse6.getEntity();
                        String response = EntityUtils.toString(entity6, "utf-8");
                        Gson gson = new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm")
                                .create();
//                        List<OnlineClassTest> onlineClassTests = new ArrayList<>();
//                        onlineClassTests = objectMapper.readValue(response, new TypeReference<List<OnlineClassTest>>(){});
                        List<OnlineClassTest> onlineClassTests = new ArrayList<>();
                        Type type = new TypeToken<List<OnlineClassTest>>() {}.getType();
                        onlineClassTests = gson.fromJson(response, type);
                        newestTest = onlineClassTests.get(0);
                        testId = newestTest.getTestId();
//                        Question[] questions = (Question[]) newestTest.getQuestions().toArray();
                        questions = newestTest.getQuestions();

                        text1.setText(questions.get(0).getContent());
                        text2.setText(questions.get(1).getContent());
                        text3.setText(questions.get(2).getContent());
                        text4.setText(questions.get(3).getContent());
                        text5.setText(questions.get(4).getContent());

                        for (Question question :questions
                             ) {
                            Set<QuestionOption> options = question.getOptions();
                        }
                        for(int i =0;i<questions.size();i++) {
                            Question question = questions.get(i);
                            List<QuestionOption> questionOptions = new ArrayList<>(question.getOptions());
                            switch (i) {
                                case 0:
                                    text11.setText(questionOptions.get(0).getContent());
                                    text12.setText(questionOptions.get(1).getContent());
                                    text13.setText(questionOptions.get(2).getContent());
                                    text14.setText(questionOptions.get(3).getContent());
                                    break;
                                case 1:
                                    text21.setText(questionOptions.get(0).getContent());
                                    text22.setText(questionOptions.get(1).getContent());
                                    text23.setText(questionOptions.get(2).getContent());
                                    text24.setText(questionOptions.get(3).getContent());
                                    break;
                                case 2:
                                    text31.setText(questionOptions.get(0).getContent());
                                    text32.setText(questionOptions.get(1).getContent());
                                    text33.setText(questionOptions.get(2).getContent());
                                    text34.setText(questionOptions.get(3).getContent());
                                    break;
                                case 3:
                                    text41.setText(questionOptions.get(0).getContent());
                                    text42.setText(questionOptions.get(1).getContent());
                                    text43.setText(questionOptions.get(2).getContent());
                                    text44.setText(questionOptions.get(3).getContent());
                                    break;
                                case 4:
                                    text51.setText(questionOptions.get(0).getContent());
                                    text52.setText(questionOptions.get(1).getContent());
                                    text53.setText(questionOptions.get(2).getContent());
                                    text54.setText(questionOptions.get(3).getContent());
                                    break;
                            }
                        }

                    }
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
//
//                //根据testId获取测试的questionId
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(application.getBaseURL() + "/tests/" + String.valueOf(
//                        testId) + "/questions");
//                try
//
//                {
//                    HttpResponse httpResponse = httpClient.execute(httpGet);
//                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse.getEntity();
//                        String response = EntityUtils.toString(entity, "utf-8");
//                        List<Question> questionList = objectMapper.readValue(response,
//                                new TypeReference<List<Question>>() {
//                        });
//                        //显示五道题目
//                        for (int i = 0; i < questionList.size(); i++) {
//                            question[i] = questionList.get(i).getContent();
//                          System.out.println("hahaha"+ question[i]);
//                            questionId[i] = questionList.get(i).getQuestionId();
//                            answerOrder[i] = questionList.get(i).getAnswerOrder();
//                        }
//
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }

                //显示五道问题的选项
                //第一道问题
//                String[] answerOption1 = new String[10];
//                HttpClient httpClient1 = new DefaultHttpClient();
//                HttpGet httpGet1 = new HttpGet(application.getBaseURL() + "/questions/" +
//                        String.valueOf(questionId[0]) + "/options");
//             try
//                {
//                    HttpResponse httpResponse1 = httpClient1.execute(httpGet1);
//                    if (httpResponse1.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity1 = httpResponse1.getEntity();
//                        String response = EntityUtils.toString(entity1, "utf-8");
//                        List<QuestionOption> questionOptionsList=new ArrayList<QuestionOption>();
//                        questionOptionsList = objectMapper.readValue(response, new TypeReference<List<QuestionOption>>(){
//                        });
//                        for (int i = 0; i < questionOptionsList.size(); i++) {
//                            answerOption1[i] = questionOptionsList.get(i).getContent();
//                        }
//                        text11.setText(answerOption1[0]);
//                        text12.setText(answerOption1[1]);
//                        text13.setText(answerOption1[2]);
//                        text14.setText(answerOption1[3]);
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }
//
//                //第二道问题
//                String[] answerOption2 = new String[10];
//                HttpClient httpClient2 = new DefaultHttpClient();
//                HttpGet httpGet2 = new HttpGet(application.getBaseURL() + "/questions/" +
//                        String.valueOf(questionId[1]) + "/options");
//                try
//
//                {
//                    HttpResponse httpResponse2 = httpClient2.execute(httpGet2);
//                    if (httpResponse2.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse2.getEntity();
//                        String response = EntityUtils.toString(entity, "utf-8");
//                        List<QuestionOption> questionOptionsList = objectMapper.readValue
//                                (response, new TypeReference<List<QuestionOption>>() {
//                                });
//                        for (int i = 0; i < questionOptionsList.size(); i++) {
//                            answerOption2[i] = questionOptionsList.get(i).getContent();
//                        }
//                        text21.setText(answerOption2[0]);
//                        text22.setText(answerOption2[1]);
//                        text23.setText(answerOption2[2]);
//                        text24.setText(answerOption2[3]);
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }
//
//                String[] answerOption3 = new String[10];
//                HttpClient httpClient3 = new DefaultHttpClient();
//                HttpGet httpGet3 = new HttpGet(application.getBaseURL() + "/questions/" +
//                        String.valueOf(questionId[2]) + "/options");
//                try
//
//                {
//                    HttpResponse httpResponse3 = httpClient3.execute(httpGet3);
//                    if (httpResponse3.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse3.getEntity();
//                        String response = EntityUtils.toString(entity, "utf-8");
//                        List<QuestionOption> questionOptionsList = objectMapper.readValue
//                                (response, new TypeReference<List<QuestionOption>>() {
//                                });
//                        for (int i = 0; i < questionOptionsList.size(); i++) {
//                            answerOption3[i] = questionOptionsList.get(i).getContent();
//                        }
//                        text31.setText(answerOption3[0]);
//                        text32.setText(answerOption3[1]);
//                        text33.setText(answerOption3[2]);
//                        text34.setText(answerOption3[3]);
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }
//
//                String[] answerOption4 = new String[10];
//                HttpClient httpClient4 = new DefaultHttpClient();
//                HttpGet httpGet4 = new HttpGet(application.getBaseURL() + "/questions/" +
//                        String.valueOf(questionId[3]) + "/options");
//                try
//
//                {
//                    HttpResponse httpResponse4 = httpClient4.execute(httpGet4);
//                    if (httpResponse4.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse4.getEntity();
//                        String response = EntityUtils.toString(entity, "utf-8");
//                        List<QuestionOption> questionOptionsList = objectMapper.readValue
//                                (response, new TypeReference<List<QuestionOption>>() {
//                                });
//                        for (int i = 0; i < questionOptionsList.size(); i++) {
//                            answerOption4[i] = questionOptionsList.get(i).getContent();
//                        }
//                        text41.setText(answerOption4[0]);
//                        text42.setText(answerOption4[1]);
//                        text43.setText(answerOption4[2]);
//                        text44.setText(answerOption4[3]);
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }
//
//                String[] answerOption5 = new String[10];
//                HttpClient httpClient5 = new DefaultHttpClient();
//                HttpGet httpGet5 = new HttpGet(application.getBaseURL() + "/questions/" +
//                        String.valueOf(questionId[4]) + "/options");
//                try
//
//                {
//                    HttpResponse httpResponse5 = httpClient5.execute(httpGet5);
//                    if (httpResponse5.getStatusLine().getStatusCode() == 200) {
//                        HttpEntity entity = httpResponse5.getEntity();
//                        String response = EntityUtils.toString(entity, "utf-8");
//                        List<QuestionOption> questionOptionsList = objectMapper.readValue
//                                (response, new TypeReference<List<QuestionOption>>() {
//                                });
//                        for (int i = 0; i < questionOptionsList.size(); i++) {
//                            answerOption5[i] = questionOptionsList.get(i).getContent();
//                        }
//                        text51.setText(answerOption5[0]);
//                        text52.setText(answerOption5[1]);
//                        text53.setText(answerOption5[2]);
//                        text54.setText(answerOption5[3]);
//                    }
//                } catch (
//                        IOException e)
//
//                {
//                    e.printStackTrace();
//                }
            }
        }).start();
    }
    public void Post(View v) throws UnsupportedEncodingException {
        for (Question q :
                questions) {
            Log.e("answer order:", String.valueOf(q.getAnswerOrder()));
        }
        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        Log.e("checked id 1", String.valueOf(radioGroup1.getCheckedRadioButtonId()));
        Log.e("checked 【-id ", String.valueOf(R.id.radio11));
        if(radioGroup1.getCheckedRadioButtonId()==R.id.radio11&&questions.get(0).getAnswerOrder()==1){
            score+=20;
            answer+="A"+",";
        }
        if(radioGroup1.getCheckedRadioButtonId()==R.id.radio12&&questions.get(0).getAnswerOrder()==2){
            score+=20;
            answer+="B"+",";
        }
        if(radioGroup1.getCheckedRadioButtonId()==R.id.radio13&&questions.get(0).getAnswerOrder()==3){
            score+=20;
            answer+="C"+",";
        }
        if(radioGroup1.getCheckedRadioButtonId()==R.id.radio14&&questions.get(0).getAnswerOrder()==4){
            score+=20;
            answer+="D"+",";
        }

        radioGroup2=(RadioGroup)findViewById(R.id.radioGroup2);
        if(radioGroup2.getCheckedRadioButtonId()==R.id.radio21&&questions.get(1).getAnswerOrder()==1){
            score+=20;
            answer+="A"+",";
        }
        if(radioGroup2.getCheckedRadioButtonId()==R.id.radio22&&questions.get(1).getAnswerOrder()==2){
            score+=20;
            answer+="B"+",";
        }
        if(radioGroup2.getCheckedRadioButtonId()==R.id.radio23&&questions.get(1).getAnswerOrder()==3){
            score+=20;
            answer+="C"+",";
        }
        if(radioGroup2.getCheckedRadioButtonId()==R.id.radio24&&questions.get(1).getAnswerOrder()==4){
            score+=20;
            answer+="D"+",";
        }

        radioGroup3=(RadioGroup)findViewById(R.id.radioGroup3);
        if(radioGroup3.getCheckedRadioButtonId()==R.id.radio31&&questions.get(2).getAnswerOrder()==1){
            score+=20;
            answer+="A"+",";
        }
        if(radioGroup3.getCheckedRadioButtonId()==R.id.radio32&&questions.get(2).getAnswerOrder()==2){
            score+=20;
            answer+="B"+",";
        }
        if(radioGroup3.getCheckedRadioButtonId()==R.id.radio33&&questions.get(2).getAnswerOrder()==3){
            score+=20;
            answer+="C"+",";
        }
        if(radioGroup3.getCheckedRadioButtonId()==R.id.radio34&&questions.get(2).getAnswerOrder()==4){
            score+=20;
            answer+="D"+",";
        }

        radioGroup4=(RadioGroup)findViewById(R.id.radioGroup4);
        if(radioGroup4.getCheckedRadioButtonId()==R.id.radio41&&questions.get(3).getAnswerOrder()==1){
            score+=20;
            answer+="A"+",";
        }
        if(radioGroup4.getCheckedRadioButtonId()==R.id.radio42&&questions.get(3).getAnswerOrder()==2){
            score+=20;
            answer+="B"+",";
        }
        if(radioGroup4.getCheckedRadioButtonId()==R.id.radio43&&questions.get(3).getAnswerOrder()==3){
            score+=20;
            answer+="C"+",";
        }
        if(radioGroup4.getCheckedRadioButtonId()==R.id.radio44&&questions.get(3).getAnswerOrder()==4){
            score+=20;
            answer+="D"+",";
        }

        radioGroup5=(RadioGroup)findViewById(R.id.radioGroup5);
        if(radioGroup5.getCheckedRadioButtonId()==R.id.radio51&&questions.get(4).getAnswerOrder()==1){
            score+=20;
            answer+="A";
        }
        if(radioGroup5.getCheckedRadioButtonId()==R.id.radio52&&questions.get(4).getAnswerOrder()==2){
            score+=20;
            answer+="B";
        }
        if(radioGroup5.getCheckedRadioButtonId()==R.id.radio53&&questions.get(4).getAnswerOrder()==3){
            score+=20;
            answer+="C";
        }
        if(radioGroup5.getCheckedRadioButtonId()==R.id.radio54&&questions.get(4).getAnswerOrder()==4){
            score+=20;
            answer+="D";
        }

        //将testOption上传至服务器
        new Thread(new Runnable(){
            public void run() {
                ObjectMapper mapper = new ObjectMapper();
                TestRecord testRecord = new TestRecord();
                testRecord.setAnswer(answer);
                testRecord.setScore(score);
                testRecord.setTest(newestTest);
                testRecord.setSchedule(application.getStudentClassSchedules().get(tag));
                try {
                    String testJackson = mapper.writeValueAsString(testRecord);
                    HttpClient httpClientPost = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(application.getBaseURL() + "/test_records");
                    HttpResponse httpResponse = null;
                    try {
                        httpPost.setEntity(new StringEntity(testJackson));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {
                        httpResponse = httpClientPost.execute(httpPost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        //跳转
                        Looper.prepare();
                        Toast.makeText(StuNewClassTest.this, "提交成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
    }).start();
    }
     public void back(View v){
         finish();
     }

     public void post1(View view) {
         Toast.makeText(StuNewClassTest.this, "提交成功！", Toast.LENGTH_SHORT).show();
     }
}
