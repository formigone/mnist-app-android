package mnist.ai.formigone.com.mnistapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.telecom.Call;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsilveira on 9/21/17.
 */

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "CanvasView";
    private SurfaceHolder holder;
    private List<Float> points;
    private Paint paint;
    private Callback callback;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);

        points = new ArrayList<>();

        getHolder().addCallback(this);
        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int i = event.getActionIndex();
                float x = event.getX(i);
                float y = event.getY(i);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        addPoint(-1, -1);
                        if (callback != null) {
                            callback.onDrawn();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        addPoint(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        addPoint(x, y);
                        break;
                }

                return true;
            }
        });
    }

    public interface Callback {
        void onDrawn();
    }

    public void setOnDrawn(Callback callback) {
        this.callback = callback;
    }

    public void render() {
        Log.v(TAG, "onDraw");
        if (holder == null) {
            return;
        }
        Canvas canvas = holder.lockCanvas();
        int totalPoints = points.size();
        if (totalPoints > 0 && totalPoints % 2 == 0) {
            float xPrev = points.get(0);
            float yPrev = points.get(1);
            float x = 0;
            float y = 0;

            for (int i = 0; i < points.size(); i += 2) {
                x = points.get(i);
                y = points.get(i + 1);

                if (xPrev == -1 && yPrev == -1) {
                    xPrev = x;
                    yPrev = y;
                }

                if (x >= 0 && y >= 0) {
                    canvas.drawLine(xPrev, yPrev, x, y, paint);
                }

                xPrev = x;
                yPrev = y;
            }
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        this.holder = holder;
        render();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        this.holder = null;
    }

    public void restore(float[] points) {
        Log.d(TAG, "onRestoreInstanceState");
        if (points.length % 2 > 0) {
            Log.d(TAG, "List to restore is invalid (odd length)");
        }

        for (int i = 0; i < points.length; i++) {
            this.points.add(points[i]);
        }
        render();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    public void addPoint(float x, float y) {
        Log.v(TAG, "Adding point <" + x + ", " + y + ">");

        points.add(x);
        points.add(y);
        render();
    }

    public float[] getPoints() {
        float fpoints[] = new float[points.size()];
        int i = 0;
        for (Float point: points) {
            fpoints[i++] = point;
        }

        return fpoints;
    }
}
