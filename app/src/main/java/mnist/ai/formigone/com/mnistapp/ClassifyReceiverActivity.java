package mnist.ai.formigone.com.mnistapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ClassifyReceiverActivity extends AppCompatActivity {
    private static final String TAG = "ClassifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_receiver);

        TextView label = (TextView)findViewById(R.id.pixels);

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
        String data = appLinkData.getQueryParameter("data");
        try {
            JSONArray pixels = new JSONArray(data);
            label.setText("Loaded list with " + pixels.length() + " items.");
            Log.d(TAG, "Pixels: " + pixels.length());
        } catch (JSONException e) {
            label.setText("Could not load list: " + e.getLocalizedMessage());
            Log.e(TAG, "ERROR: " + e.getMessage());
        }
    }
}
