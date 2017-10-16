package mnist.ai.formigone.com.mnistapp.classifiers;

import android.content.res.AssetManager;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import mnist.ai.formigone.com.mnistapp.activities.MnistActivity;

/**
 * Created by rsilveira on 10/9/17.
 */

public class MnistClassifier {
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
}
