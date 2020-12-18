package com.example.fyp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class customize extends AppCompatActivity implements ChoicesRVA.ChoiceInteractionListener, View.OnClickListener, View.OnTouchListener, Info {

    private final String mDisplayName = "abc";
    public float density;
    ConstraintLayout touchField;
    boolean first = true;
    Bitmap merged;
    private int resourceId;
    //private ImageView ivPreview;
    private RecyclerView rvChoices;
    private ChoicesRVA choicesRVA;
    //   private SimpleDraweeView sdvChoice;
    private List<Choice> choiceList;
    private float lastXImage, lastYImage;
    private float actualXImage, actualYImage;
    private float dXImageLoc;
    private float dYImageLoc;
    private SeekBar sbImageSize;
    private int height;
    private FrameLayout mFrameLayoutImageContainer;
    private int width;
    private long startClickTime;
    private float dXILoc;
    private float dYILoc;
    private float dXHSI;
    private ImageButton mImageButtonImageHorizontalSize;
    private float lastWidthImage, lastHeightImage;
    private SimpleDraweeView mSimpleDraweeViewAddPhoto;
    private ImageButton mImageButtonImageVerticalSize;
    private float dYVSI;
    private ImageButton mImageButtonCommitImage;
    private ImageView mImageViewMemeViewer;
    private List<Bitmap> mListBitmapHistory;
    private Bitmap mClearImageBitmap;
    private int userOn;

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        Objects.requireNonNull(getSupportActionBar()).hide();

        initMembers();
        initViews();
        checkIntent();
        populateChoiceList();
        mImageButtonImageHorizontalSize.setOnTouchListener(this);
        mImageButtonImageVerticalSize.setOnTouchListener(this);
        setScales();

        mImageButtonCommitImage.setOnClickListener(this);
        density = getResources().getDisplayMetrics().density;
        mListBitmapHistory = new ArrayList<>();
        mSimpleDraweeViewAddPhoto.setOnTouchListener(this);


        int halfWidth = (mImageViewMemeViewer.getWidth() / 2) - (mFrameLayoutImageContainer.getWidth() / 2);
        int halfHeight = (mImageViewMemeViewer.getHeight() / 2) - (mFrameLayoutImageContainer.getHeight() / 2);

        mFrameLayoutImageContainer.setY(halfHeight);
        mFrameLayoutImageContainer.setX(halfWidth);

        actualXImage = halfWidth + mSimpleDraweeViewAddPhoto.getX();
        actualYImage = halfHeight + mSimpleDraweeViewAddPhoto.getY();

        lastXImage = mFrameLayoutImageContainer.getX();
        lastYImage = mFrameLayoutImageContainer.getY();


    }

    private void populateChoiceList() {
        choiceList.clear();
        choiceList.add(new Choice("belt1", R.drawable.belt1));
        choiceList.add(new Choice("belt2", R.drawable.belt2));
        choiceList.add(new Choice("belt3", R.drawable.belt3));
        choiceList.add(new Choice("belt4", R.drawable.belt4));
        choiceList.add(new Choice("button1", R.drawable.button1));
        choiceList.add(new Choice("button2", R.drawable.button2));
        choiceList.add(new Choice("button3", R.drawable.button3));
        choiceList.add(new Choice("button4", R.drawable.button4));
        choiceList.add(new Choice("button5", R.drawable.button5));
        choiceList.add(new Choice("pocket1", R.drawable.pocket1));
        choiceList.add(new Choice("pocket2", R.drawable.pocket2));
        choiceList.add(new Choice("pocket3", R.drawable.pocket3));
        choiceList.add(new Choice("pocket4", R.drawable.pocket4));
        choiceList.add(new Choice("sleeve1l", R.drawable.sleeve1l_));
        choiceList.add(new Choice("sleeve1r", R.drawable.sleeve1r_));
        choiceList.add(new Choice("sleeve2l", R.drawable.sleeve2l_));
        choiceList.add(new Choice("sleeve2r", R.drawable.sleeve2r_));
        choiceList.add(new Choice("sleeve3l", R.drawable.sleeve3l_));
        choiceList.add(new Choice("sleeve3r", R.drawable.sleeve3r_));
        choiceList.add(new Choice("sleeve4l", R.drawable.sleeve4l_));
        choiceList.add(new Choice("sleeve4r", R.drawable.sleeve4r_));
        choiceList.add(new Choice("sleeve5l", R.drawable.sleeve5l_));
        choiceList.add(new Choice("sleeve5r", R.drawable.sleeve5r_));
        choiceList.add(new Choice("sleeve6l", R.drawable.sleeve6l_));
        choiceList.add(new Choice("sleeve6r", R.drawable.sleeve6r_));
        choicesRVA.setChoiceList(choiceList);
    }

    private void initMembers() {
        this.choicesRVA = new ChoicesRVA(this);
        this.choiceList = new ArrayList<>();
        this.choicesRVA.setChoiceList(choiceList);
    }

    private void initViews() {
        mFrameLayoutImageContainer = findViewById(R.id.mFrameLayoutPhotoContainer);
        touchField = findViewById(R.id.touch_field);
        mImageViewMemeViewer = findViewById(R.id.iv_preview);
        mImageButtonCommitImage = findViewById(R.id.mImageButtonCommitImage);
        mImageButtonImageVerticalSize = findViewById(R.id.mImageButtonImageVerticalSize);
        mSimpleDraweeViewAddPhoto = findViewById(R.id.mSimpleDraweeViewAddPhoto);
        mImageButtonImageHorizontalSize = findViewById(R.id.mImageButtonImageHorizontalSize);

        //sdvChoice = findViewById(R.id.sdv_choice_image);
        rvChoices = findViewById(R.id.rv_choices);
        //ivPreview = findViewById(R.id.iv_preview);
        rvChoices.setAdapter(choicesRVA);
        getMetrics();
        touchField.setOnTouchListener(this);
    }

    private void getMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        height = metrics.heightPixels;
        width = metrics.widthPixels;
    }

    private void checkIntent() {
        resourceId = getIntent().getIntExtra("resourceId", 0);
        //ivPreview.setImageDrawable(getDrawable(resourceId));
        loadClearImage(drawableToBitmap(getDrawable(resourceId)));

    }

    @Override
    public void onChoiceClicked(Choice choice) {
        Log.i(TAG, "onChoiceClicked: " + choice.getName());
        mSimpleDraweeViewAddPhoto.setImageResource(choice.getResourceId());
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        if (view == touchField) {
            if (first) {
                view = mFrameLayoutImageContainer;
                first = false;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dXImageLoc = view.getX() - event.getRawX();
                    dYImageLoc = view.getY() - event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    mFrameLayoutImageContainer.setY(event.getRawY() + dYImageLoc - mSimpleDraweeViewAddPhoto.getTop() + lastYImage);
                    mFrameLayoutImageContainer.setX(event.getRawX() + dXImageLoc - mSimpleDraweeViewAddPhoto.getLeft() + lastXImage);

                    actualXImage = mSimpleDraweeViewAddPhoto.getX() + mFrameLayoutImageContainer.getX();
                    actualYImage = mSimpleDraweeViewAddPhoto.getY() + mFrameLayoutImageContainer.getY();
                    Log.i(TAG, "onTouch: " + actualYImage);
                    Log.i(TAG, "onTouch: " + actualXImage);

                    break;
                default:
                    lastXImage = mFrameLayoutImageContainer.getX();
                    lastYImage = mFrameLayoutImageContainer.getY();
                    return false;
            }

        }


        if (view == mImageButtonImageHorizontalSize) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    assert view != null;
                    dXHSI = view.getX() - event.getRawX();

                    lastWidthImage = mSimpleDraweeViewAddPhoto.getWidth();

                    break;

                case MotionEvent.ACTION_MOVE:

                    if (Math.round(event.getRawX() + dXHSI - mImageButtonImageHorizontalSize.getLeft() + lastWidthImage) < (50 * density))
                        break;

                    mSimpleDraweeViewAddPhoto.getLayoutParams().width = (Math.round(event.getRawX() + dXHSI - mImageButtonImageHorizontalSize.getLeft() + lastWidthImage));
                    mSimpleDraweeViewAddPhoto.requestLayout();

                    break;
                default:

                    lastWidthImage = mSimpleDraweeViewAddPhoto.getLayoutParams().width;
                    return false;
            }
        }
        if (view == mImageButtonImageVerticalSize) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dYVSI = view.getY() - event.getRawY();
                    lastHeightImage = mSimpleDraweeViewAddPhoto.getHeight();

                    break;
                case MotionEvent.ACTION_MOVE:

                    if ((Math.round(event.getRawY() + dYVSI - mImageButtonImageVerticalSize.getTop() + lastHeightImage)) < (50 * density))
                        break;

                    mSimpleDraweeViewAddPhoto.getLayoutParams().height = (Math.round(event.getRawY() + dYVSI - mImageButtonImageVerticalSize.getTop() + lastHeightImage));
                    mSimpleDraweeViewAddPhoto.requestLayout();

                    break;
                default:

                    lastHeightImage = mSimpleDraweeViewAddPhoto.getLayoutParams().height;
                    return false;
            }
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view == mImageButtonCommitImage) {

            ColorFilter tint = mSimpleDraweeViewAddPhoto.getBackground().getColorFilter();
            mSimpleDraweeViewAddPhoto.setBackgroundColor(Color.TRANSPARENT);
            mSimpleDraweeViewAddPhoto.setDrawingCacheEnabled(true);

            mSimpleDraweeViewAddPhoto.buildDrawingCache();

            Bitmap bitmap1 = mSimpleDraweeViewAddPhoto.getDrawingCache();

            //Matrix matrix = new Matrix();
            //matrix.setRotate((float) (mSeekBarImageRotation.getProgress() * 3.60), bitmap1.getWidth() / 2, bitmap1.getHeight() / 2);

            bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), new Matrix(), true);
            Bitmap bitmap = drawableToBitmap(mImageViewMemeViewer.getDrawable());

            mSimpleDraweeViewAddPhoto.setBackground(ContextCompat.getDrawable(this, R.drawable.container_border));
            mSimpleDraweeViewAddPhoto.getBackground().setColorFilter(tint);

            Bitmap[] listBmp = {bitmap, bitmap1};
            Bitmap mergedImg = overlay(
                    listBmp[0],
                    listBmp[1],
                    actualXImage,
                    actualYImage
            );

            merged = mergedImg;
            mListBitmapHistory.add(mergedImg);
            mImageViewMemeViewer.setImageBitmap(mergedImg);

            //mVibrator.vibrate(50);

        }

    }

    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2, float x, float y) {

        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        int bitmapCenterX = (int) (x + (bmp2.getWidth() / 2));
        int bitmapCenterY = (int) (y + (bmp2.getHeight() / 2));

        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.save();
        canvas.rotate(((float) 0.0), bitmapCenterX, bitmapCenterY);
        canvas.drawBitmap(bmp2, x, y, null);
        canvas.restore();
        return bmOverlay;

    }

    private void setScales() {

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        int dpValue = 30;

        mImageViewMemeViewer.getLayoutParams().width = mImageViewMemeViewer.getLayoutParams().width - (int) (density * dpValue);
        mImageViewMemeViewer.getLayoutParams().height = mImageViewMemeViewer.getLayoutParams().height - (int) (density * dpValue);
        mImageViewMemeViewer.requestLayout();

        this.height = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {

        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }

    }

    private void loadClearImage(Bitmap bitmap) {
        mClearImageBitmap = resize(bitmap, width, height);
        mImageViewMemeViewer.getLayoutParams().width = mClearImageBitmap.getWidth();
        mImageViewMemeViewer.getLayoutParams().height = mClearImageBitmap.getHeight();
        mImageViewMemeViewer.requestLayout();

        mImageViewMemeViewer.setImageBitmap(mClearImageBitmap);
        mListBitmapHistory = new ArrayList<>();
        mListBitmapHistory.add(mClearImageBitmap);
        merged = mClearImageBitmap;
        userOn = 0;

    }

    public void print(View view) {
        mImageButtonCommitImage.performClick();
    }

    public void save(View view) {
        showNameDialog();
    }

    private void showNameDialog() {

        final Dialog nameDialog = new Dialog(this);
        nameDialog.setContentView(R.layout.dialog_enter_name);
        nameDialog.setCancelable(true);
        nameDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.colorTransparent));
        nameDialog.getWindow().setLayout(MATCH_PARENT, WRAP_CONTENT);

        final EditText editTextName = nameDialog.findViewById(R.id.editTextFileName);
        Button buttonSave = nameDialog.findViewById(R.id.buttonSaveImage);
        editTextName.setText("" + (mDisplayName.replace(" ", "_")) + "_" + System.currentTimeMillis());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = editTextName.getText().toString();
                String path = ROOT + File.separator + "Memeify";

                File directory = new File(path);
                if (!directory.exists())
                    directory.mkdirs();

                File file = new File(path, name + ".jpg");

                try {

                    new AsyncTaskBitmapWriter(file, merged, customize.this)
                            .execute();

                    nameDialog.cancel();
                } catch (Exception e) {

                    Toast.makeText(customize.this, "Failed to save, Try again.", Toast.LENGTH_SHORT).show();
                    nameDialog.cancel();

                }
            }
        });

        nameDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                editTextName.setText("" + (mDisplayName.replace(" ", "_")) + "_" + System.currentTimeMillis());
            }
        });

        nameDialog.show();
    }

    public void undo(View view) {
        try {
            Log.i(TAG, "undo: " + mListBitmapHistory.size());
            mListBitmapHistory.remove(merged);
            Bitmap undoMerged = mListBitmapHistory.get(mListBitmapHistory.size() - 1);
            mImageViewMemeViewer.setImageBitmap(undoMerged);
            merged = undoMerged;
        } catch (Exception e) {
            checkIntent();
            e.printStackTrace();
        }

    }
}
