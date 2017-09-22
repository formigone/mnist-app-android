package mnist.ai.formigone.com.mnistapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public void onDrawn() {
        Log.v(TAG, "CanvasView has finished drawing");
    }
}
