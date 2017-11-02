package mnist.ai.formigone.com.mnistapp.repositories;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import mnist.ai.formigone.com.mnistapp.BuildConfig;

/**
 * Created by rsilveira on 10/31/17.
 */

public class DigitRepository {
    public static final String API_BASE = "http://api.mnist.rodrigo-silveira.com/";
    public static final String API_BASE_DEBUG = "http://10.0.2.2:8088/";

    public static final String API_METHOD_SAVE = "v1";

    public static JsonObjectRequest save(JSONObject data, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String api = API_BASE;

        if (BuildConfig.DEBUG) {
            api = API_BASE_DEBUG;
        }

        return new JsonObjectRequest(Request.Method.POST, api + API_METHOD_SAVE, data, listener, errorListener);
    }
}
