package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.views.CanvasView;

public class MnistActivity extends AppCompatActivity {
    private static final String TAG = "MnistActivity";
    private CanvasView canvas;
    private TextView predictionContainer;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnist);
        queue = Volley.newRequestQueue(this);

        canvas = (CanvasView) findViewById(R.id.canvas);
        canvas.setOnDrawn(new CanvasView.Callback() {
            @Override
            public void onDrawn(Bitmap bitmap) {
                Log.v(TAG, "Processing pixels");
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
        findViewById(R.id.btn_correct).setEnabled(false);
        findViewById(R.id.btn_wrong).setEnabled(false);
        findViewById(R.id.btn_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.reset();
                predictionContainer.setText("");
            }
        });
    }

    private class MnistPost extends AsyncTask<Bitmap, Integer, List<Double>> {
        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Pre execution of async task");
        }

        @Override
        protected List<Double> doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 27, 27, true);
            resized = Bitmap.createScaledBitmap(resized, 28, 28, true);

            Log.v(TAG, "in background... (" + resized.getWidth() + ", " + resized.getHeight() + ")");
            int min = Integer.MIN_VALUE;
            int max = Integer.MAX_VALUE;

            List<Double> pixels = new ArrayList<Double>();
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

            int tmp = max;
            max = min;
            min = tmp;
            double range = max - min;

            if (range == 0) {
                range = 0.01;
            }

            Log.v(TAG, "MIN/MAX: " + min + ", " + max);
            for (int y = 0; y < resized.getHeight(); y++) {
                for (int x = 0; x < resized.getWidth(); x++) {
                    int val = resized.getPixel(x, y);
                    pixels.add(1 - (val - min) / range);
                }
            }
            Log.v(TAG, "Normalized");

            return pixels;
        }

        @Override
        protected void onPostExecute(List<Double> pixels) {
            Log.v(TAG, "Got pixels: " + pixels);
            JSONObject payload;

            try {
                payload = new JSONObject();
                payload.put("pixels", new JSONArray(pixels));

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8088/api.php", payload,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v(TAG, "RESP(200): " + response);
                                try {
                                    JSONObject resp = response.getJSONObject("response");
                                    int predicton = resp.getInt("prediction");
                                    predictionContainer.setText(Integer.toString(predicton));
                                } catch (JSONException error) {
                                    Log.e(TAG, error.getMessage());
                                    Toast.makeText(getBaseContext(), "Could not parse response: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                    predictionContainer.setText("");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v(TAG, "RESP(~200): " + error.getMessage());
                                Toast.makeText(getBaseContext(), "Error classifying digit: " + error.getMessage(), Toast.LENGTH_LONG).show();
                                predictionContainer.setText("");
                            }
                        });

                queue.add(req);
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "Error preparing POST request", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
