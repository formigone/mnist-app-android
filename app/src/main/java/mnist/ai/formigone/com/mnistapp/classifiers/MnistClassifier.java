package mnist.ai.formigone.com.mnistapp.classifiers;

import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import mnist.ai.formigone.com.mnistapp.activities.MnistActivity;

/**
 * Created by rsilveira on 10/9/17.
 */

public class MnistClassifier {
    private static final String TAG = "MnistClassifer";
    public static final String MODEL_FILE = "mnist-20171007.pb";

    private TensorFlowInferenceInterface graphDef;
    private float[] percentages;
    private float[] rawPercentages;
    private int prediction;

    public MnistClassifier(AssetManager assets, String graphDef) {
        this.graphDef = new TensorFlowInferenceInterface(assets, graphDef);
        percentages = new float[10];
        rawPercentages = new float[10];
    }

    public MnistClassifier classify(float[] pixels) {
        graphDef.feed("Placeholder", pixels, 1, pixels.length);
        graphDef.feed("dropout/Placeholder", new float[]{1f}, 1);
        graphDef.run(new String[]{"fc2/add"});

        graphDef.fetch("fc2/add", rawPercentages);
        percentages = scale(rawPercentages);

        return this;
    }

    public MnistClassifier classify(JSONArray pixels) {
        float[] pix = toFloatArray(pixels);
        return classify(pix);
    }

    public void close() {
        graphDef.close();
    }

    private float[] toFloatArray(JSONArray list) {
        float[] data = new float[list.length()];
        for (int i = 0; i < list.length(); i++) {
            try {
                data[i] = Float.parseFloat(list.getString(i));
            } catch (JSONException e) {
                Log.d(TAG, "Could not convert JSONArray to float[]");
            }
        }

        return data;
    }

    public float[] getPercentages(boolean raw) {
        return raw ? rawPercentages : percentages;
    }

    public int getPrediction() {
        prediction = argMax(percentages);
        return prediction;
    }

    public float getPredictionPercent() {
        int prediction = argMax(percentages);
        return percentages[prediction];
    }

    private int argMax(float[] values) {
        float max = Float.MIN_VALUE;
        int maxIndex = 0;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    private float[] scale(float[] values) {
        float[] scaled = new float[values.length];
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }

            if (values[i] < min) {
                min = values[i];
            }
        }

        float diff = max - min;
        float oneNth = 1 / values.length;

        for (int i = 0; i < values.length; i++) {
            if (diff == 0) {
                scaled[i] = oneNth;
            } else {
                scaled[i] = (values[i] - min) / diff;
            }
        }

        return scaled;
    }

    public JSONArray getPercentagesAsJSONArray() {
        JSONArray array = new JSONArray();
        for (int i = 0; i < rawPercentages.length; i++) {
            try {
                array.put(i, rawPercentages[i]);
            } catch (JSONException e) {
                Log.v(TAG, "Could not serialize percentages");
            }
        }

        return array;
    }
}
