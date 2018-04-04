package com.easyclass.claraliu.core.student;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Clara liu on 2017/12/20.
 */

public class UploadThread extends Thread{
    private String fileAbsolutePath;
    private String url;
    public UploadThread(String fileAbsolutePath, String url){
        this.fileAbsolutePath=fileAbsolutePath;
        this.url=url;
    }

    public void run(){
        uploadHttpClient();
    }

    private void uploadHttpClient() {
        HttpClient client=new DefaultHttpClient();
        HttpPost post=new HttpPost(url);

        //文件的路径和文件名要改
        //File parent= Environment.getExternalStorageDirectory();
        //File filAbs=new File(parent,"xxxx");

        MultipartEntity muti=new MultipartEntity();
        FileBody fileBody=new FileBody(new File(fileAbsolutePath));
        muti.addPart("file",fileBody);
        post.setEntity(muti);
        try{
            HttpResponse response=client.execute(post);
            if(response.getStatusLine().getStatusCode()==201){
                Log.i("meng", EntityUtils.toString(response.getEntity())+"");
            }
        }catch(ClientProtocolException e1){
            e1.printStackTrace();
        }catch(IOException e1){
            e1.printStackTrace();
        }
    }
}
