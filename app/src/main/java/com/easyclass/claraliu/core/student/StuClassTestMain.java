package com.easyclass.claraliu.core.student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;

/**
 * Created by Psj on 2017/12/23.
 */

public class StuClassTestMain extends AppCompatActivity {
    private Button mBtn1;
    private Button mBtn2;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stu_classroom_test);
    }
    public void beginTest(View v){
        Intent intent1 = getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent intent = getIntent();
        intent = new Intent(StuClassTestMain.this, StuNewClassTest.class);
        intent.putExtra("tag",i);
        startActivity(intent);
    }
    public void preTest(View v){
        Intent intent1 = getIntent();
        int i=intent1.getIntExtra("tag",-1);
        Intent intent = getIntent();
        intent = new Intent(StuClassTestMain.this,StuPreClassTest.class);
        intent.putExtra("tag",i);
        startActivity(intent);
    }
    public void back(View v){
        finish();
    }
}
