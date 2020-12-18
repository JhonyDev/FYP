package com.example.fyp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PaintView extends View implements Info {
    public static int BRUSH_SIZE = 10;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;

    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();

    public PaintView(Context context) {
        super(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xffffff);
    }

    public void initialise(DisplayMetrics displayMetrics) {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(makeTransparent(mBitmap));
        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
        invalidate();
    }

    public static Bitmap makeTransparent(Bitmap bit) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {
            allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw: ");
        canvas.save();
        mCanvas.drawColor(backgroundColor); // WRONG
        for (Draw draw : paths) {
            mPaint.setColor(draw.color); // WRONG
            mPaint.setStrokeWidth(draw.strokeWidth);
            mPaint.setMaskFilter(null);
            canvas.drawPath(draw.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {

        mPath = new Path();

        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        paths.add(draw);

        mPath.reset();
        mPath.moveTo(x, y);

        mX = x;
        mY = y;

    }

    private void touchMove(float x, float y) {

        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {

            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            mX = x;
            mY = y;

        }

    }

    private void touchUp() {

        mPath.lineTo(mX, mY);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;

        }

        return true;

    }

    public void clear() {

        backgroundColor = DEFAULT_BG_COLOR;

        paths.clear();
        invalidate();

    }

    public void undo() {
        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nothing to Undo", Toast.LENGTH_LONG).show();
        }
    }

    public void redo() {
        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate(); // add
        } else {
            Toast.makeText(getContext(), "Nothing to Redo", Toast.LENGTH_LONG).show();
        }
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void setColor(int color) {
        currentColor = color;
    }

    @SuppressLint("WrongThread")
    public void saveImage(Activity context) {
        int count = 0;
        File sdDirectory = Environment.getExternalStorageDirectory();
        File subDirectory = new File(sdDirectory.toString() + "/Pictures/Paint");

        if (subDirectory.exists()) {
            File[] existing = subDirectory.listFiles();
            for (File file : existing) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    count++;
                }
            }
        } else {
            subDirectory.mkdir();
        }

        if (subDirectory.exists()) {
            count++;
            File image = new File(subDirectory, "/drawing_" + (count + 1) + ".png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(image);
                setDrawingCacheEnabled(true);
                buildDrawingCache();
                Bitmap bmp = getDrawingCache();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

                //To show saved image in gallery by scanning it after saving
                MediaScannerConnection.scanFile(context,
                        new String[]{image.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });

                fileOutputStream.flush();
                fileOutputStream.close();

                Toast.makeText(getContext(), "Image Saved", Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {

            } catch (IOException e) {


            }

        }
context.finish();
    }
}
