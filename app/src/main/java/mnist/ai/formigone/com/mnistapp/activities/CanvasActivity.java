package mnist.ai.formigone.com.mnistapp.activities;

import android.Manifest;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.views.CanvasView;

public class CanvasActivity extends AppCompatActivity implements CanvasView.Callback {
    private static final String TAG = "CanvasActivity";
    public final static String STATE_KEY_POINTS = "points";
    private CanvasView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        canvas = (CanvasView) findViewById(R.id.canvas);
        canvas.setOnDrawn(this);

        int _ = 0;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, _);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloatArray(STATE_KEY_POINTS, canvas.getPoints());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            float points[] = savedInstanceState.getFloatArray(STATE_KEY_POINTS);
            if (points != null) {
                canvas.restore(points);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDrawn(Bitmap bitmap) {
        Log.v(TAG, "CanvasView has finished drawing");
//        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 28, 28, false);

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        resized.compress(Bitmap.CompressFormat.PNG, 0, stream);
//        byte[] pixels = stream.toByteArray();
//        byte[] pixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();

//        int pixels[] = new int[28 * 28];
//        resized.getPixels(pixels, 0, 28, 0, 0, 28, 28);

        FileOutputStream out = null;
        try {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, "mnist-" + Calendar.getInstance().getTime().getTime() + ".png");
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
//            resized.compress(Bitmap.CompressFormat.PNG, 0, out);
            Log.v(TAG, "File: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.v(TAG, "Got pixels");
//        profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));

    }
}
