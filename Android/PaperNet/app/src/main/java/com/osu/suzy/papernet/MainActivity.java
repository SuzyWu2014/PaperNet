package com.osu.suzy.papernet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;

import com.osu.suzy.papernet.camera.UtilityCamera;

public class MainActivity extends Activity {

    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE=1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //take a photo and upload
        Button btn_take=(Button)findViewById(R.id.btn_take);
        btn_take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCamera();
            }
        });

        //upload a photo
        Button btn_upload=(Button)findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //search for a paper
        Button btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    //send an intent to capture a photo
    private void displayCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //create the file where the photo should go
            File photoFile=null;
            try {
                photoFile=UtilityCamera.createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(photoFile!=null){
                mCurrentPhotoPath=photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Upload photo
                //get photo bytes
            byte[] ptobytes=null;
            try {
              ptobytes= UtilityCamera.putFileToBytes(mCurrentPhotoPath);
              UploadPhoto(ptobytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent intent=new Intent(MainActivity.this, Activity_web.class);
            intent.putExtra("new_photo_path",mCurrentPhotoPath);
           // startActivity(intent);
            finish();
        }
    }

    private void UploadPhoto(byte[] fileBytes){
        StringBuffer err=new StringBuffer();

        if(fileBytes==null || fileBytes.length==0){
            err.append("Photo is missing");
        }

        if(err.length()>0){
            new AlertDialog.Builder(this).setTitle("Warning").setMessage(err+". Continue?").setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //retake photo
                }
            }).setNegativeButton(android.R.string.no,null);

        }else{

        }

        //delete file when finish upload
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
