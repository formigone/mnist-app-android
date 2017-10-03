package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Bitmap;
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
                Random random = new Random();
                int prediction = random.nextInt(10);
                predictionContainer.setText(Integer.toString(prediction));
            }
        });

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

    private class MnistPost extends AsyncTask<Bitmap, Integer, List<Integer>> {
        @Override
        protected void onPreExecute() {
            Log.v(TAG, "Pre execution of async task");
        }

        @Override
        protected List<Integer> doInBackground(Bitmap... params) {
            Bitmap bitmap = params[0];
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 28, 28, true);

            Log.v(TAG, "in background... (" + resized.getWidth() + ", " + resized.getHeight() + ")");
            int min = Integer.MIN_VALUE;
            int max = Integer.MAX_VALUE;

            List<Integer> pixels = new ArrayList<Integer>();
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

            Log.v(TAG, "MIN/MAX: " + min + ", " + max);
            for (int y = 0; y < resized.getHeight(); y++) {
                for (int x = 0; x < resized.getWidth(); x++) {
                    int val = resized.getPixel(x, y);
                    pixels.add(1 - (val - min) / (max - min));
                }
            }
            Log.v(TAG, "Normalized");

            return pixels;
        }

        @Override
        protected void onPostExecute(List<Integer> pixels) {
            Log.v(TAG, "Got pixels: " + pixels);
            Toast.makeText(getBaseContext(), "Got pixels", Toast.LENGTH_SHORT).show();
            JSONObject payload;

            try {
                payload = new JSONObject();
                payload.put("pixels", new JSONArray(pixels));

                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8088/api.php", payload,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.v(TAG, "RESP(200): " + response);
                                Toast.makeText(getBaseContext(), "Image saved", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.v(TAG, "RESP(~200): " + error.getMessage());
                                Toast.makeText(getBaseContext(), "Error saving image: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                queue.add(req);
            } catch (JSONException e) {
                Toast.makeText(getBaseContext(), "Error preparing POST request", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
