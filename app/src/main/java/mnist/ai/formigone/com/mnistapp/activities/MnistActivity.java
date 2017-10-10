package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.classifiers.MnistClassifier;
import mnist.ai.formigone.com.mnistapp.views.CanvasView;

public class MnistActivity extends AppCompatActivity {
    private static final String TAG = "MnistActivity";
    private CanvasView canvas;
    private TextView predictionContainer;
    private TextView predictionPercentContainer;
    private ImageButton btnWrong;
    private ImageButton btnCorrect;
//    private RequestQueue queue;

    private List<ImageView> bars;
    private FirebaseAnalytics tracker;
    private MnistClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnist);
//        queue = Volley.newRequestQueue(this);
        classifier = new MnistClassifier(getAssets(), "mnist-20171007.pb");
        tracker = FirebaseAnalytics.getInstance(this);
        tracker.setAnalyticsCollectionEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        canvas = (CanvasView) findViewById(R.id.canvas);
        canvas.setOnDrawn(new CanvasView.Callback() {
            @Override
            public void onDrawn(Bitmap bitmap) {
                new MnistPost().execute(bitmap);
                predictionContainer.setText("...");
            }
        });

        // response from model: {
        //      "predictions":[
        //          -6.6451120376587,
        //          1.7916091680527,
        //          -7.4739499092102,
        //          3.5438303947449,
        //          0.73703813552856,
        //          -2.1572058200836,
        //          -10.432391166687,
        //          0.94211292266846,
        //          1.2044558525085,
        //          8.8206148147583
        //      ],
        //      "prediction": 9,
        //      "classes": [0,1,2,3,4,5,6,7,8,9]
        // }

        predictionContainer = (TextView) findViewById(R.id.prediction);
        predictionPercentContainer = (TextView) findViewById(R.id.prediction_percent);

        bars = new ArrayList<ImageView>();
        bars.add((ImageView) findViewById(R.id.prediction_graph_0));
        bars.add((ImageView) findViewById(R.id.prediction_graph_1));
        bars.add((ImageView) findViewById(R.id.prediction_graph_2));
        bars.add((ImageView) findViewById(R.id.prediction_graph_3));
        bars.add((ImageView) findViewById(R.id.prediction_graph_4));
        bars.add((ImageView) findViewById(R.id.prediction_graph_5));
        bars.add((ImageView) findViewById(R.id.prediction_graph_6));
        bars.add((ImageView) findViewById(R.id.prediction_graph_7));
        bars.add((ImageView) findViewById(R.id.prediction_graph_8));
        bars.add((ImageView) findViewById(R.id.prediction_graph_9));

        btnWrong = (ImageButton)findViewById(R.id.btn_correct);
        btnCorrect = (ImageButton)findViewById(R.id.btn_wrong);

        btnWrong.setVisibility(View.GONE);
        btnCorrect.setVisibility(View.GONE);

        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCorrect.setAlpha(0.5f);
                btnWrong.setAlpha(1f);
            }
        });

        btnCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnWrong.setAlpha(0.5f);
                btnCorrect.setAlpha(1f);
            }
        });

        drawBars(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                canvas.reset();
                predictionContainer.setText("");
                predictionPercentContainer.setText("");
                findViewById(R.id.btn_correct).setVisibility(View.GONE);
                findViewById(R.id.btn_wrong).setVisibility(View.GONE);
                drawBars(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mnist, menu);
        return true;
    }

    /**
     *
     * @param percents If not set, or has less elements than bars, bar will be set to 0
     */
    private void drawBars(float[] percents) {
        int maxHeight = bars.get(0).getBottom() - predictionContainer.getBottom();
        if (maxHeight == 0) {
            maxHeight = 1000;
        }
        for (int i = 0; i < bars.size(); i++) {
            ImageView bar = bars.get(i);
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) bar.getLayoutParams();
            lp.topMargin = maxHeight;
            if (percents != null && percents.length > i) {
                lp.topMargin = maxHeight - (int)(maxHeight * percents[i]);
            }

            bar.setLayoutParams(lp);
            if (lp.topMargin < 100) {
                bar.setTranslationY(500f);
            } else {
                bar.setTranslationY(lp.topMargin * 2f);
            }
            bar.animate().translationY(0.f);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        canvas.setTranslationY(32);
        canvas.setAlpha(0.5f);
        canvas.animate().translationY(-32);
        canvas.animate().alpha(1f);
    }

    private class MnistPost extends AsyncTask<Bitmap, Integer, float[]> {
        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Pre execution of async task");
        }

        @Override
        protected float[] doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 27, 27, true);
            resized = Bitmap.createScaledBitmap(resized, 28, 28, true);

            int min = Integer.MIN_VALUE;
            int max = Integer.MAX_VALUE;

            float[] pixels = new float[resized.getWidth() * resized.getHeight()];
            for (int y = 0; y < resized.getHeight(); y++) {
                for (int x = 0; x < resized.getWidth(); x++) {
                    int val = resized.getPixel(x, y);
                    if (val > min) {
                        min = val;
                    }

                    if (val < max) {
                        max = val;
                    }
                }
            }

            int i = 0;
            int tmp = max;
            max = min;
            min = tmp;
            float range = max - min;
            float oneNth = 1 / resized.getWidth() * resized.getHeight();

            for (int y = 0; y < resized.getHeight(); y++) {
                for (int x = 0; x < resized.getWidth(); x++, i++) {
                    int val = resized.getPixel(x, y);
                    if (range == 0) {
                        pixels[i] = 1 - oneNth;
                    } else {
                        pixels[i] = 1 - (val - min) / range;
                    }
                }
            }

            return pixels;
        }

        @Override
        protected void onPostExecute(float[] pixels) {
            Log.v(TAG, "Got pixels");
            classifier.classify(pixels);
            predictionContainer.setText(Integer.toString(classifier.getPrediction()));
//            predictionPercentContainer.setText(String.format("%.2f", classifier.getPredictionPercent()) + "%");
            btnCorrect.setVisibility(View.VISIBLE);
            btnCorrect.setAlpha(1f);
            btnWrong.setVisibility(View.VISIBLE);
            btnWrong.setAlpha(1f);
            drawBars(classifier.getPercentages());
//            JSONObject payload;
//
//            try {
//                payload = new JSONObject();
//                payload.put("pixels", new JSONArray(pixels));
//
//                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8088/api.php", payload,
//                        new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.v(TAG, "RESP(200): " + response);
//                                try {
//                                    JSONObject resp = response.getJSONObject("response");
//                                    int prediction = resp.getInt("prediction");
//                                    predictionContainer.setText(Integer.toString(prediction));
//                                    JSONArray predictions = resp.getJSONArray("predictions");
//                                    double[] percents = new double[predictions.length()];
//
//                                    for (int i = 0; i < bars.size(); i++) {
//                                        percents[i] = predictions.getDouble(i);
//                                    }
//                                    percents = scale(percents);
//                                    drawBars(percents);
//                                } catch (JSONException error) {
//                                    Log.e(TAG, error.getMessage());
//                                    Toast.makeText(getBaseContext(), "Could not parse response: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                                    predictionContainer.setText("");
//                                    drawBars(null);
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.v(TAG, "RESP(~200): " + error.getMessage());
//                                Toast.makeText(getBaseContext(), "Error classifying digit: " + error.getMessage(), Toast.LENGTH_LONG).show();
//                                predictionContainer.setText("");
//                                drawBars(null);
//                            }
//                        });
//
//                queue.add(req);
//            } catch (JSONException e) {
//                Toast.makeText(getBaseContext(), "Error preparing POST request", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
