package com.example.user.plaspick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class Upload extends AppCompatActivity  {

    GoogleSignInClient mGoogleSignInClient;
    ImageView mimageView;
    String time;
    String Email;
    private String UploadUrl = "http://192.168.0.100/News/Upload.php";
    Button button2;
    TextView textView;
    String encodedImage = null;
    CameraPhoto cameraPhoto;
    //final int CAMERA_REQUEST = 13323;
    Uri selectedImage;
    Bitmap b = null;
    private final int PICK_IMAGE_CAMERA = 1;
    public int counter;
    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            textView.setText(String.valueOf(counter));
            counter--;
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(Upload.this, Upload.class);
            //intent.putExtra("Name",Name);
            //intent.putExtra("Email",Email);
            //intent.putExtra("photo",photo.toString());
            //intent.putExtra("id",ID);
            //intent.putExtra("GivenName",GivenName);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        mimageView = (ImageView) this.findViewById(R.id.imageviewer);
        //Button button = (Button) this.findViewById(R.id.takePicture);
        textView = (TextView) this.findViewById(R.id.counter);
        button2 = (Button) this.findViewById(R.id.uploadPicture);
        button2.setVisibility(View.INVISIBLE);
        counter = 60;
        cameraPhoto = new CameraPhoto(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Upload.this);
        if (acct != null) {
            Email = acct.getEmail();
            Toast.makeText(getApplicationContext(), Email, Toast.LENGTH_SHORT).show();
        }


    }

    public void takeImageFromCamera(View view) {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Kumel/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";

        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException ignored) {

        }

        selectedImage = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //set Clicked picture on ImageView
        //if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        //  Bitmap mphoto = (Bitmap) data.getExtras().get("data");
        // mimageView.setImageBitmap(mphoto);

        //Upload image on server by converting to baseX64
        //BitmapDrawable drawable = (BitmapDrawable) mimageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //byte[] byteArray = byteArrayOutputStream .toByteArray();
        //String image_byte=String.valueOf(byteArray);

        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
        //  encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //}
        //Image can now be uploaded

        try {
            if (selectedImage != null) {
                if (selectedImage.toString().startsWith("file:")) {

                    File DKNY = new File(selectedImage.getPath());

                    InputStream in = null;
                    try {
                        final int IMAGE_MAX_SIZE = 1200000;
                        in = getContentResolver().openInputStream(selectedImage);
                        // Decode image size
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(in, null, o);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int scale = 1;
                        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                                IMAGE_MAX_SIZE) {
                            scale++;
                        }

                        in = getContentResolver().openInputStream(selectedImage);
                        if (scale > 1) {
                            scale--;
                            // scale to max possible inSampleSize that still yields an image
                            // larger than target
                            o = new BitmapFactory.Options();
                            o.inSampleSize = scale;
                            b = BitmapFactory.decodeStream(in, null, o);

                            // resize to desired dimensions
                            int height = b.getHeight();
                            int width = b.getWidth();

                            double y = Math.sqrt(IMAGE_MAX_SIZE
                                    / (((double) width) / height));
                            double x = (y / height) * width;

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                                    (int) y, true);
                            b.recycle();
                            b = scaledBitmap;

                            System.gc();
                        } else {
                            b = BitmapFactory.decodeStream(in);
                        }
                        in.close();
                        mimageView.setImageBitmap(b);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}

        button2.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        countDownTimer.start();

    }

    public void uploadImage(View view) {
        countDownTimer.cancel();
        //initRxTrueTime();
        //if (time == "NO"){
          //  return;
        //}

        textView.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        //String image_byte=String.valueOf(byteArray);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("image", encodedImage);
        postData.put("email", Email);

        PostResponseAsyncTask task = new PostResponseAsyncTask(Upload.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Uploaded_Success")) {

                    Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
                    try {
                        TimeUnit.SECONDS.sleep(8);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                    countDownTimer.start();
                }

            }
        });
        task.execute("http://192.168.0.101/News/Upload.php");
        task.setEachExceptionsHandler(new EachExceptionsHandler() {
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

        HashMap<String, String> getData = new HashMap<String, String>();
        getData.put("email", Email);

        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Upload.this, getData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (!(s.isEmpty())) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                    Intent intent2 = new Intent(Upload.this,Result.class);
                        intent2.putExtra("pred",s);
                        intent2.putExtra("time",time);
                        intent2.putExtra("email",Email);
                        startActivity(intent2);


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        task2.execute("http://192.168.0.101/News/Download.php");

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

    private void initRxTrueTime() {
        DisposableSingleObserver<Date> disposable = TrueTimeRx.build()
                .withConnectionTimeout(31_428)
                .withRetryCount(100)
                .withSharedPreferencesCache(this)
                .withLoggingEnabled(true)
                .initializeRx("time.google.com")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Date>() {
                    @Override
                    public void onSuccess(Date date) {
                        //Log.d(TAG, "Success initialized TrueTime :" + date.toString());
                        Toast.makeText(Upload.this, "Success TrueTime :" +date.toString(), Toast.LENGTH_SHORT).show();
                        time = date.toString();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Upload.this, "Failed TrueTime ", Toast.LENGTH_SHORT).show();
                        time = "NO";

                        //Log.e(TAG, "something went wrong when trying to initializeRx TrueTime", e);
                    }
                });
    }


}