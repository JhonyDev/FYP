package com.example.fyp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class AsyncTaskBitmapWriter extends AsyncTask<Void, Void, Boolean> implements Info{

    private File file;
    private Bitmap bitmap;
    private Context mContext;

    public AsyncTaskBitmapWriter(File input_file, Bitmap input_bitmap, Context context) {
        file = input_file;
        bitmap = input_bitmap;
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... arg0) {
        try {
            Log.i(TAG, "doInBackground: ");

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, true);
            int BUFFER_SIZE = 1024 * 10;
            final BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);
            bitmap.compress(CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            fos.close();

            DressDesigner.refreshGallery(file.getPath());
            return true;

        } catch (Exception e) {

            Log.v(Info.TAG, e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {

            Toast.makeText(mContext, "Image saved to device.", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(mContext, "Could not save to device.", Toast.LENGTH_SHORT).show();
        }
    }
}