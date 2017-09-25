package mnist.ai.formigone.com.mnistapp.views;

import android.content.Context;
import android.graphics.Bitmap;
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

public class CanvasView extends View {
    private static final String TAG = "CanvasView";
    private List<Float> points;
    private Paint paint;
    private Canvas canvas;
    private Callback callback;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        points = new ArrayList<>();

        View v = this;

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int i = event.getActionIndex();
                float x = event.getX(i);
                float y = event.getY(i);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        addPoint(-1, -1);
                        if (callback != null && canvas != null) {
                            Bitmap b = Bitmap.createBitmap( canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas c = new Canvas(b);
                            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                            v.draw(c);
                            callback.onDrawn(b);
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
        void onDrawn(Bitmap bitmap);
    }

    private Bitmap toBitmap(int width, int height) {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    public void setOnDrawn(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = render(canvas);
    }

    private Canvas render(Canvas canvas) {
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

        return canvas;
    }

    public void restore(float[] points) {
        if (points.length % 2 > 0) {
            Log.d(TAG, "List to restore is invalid (odd length)");
            return;
        }

        for (int i = 0; i < points.length; i++) {
            this.points.add(points[i]);
        }
        postInvalidate();
    }

    public void addPoint(float x, float y) {
        points.add(x);
        points.add(y);
        postInvalidate();
    }

    public float[] getPoints() {
        float points[] = new float[this.points.size()];
        int i = 0;
        for (Float point : points) {
            points[i++] = point;
        }

        return points;
    }
}
