package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.views.CanvasView;

public class CanvasActivity extends AppCompatActivity {
    private static final String TAG = "CanvasActivity";
    private CanvasView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        surfaceView = (CanvasView)findViewById(R.id.surfaceView);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int i = event.getActionIndex();
                Log.v(TAG, "Touched " + event.getX(i) + ", " + event.getY(i) + " => " + i + ", " + event.getAction());
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        surfaceView.addPointGroup();
                        surfaceView.addPoint(new Point((int)event.getX(i), (int)event.getY(i)));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        surfaceView.addPoint(new Point((int)event.getX(i), (int)event.getY(i)));
                        break;
                }

                return true;
            }
        });
    }
}
