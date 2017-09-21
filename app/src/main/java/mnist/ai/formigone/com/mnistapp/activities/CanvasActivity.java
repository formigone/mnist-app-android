package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.views.CanvasViewImpl;

public class CanvasActivity extends AppCompatActivity {
    private static final String TAG = "CanvasActivity";
    private CanvasViewImpl surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        surfaceView = new CanvasViewImpl(this);
        LinearLayout canvasContainer = (LinearLayout)findViewById(R.id.canvasContainer);

        canvasContainer.addView(surfaceView);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int i = event.getActionIndex();
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
