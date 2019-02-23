package com.example.user.plaspick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

public class Page1 extends AppCompatActivity {

    String Email;
    String Name;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Page1.this);
        if (acct != null) {
            Email = acct.getEmail();
            Name = acct.getGivenName();
            Toast.makeText(getApplicationContext(), Email, Toast.LENGTH_SHORT).show();
        }
        HashMap<String, String> getData = new HashMap<String, String>();
        getData.put("user_id", Email);
        getData.put("user_name", Name);

        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Page1.this, getData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (!(s.isEmpty())) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(Page1.this,MainActivity.class);
                    startActivity(intent2);


                }

            }
        });
        task2.execute("http://192.168.0.101/News/dbms.php");

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

        Button cam = (Button)findViewById(R.id.camera);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Page1.this,Upload.class);
                startActivity(intent);
            }
        });
    }

    //ehTHBSVo9IHstgctm7CKJgA- (Client Secret)
    //143624861542-t63jbmpo2skqasthti2js5jbe5pas0qn.apps.googleusercontent.com (Client ID)


    //@Override
    //protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //  super.onActivityResult(requestCode, resultCode, data);
    //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
    //imageView.setImageBitmap(bitmap);
    //}
}

