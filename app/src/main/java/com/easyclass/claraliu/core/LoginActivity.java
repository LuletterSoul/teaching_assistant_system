package com.easyclass.claraliu.core;
//登录仍需测试
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
import com.easyclass.claraliu.core.student.StudentMainActivity;
import com.easyclass.claraliu.core.teacher.TeacherMain;
import com.easyclass.claraliu.core.teacher.TeacherMain;
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

import edu.vero.easyclass.domain.Student;
import edu.vero.easyclass.domain.User;

public class LoginActivity extends Activity{

    private EditText loginAccount;//账号
    private EditText loginPassword;//密码
    private Button loginBtn;
    private Button registerBtn;
    private List<User> users;
    private MyApplication application ;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        application= (MyApplication) this.getApplicationContext();
        initJson();
        init();
    }

    //访问服务器，从而获取服务器传递过来的json数据
    private void initJson() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/users");
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);

                        Gson gson=new Gson();
                        //把JSON格式的字符串转为List
                        users = gson.fromJson(jsonString,new TypeToken<List<User>>(){}.getType());
                        for (User u: users){
                            System.out.println("把JSON格式的字符串转为List///" + u.getPassword());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 初始化数据
     */
    private void init() {
        loginAccount = (EditText) findViewById(R.id.login_account);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginBtn = (Button) findViewById(R.id.login_btn);
        registerBtn = (Button) findViewById(R.id.register_btn);

        //点击登录按钮
        loginBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = loginAccount.getText().toString();//账号
                String password = loginPassword.getText().toString();//密码
                Log.e("account",account);
                Log.e("password",password);

                int temp=0;
                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
                   //判断
                    for(int i=0;i<users.size();i++) {
                        if (account.equals(users.get(i).getUsername()) && password.equals(users.get(i).getPassword())) {
                            temp=1;
                            onSuccess(users.get(i).getUserId());
                        }
                    }
                    if(temp==0)
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "账号密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击注册按钮
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onSuccess(int UserId) {
        //将用户的账号设置为全局变量
        application.setUserId(UserId);
        //用于判断身份，是学生还是老师
        new Thread(new Runnable() {
            @Override
            public void run() {
                int temp=0;

                HttpClient httpClient=new DefaultHttpClient();
                //指定访问的服务器地址
                HttpGet httpGet=new HttpGet(application.getBaseURL()+"/students");
                try {
                    HttpResponse httpResponse=httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        HttpEntity entity=httpResponse.getEntity();
                        String jsonString= EntityUtils.toString(entity,"utf-8");
                        //用此方法在日志中打印信息
                        Log.e("tag",jsonString);

                        Gson gson=new Gson();
                        //把JSON格式的字符串转为List
                        List<Student> students = gson.fromJson(jsonString,new TypeToken<List<Student>>(){}.getType());
                        for (Student s: students){
                            System.out.println("把JSON格式的字符串转为List//" + s.getStudentId());
                        }

                        for(int i=0;i<students.size();i++){
                            int studentUserId= students.get(i).getUserId();
                            if(UserId==studentUserId){
                                //将学生的学号存入全局变量
                                application.setStudentId(students.get(i).getStudentId());
                                //是学生就转到学生主界面
                                temp=1;
                                Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                                //修改第159行
                                intent.putExtra("studentName",students.get(i).getStudentName());
                                startActivity(intent);
                                finish();
                            }
                        }
                        if(temp==0){
                            //不是学生就跳转到老师界面
                            Intent intent = new Intent(LoginActivity.this, TeacherMain.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
