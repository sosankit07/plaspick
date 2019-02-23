package com.example.user.plaspick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

public class Result extends AppCompatActivity {

    String pred;
    String time;
    String Email;
    String Score;
    String[] words;
    String parleg;
    String balaji;
    String[] string2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle bundle = getIntent().getExtras();
        pred = bundle.getString("pred");
        time = bundle.getString("time");
        Email = bundle.getString("email");
        TextView textView = (TextView) findViewById(R.id.texter);
        textView.setText(pred);
        words = pred.split(" ");
        Score = words[1]; //total ___ objects detected....
        string2 =  words[6].split(",");
        parleg = string2[0];
        balaji = words [8];
        Button retry =  (Button) findViewById(R.id.back);
        Button valid = (Button) findViewById(R.id.valid);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Result.this,Upload.class);
                startActivity(intent);
            }
        });
        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendResult();
             }
        });
    }
    public void sendResult() {

        HashMap<String, String> getData = new HashMap<String, String>();
        getData.put("email", Email);
        getData.put("score", Score);
        getData.put("parle", parleg);
        getData.put("balaji", balaji);

        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Result.this, getData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (!(s.isEmpty())) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(Result.this,Page1.class);
                    startActivity(intent2);


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        task2.execute("http://192.168.0.101/News/dbms_2.php");

        task2.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Toast.makeText(getApplicationContext(), "Cannot connect to server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Toast.makeText(getApplicationContext(), "Protocol Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Toast.makeText(getApplicationContext(), "Encoding Error!", Toast.LENGTH_SHORT).show();

            }
        });



    }


}
