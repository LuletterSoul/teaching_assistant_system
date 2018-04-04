package com.easyclass.claraliu.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import edu.vero.easyclass.domain.Student;

public class RegisterActivity extends Activity {

    private EditText loginName;//用户昵称
    private EditText account;//注册账号
    private EditText password;//注册密码
    private EditText passwordConfirm;//确认密码
    private Button registBtn;//注册
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        application= (MyApplication) this.getApplicationContext();
        initView();
    }

    private void initView() {
        loginName = (EditText) findViewById(R.id.regist_trueName);
        account = (EditText) findViewById(R.id.regist_account);
        password = (EditText) findViewById(R.id.regist_password);
        passwordConfirm=(EditText)findViewById(R.id.regist_passwordConfirm);
        registBtn = (Button) findViewById(R.id.regist_btn);

        registBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得用户输入的信息
                String trueName = loginName.getText().toString();
                String accountNum = account.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordCon = passwordConfirm.getText().toString();

                if (!TextUtils.isEmpty(trueName) && !TextUtils.isEmpty(accountNum)
                        && !TextUtils.isEmpty(passwordStr)&& !TextUtils.isEmpty(passwordCon)){
                    if(passwordCon.equals(passwordStr)) {
                        onSuccess(trueName, accountNum, passwordStr);
                    }
                    else
                        Toast.makeText(RegisterActivity.this, "确认密码错误！", Toast.LENGTH_SHORT).show();
                   //加入注册信息到数据库
                } else {
                    Toast.makeText(RegisterActivity.this, "输入信息未完全！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onSuccess(final String trueName, final String accountNum, final String passwordStr){
        Student user=new Student();
        user.setUsername(accountNum);
        user.setPassword(passwordStr);

        //开启子线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //发起post请求
                HttpPost httpPost=new HttpPost(application.getBaseURL()+"/users");
                HttpResponse httpResponse=null;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(user);
                    httpPost.setEntity(new StringEntity(json));
                    httpResponse=httpClient.execute(httpPost);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(httpResponse.getStatusLine().getStatusCode()==201) {
                    Log.e("success","register");
                    Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        }).start();
    }
    public void back(View v){
        finish();
    }
}

