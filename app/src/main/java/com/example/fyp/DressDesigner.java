package com.example.fyp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

public class DressDesigner extends Application implements Info {

    private static DressDesigner sInstance;

    public static void refreshGallery(String path) {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        Uri contentUri = Uri.fromFile(new File(path));

        mediaScanIntent.setData(contentUri);
        DressDesigner.getAppContext().sendBroadcast(mediaScanIntent);

    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        sInstance = this;

    }
}
