package mnist.ai.formigone.com.mnistapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.classifiers.MnistClassifier;
import mnist.ai.formigone.com.mnistapp.repositories.DigitRepository;

public class ClassifyReceiverActivity extends AppCompatActivity {
    private static final String TAG = "ClassifyActivity";
    private TextView label;
    private ProgressBar spinner;
    private FirebaseAnalytics tracker;
    private RequestQueue queue;
    private MnistClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_receiver);

        label = (TextView) findViewById(R.id.label);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        queue = Volley.newRequestQueue(this);

        classifier = new MnistClassifier(getAssets(), MnistClassifier.MODEL_FILE);
        tracker = FirebaseAnalytics.getInstance(this);
        tracker.setAnalyticsCollectionEnabled(true);

        spinner.setVisibility(View.VISIBLE);
        label.setText("Thinking...");

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        boolean ok = false;

        Log.v(TAG, "Received intent action: " + action);
        Log.v(TAG, "Received intent type: " + type);

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/mnist".equals(type)) {
                ok = handleTextShare(intent);
            }
        }

        if (!ok) {
            spinner.setVisibility(View.INVISIBLE);
            label.setText("This is awkward... I was given weird data that can't be processed.");
        }
    }

    boolean handleTextShare(Intent intent) {
        String jsonString = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.v(TAG, "Received intent text: " + jsonString);

        if (jsonString == null) {
            return false;
        }

        try {
            JSONArray payload = new JSONArray(jsonString);
            Log.d(TAG, "Payload: " + payload.length());

            spinner.setVisibility(View.VISIBLE);
            for (int i = 0; i < payload.length(); i++) {
                label.setText(genLabel(i + 1, payload.length()));

                JSONObject digit = payload.getJSONObject(i);
                JSONArray pixels = digit.getJSONArray("pixels");

                int prediction = classifier.classify(pixels).getPrediction();
                digit.remove("id");
                digit.put("prediction", prediction);
                digit.put("percentages", classifier.getPercentagesAsJSONArray());
                digit.put("actual", null);
                digit.put("model", MnistClassifier.MODEL_FILE);
            }

            Log.v(TAG, "Predictions: " + payload);
            label.setText("Saving...");
            classifier.close();

            JSONObject data = new JSONObject();
            data.put("digits", payload);

            JsonObjectRequest req = DigitRepository.save(data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, "RESP(200): " + response);
                            spinner.setVisibility(View.INVISIBLE);
                            label.setText("Done!");

                            navigateHome();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String body = "D'oh!";
                            if (error.networkResponse.data != null) {
                                try {
                                    body = new String(error.networkResponse.data, "UTF-8");
                                    JSONObject resp = new JSONObject(body);
                                    if (resp.has("error")) {
                                        body = resp.getString("error");
                                    }
                                } catch (UnsupportedEncodingException | JSONException e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            }
                            Log.v(TAG, "RESP(>299): " + error.toString());
                            spinner.setVisibility(View.INVISIBLE);
                            label.setText(body);

                            navigateHome();
                        }
                    });

            req.setRetryPolicy(new DefaultRetryPolicy(2500, 2, 1f));

            queue.add(req);
        } catch (JSONException e) {
            label.setText("Could not load list: " + e.getLocalizedMessage());
            Log.e(TAG, "ERROR: " + e.getMessage());
            return false;
        }

        return true;
    }

    private String genLabel(int current, int total) {
        return "Classifying " + current + " of " + total;
    }

    private void navigateHome() {
        Intent intent = new Intent(this, MnistActivity.class);
        startActivity(intent);
    }
}
