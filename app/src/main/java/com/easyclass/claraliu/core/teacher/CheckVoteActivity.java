package com.easyclass.claraliu.core.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easyclass.claraliu.core.LoginActivity;
import com.easyclass.claraliu.core.R;
import com.easyclass.claraliu.core.application.MyApplication;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;

import edu.vero.easyclass.domain.Vote;
import edu.vero.easyclass.domain.VoteOption;

/**
 * Created by Administrator on 2017/12/29.
 */

public class CheckVoteActivity extends AppCompatActivity{
    MyApplication application;
    private Vote newestVote;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkvote);
        newestVote=new Vote();
        application=(MyApplication)getApplicationContext();
        int attendanceId=application.getAttendanceId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClientVote = new DefaultHttpClient();
                HttpGet httpGetVote = new HttpGet(application.getBaseURL() + "/attendances/"
                        + attendanceId + "/newest_vote");
                HttpResponse httpResponseVote = null;
                try {
                    httpResponseVote = httpClientVote.execute(httpGetVote);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (httpResponseVote.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entityVote = httpResponseVote.getEntity();
                    try {
                        String responseVote = EntityUtils.toString(entityVote, "utf-8");
                         System.out.println("123"+responseVote);
                        ObjectMapper objectMapper = new ObjectMapper();
                        SimpleDateFormat myDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        objectMapper.setDateFormat(myDateFormat);
                        newestVote = objectMapper.readValue(responseVote, Vote.class);
                        //    Log.e("tag",newestVote.getOptions().toString());
                        TextView textView1 = (TextView) findViewById(R.id.testView11);
                        TextView textView2 = (TextView) findViewById(R.id.testView12);
                        //     Set<VoteOption> voteOptions=newestVote.getOptions();
                        //     Log.e("tag",voteOptions.toString());
                        Iterator<VoteOption> it = newestVote.getOptions().iterator();
                        VoteOption[] voteOption = new VoteOption[4];
                        int i = 0;
                        while (it.hasNext()) {
                            voteOption[i] = it.next();
                            i++;
                        }
                        textView1.setText(voteOption[0].getOptionCount().toString());
                        textView2.setText(voteOption[1].getOptionCount().toString());
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        }

public void back(View v){
finish();
}


}
