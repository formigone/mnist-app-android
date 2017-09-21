package mnist.ai.formigone.com.mnistapp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsilveira on 9/21/17.
 */

public class CanvasViewImpl extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "CanvasView";
    private static final Paint paint = new Paint();
    private SurfaceHolder holder;
    private boolean running;
    private List<List<Point>> pointGroup = new ArrayList<List<Point>>();
    protected Thread thread;

    @Override
    public void run() {
        if (running) {
            Log.v(TAG, "onDraw");
            Canvas canvas = holder.lockCanvas();
            if (pointGroup.size() > 0) {
                for (List<Point> points:pointGroup) {
                    if (points.size() > 0) {
                        Point lastPoint = points.get(0);
                        for (Point point : points) {
                            canvas.drawLine(lastPoint.x, lastPoint.y, point.x, point.y, paint);
                            lastPoint = point;
                        }
                    }
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void addPointGroup() {
        pointGroup.add(new ArrayList<Point>());
    }

    public void addPoint(Point point) {
        if (pointGroup.size() == 0) {
            addPointGroup();
        }
        int group = pointGroup.size() - 1;
        Log.v(TAG, "Adding point to group " + group);

        List<Point> points = pointGroup.get(group);
        points.add(point);
        Log.v(TAG, "Added point");
        run();
    }

    public CanvasViewImpl(Context context) {
        super(context);
        thread = new Thread(this);
        holder = getHolder();
        holder.addCallback(this);
        running = false;
        paint.setColor(Color.RED);
        paint.setStrokeWidth(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Log.v(TAG, "Constructor");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        running = true;
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed");
        running = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged");
    }
}
