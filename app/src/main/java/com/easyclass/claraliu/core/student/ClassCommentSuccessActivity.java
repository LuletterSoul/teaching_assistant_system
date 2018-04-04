package com.easyclass.claraliu.core.student;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.easyclass.claraliu.core.R;

public class ClassCommentSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_comment_success);
    }

    public void back(View v){
        finish();
    }
}
