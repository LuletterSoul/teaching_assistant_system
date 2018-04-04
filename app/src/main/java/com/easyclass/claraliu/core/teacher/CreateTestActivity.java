package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.easyclass.claraliu.core.student.StuVoteActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.vero.easyclass.domain.OnlineClassTest;
import edu.vero.easyclass.domain.Question;
import edu.vero.easyclass.domain.QuestionOption;

/**
 * Created by Administrator on 2017/12/29.
 */

public class CreateTestActivity extends AppCompatActivity{
    MyApplication application;
    List<Question> questionList;

    public void onCreate(Bundle savedInstanceState) {
        application=(MyApplication)getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_test);
    }
    public void postTest(View v) throws JsonProcessingException, UnsupportedEncodingException {
        Intent intent =getIntent();
        int tag =  intent.getIntExtra("tag",-1);
        OnlineClassTest onlineClassTest1;
        EditText text1=(EditText)findViewById(R.id.text1);
        EditText radio11=(EditText)findViewById(R.id.radio11);
        EditText radio12=(EditText)findViewById(R.id.radio12);
        EditText radio13=(EditText)findViewById(R.id.radio13);
        EditText radio14=(EditText)findViewById(R.id.radio14);
        EditText radiogroup1=(EditText)findViewById(R.id.radiogroup1);
        QuestionOption questionOption11=new QuestionOption();
        questionOption11.setOptionOrder(1);
        questionOption11.setContent(radio11.getText().toString());
        QuestionOption questionOption12=new QuestionOption();
        questionOption12.setOptionOrder(2);
        questionOption12.setContent(radio12.getText().toString());
        QuestionOption questionOption13=new QuestionOption();
        questionOption13.setOptionOrder(3);
        questionOption13.setContent(radio13.getText().toString());
        QuestionOption questionOption14=new QuestionOption();
        questionOption14.setOptionOrder(4);
        questionOption14.setContent(radio14.getText().toString());
        String answer1=radiogroup1.getText().toString();
        Set<QuestionOption> questionOption1=new HashSet<QuestionOption>();
        questionOption1.add(questionOption11);
        questionOption1.add(questionOption12);
        questionOption1.add(questionOption13);
        questionOption1.add(questionOption14);
        Question question1=new Question();
        int answerorder1=0;
        if(answer1=="A"){
            answerorder1=1;
        }else if(answer1=="B"){
            answerorder1=2;
        }else if(answer1=="C"){
            answerorder1=3;
        }else{
            answerorder1=4;
        }
        question1.setAnswerOrder(answerorder1);
        question1.setContent(text1.getText().toString());
        question1.setCourse(application.getTeacherArrangements().get(tag).getCourse());
        question1.setOptions(questionOption1);

        OnlineClassTest onlineClassTest2;
        EditText text2=(EditText)findViewById(R.id.text2);
        EditText radio21=(EditText)findViewById(R.id.radio21);
        EditText radio22=(EditText)findViewById(R.id.radio22);
        EditText radio23=(EditText)findViewById(R.id.radio23);
        EditText radio24=(EditText)findViewById(R.id.radio24);
        EditText radiogroup2=(EditText)findViewById(R.id.radiogroup2);
        QuestionOption questionOption21=new QuestionOption();
        questionOption21.setOptionOrder(1);
        questionOption21.setContent(radio21.getText().toString());
        QuestionOption questionOption22=new QuestionOption();
        questionOption22.setOptionOrder(2);
        questionOption22.setContent(radio22.getText().toString());
        QuestionOption questionOption23=new QuestionOption();
        questionOption23.setOptionOrder(3);
        questionOption23.setContent(radio23.getText().toString());
        QuestionOption questionOption24=new QuestionOption();
        questionOption24.setOptionOrder(4);
        questionOption24.setContent(radio24.getText().toString());
        String answer2=radiogroup2.getText().toString();
        Set<QuestionOption> questionOption2=new HashSet<QuestionOption>();
        questionOption1.add(questionOption21);
        questionOption1.add(questionOption22);
        questionOption1.add(questionOption23);
        questionOption1.add(questionOption24);
        Question question2=new Question();
        int answerorder2=0;
        if(answer2=="A"){
            answerorder2=1;
        }else if(answer2=="B"){
            answerorder2=2;
        }else if(answer2=="C"){
            answerorder2=3;
        }else{
            answerorder2=4;
        }
        question2.setAnswerOrder(answerorder2);
        question2.setContent(text2.getText().toString());
        question2.setCourse(application.getTeacherArrangements().get(tag).getCourse());
        question2.setOptions(questionOption2);


        OnlineClassTest onlineClassTest3;
        EditText text3=(EditText)findViewById(R.id.text3);
        EditText radio31=(EditText)findViewById(R.id.radio31);
        EditText radio32=(EditText)findViewById(R.id.radio32);
        EditText radio33=(EditText)findViewById(R.id.radio33);
        EditText radio34=(EditText)findViewById(R.id.radio34);
        EditText radiogroup3=(EditText)findViewById(R.id.radiogroup3);
        QuestionOption questionOption31=new QuestionOption();
        questionOption31.setOptionOrder(1);
        questionOption31.setContent(radio31.getText().toString());
        QuestionOption questionOption32=new QuestionOption();
        questionOption32.setOptionOrder(2);
        questionOption32.setContent(radio32.getText().toString());
        QuestionOption questionOption33=new QuestionOption();
        questionOption33.setOptionOrder(3);
        questionOption33.setContent(radio33.getText().toString());
        QuestionOption questionOption34=new QuestionOption();
        questionOption34.setOptionOrder(4);
        questionOption34.setContent(radio34.getText().toString());
        String answer3=radiogroup3.getText().toString();
        Set<QuestionOption> questionOption3=new HashSet<QuestionOption>();
        questionOption3.add(questionOption31);
        questionOption3.add(questionOption32);
        questionOption3.add(questionOption33);
        questionOption3.add(questionOption34);
        Question question3=new Question();
        int answerorder3=0;
        if(answer3=="A"){
            answerorder3=1;
        }else if(answer3=="B"){
            answerorder3=2;
        }else if(answer3=="C"){
            answerorder3=3;
        }else{
            answerorder3=4;
        }
        question3.setAnswerOrder(answerorder3);
        question3.setContent(text3.getText().toString());
        question3.setCourse(application.getTeacherArrangements().get(tag).getCourse());
        question3.setOptions(questionOption3);



        OnlineClassTest onlineClassTest4;
        EditText text4=(EditText)findViewById(R.id.text4);
        EditText radio41=(EditText)findViewById(R.id.radio41);
        EditText radio42=(EditText)findViewById(R.id.radio42);
        EditText radio43=(EditText)findViewById(R.id.radio43);
        EditText radio44=(EditText)findViewById(R.id.radio44);
        EditText radiogroup4=(EditText)findViewById(R.id.radiogroup4);
        QuestionOption questionOption41=new QuestionOption();
        questionOption41.setOptionOrder(1);
        questionOption41.setContent(radio41.getText().toString());
        QuestionOption questionOption42=new QuestionOption();
        questionOption42.setOptionOrder(2);
        questionOption42.setContent(radio42.getText().toString());
        QuestionOption questionOption43=new QuestionOption();
        questionOption43.setOptionOrder(3);
        questionOption43.setContent(radio43.getText().toString());
        QuestionOption questionOption44=new QuestionOption();
        questionOption44.setOptionOrder(4);
        questionOption44.setContent(radio44.getText().toString());
        String answer4=radiogroup4.getText().toString();
        Set<QuestionOption> questionOption4=new HashSet<QuestionOption>();
        questionOption4.add(questionOption41);
        questionOption4.add(questionOption42);
        questionOption4.add(questionOption43);
        questionOption4.add(questionOption44);
        Question question4=new Question();
        int answerorder4=0;
        if(answer4=="A"){
            answerorder4=1;
        }else if(answer4=="B"){
            answerorder4=2;
        }else if(answer4=="C"){
            answerorder4=3;
        }else{
            answerorder4=4;
        }
        question4.setAnswerOrder(answerorder4);
        question4.setContent(text4.getText().toString());
        question4.setCourse(application.getTeacherArrangements().get(tag).getCourse());
        question4.setOptions(questionOption4);




        OnlineClassTest onlineClassTest5;
        EditText text5=(EditText)findViewById(R.id.text5);
        EditText radio51=(EditText)findViewById(R.id.radio51);
        EditText radio52=(EditText)findViewById(R.id.radio52);
        EditText radio53=(EditText)findViewById(R.id.radio53);
        EditText radio54=(EditText)findViewById(R.id.radio54);
        EditText radiogroup5=(EditText)findViewById(R.id.radiogroup5);
        QuestionOption questionOption51=new QuestionOption();
        questionOption51.setOptionOrder(1);
        questionOption51.setContent(radio51.getText().toString());
        QuestionOption questionOption52=new QuestionOption();
        questionOption52.setOptionOrder(2);
        questionOption52.setContent(radio52.getText().toString());
        QuestionOption questionOption53=new QuestionOption();
        questionOption53.setOptionOrder(3);
        questionOption53.setContent(radio53.getText().toString());
        QuestionOption questionOption54=new QuestionOption();
        questionOption54.setOptionOrder(4);
        questionOption54.setContent(radio54.getText().toString());
        String answer5=radiogroup5.getText().toString();
        Set<QuestionOption> questionOption5=new HashSet<QuestionOption>();
        questionOption5.add(questionOption51);
        questionOption5.add(questionOption52);
        questionOption5.add(questionOption53);
        questionOption5.add(questionOption54);
        Question question5=new Question();
        int answerorder5=0;
        if(answer5=="A"){
            answerorder5=1;
        }else if(answer5=="B"){
            answerorder5=2;
        }else if(answer5=="C"){
            answerorder5=3;
        }else{
            answerorder5=4;
        }
        question5.setAnswerOrder(answerorder5);
        question5.setContent(text5.getText().toString());
        question5.setCourse(application.getTeacherArrangements().get(tag).getCourse());
        question5.setOptions(questionOption5);

        onlineClassTest1=new OnlineClassTest();
        questionList=new ArrayList<Question>();
        questionList.add(question1);
        questionList.add(question2);
        questionList.add(question3);
        questionList.add(question4);
        questionList.add(question5);
        onlineClassTest1.setArrangement(application.getTeacherArrangements().get(tag));
        onlineClassTest1.setQuestions(questionList);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        long currentTime=System.currentTimeMillis();
        onlineClassTest1.setEstablishedTime(curDate);
        currentTime +=5*60*1000;
        Date deadline=new Date(currentTime);
        onlineClassTest1.setDeadline(deadline);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ObjectMapper mapper = new ObjectMapper();
                String testJackson = null;
                try {
                    testJackson = mapper.writeValueAsString(onlineClassTest1);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                HttpClient httpClientPost=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(application.getBaseURL()+"/arrangements/"+application.getTeacherArrangements()
                        .get(tag).getArrangementId()+"/tests");
                HttpResponse httpResponse=null;
                try {
                    httpPost.setEntity(new StringEntity(testJackson));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    httpResponse=httpClientPost.execute(httpPost);
                    if(httpResponse.getStatusLine().getStatusCode()==201) {
                        Looper.prepare();
                        Toast.makeText(CreateTestActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
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
