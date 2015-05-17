package com.osu.suzy.papernet.camera;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suzy on 5/16/15.
 */
public class UtilityCamera{

    public static File createImageFile() throws IOException{
        //create an image file name
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);

        return image;
    }

    public static byte[] putFileToBytes(String filepath) throws IOException{
        File file=new File(filepath);
        byte[] fileBytes=new byte[(int)file.length()];

        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream(file);
            inputStream.read(fileBytes);
        }finally {
            inputStream.close();
        }
        return fileBytes!= null && fileBytes.length > 0 ? fileBytes : null;
    }
}
