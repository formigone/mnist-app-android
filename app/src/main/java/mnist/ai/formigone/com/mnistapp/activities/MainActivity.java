package mnist.ai.formigone.com.mnistapp.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.adapters.SimpleAdapter;
import mnist.ai.formigone.com.mnistapp.models.SimpleItem;

public class MainActivity extends AppCompatActivity {

    private ArrayList<SimpleItem> activityList = new ArrayList<SimpleItem>();
    private Map<String, Class> categoryActivityMap = new HashMap<String, Class>();

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.main_list);

//        activityList.add(new SimpleItem("Order Calculator", "OrderCalculator"));
//        activityList.add(new SimpleItem("Canvas", "Canvas"));
        activityList.add(new SimpleItem("MNIST App", "MNIST"));

        categoryActivityMap.put("OrderCalculator", OrderCalculatorActivity.class);
        categoryActivityMap.put("Canvas", CanvasActivity.class);
        categoryActivityMap.put("MNIST", MnistActivity.class);

        SimpleAdapter listAdapter = new SimpleAdapter(this, activityList, listView);

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(listClickHandler);
//
//
//        TensorFlowInferenceInterface tf = new TensorFlowInferenceInterface(getAssets(), "mnist-20171007.pb");
//        float[] input = new float[]{
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.4784314036369324f, 0.6941176652908325f, 0.9960784912109375f, 0.5529412031173706f, 0.09803922474384308f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.07058823853731155f, 0.4784314036369324f, 0.9450981020927429f, 0.9450981020927429f, 0.9960784912109375f,
//                0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9568628072738647f, 0.8352941870689392f, 0.13333334028720856f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.6274510025978088f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9960784912109375f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f,
//                0.9921569228172302f, 0.9921569228172302f, 0.3137255012989044f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.6823529601097107f, 0.9921569228172302f,
//                0.9921569228172302f, 0.9921569228172302f, 0.9960784912109375f, 0.4745098352432251f, 0.15294118225574493f, 0.8627451658248901f, 0.9921569228172302f, 0.9921569228172302f, 0.3137255012989044f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5411764979362488f, 0.9921569228172302f, 0.9803922176361084f, 0.729411780834198f, 0.20784315466880798f,
//                0.011764707043766975f, 0.09803922474384308f, 0.8823530077934265f, 0.9921569228172302f, 0.9921569228172302f, 0.3137255012989044f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.04313725605607033f, 0.25882354378700256f, 0.24705883860588074f, 0.0f, 0.0f, 0.09019608050584793f, 0.501960813999176f, 0.9921569228172302f, 0.9921569228172302f,
//                0.9490196704864502f, 0.24705883860588074f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.019607843831181526f, 0.16078431904315948f,
//                0.16078431904315948f, 0.4117647409439087f, 0.8117647767066956f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.4235294461250305f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.10588236153125763f, 0.9921569228172302f, 0.9921569228172302f, 0.9960784912109375f, 0.9921569228172302f, 0.9921569228172302f,
//                0.9921569228172302f, 0.9921569228172302f, 0.6117647290229797f, 0.03529411926865578f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.10196079313755035f, 0.9411765336990356f, 0.9686275124549866f, 0.9960784912109375f, 0.9647059440612793f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f,
//                0.6235294342041016f, 0.019607843831181526f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.250980406999588f, 0.4745098352432251f,
//                0.22352942824363708f, 0.4705882668495178f, 0.9137255549430847f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.364705890417099f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.16862745583057404f, 0.9019608497619629f, 0.9960784912109375f, 1.0f, 0.8941177129745483f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.29019609093666077f, 0.9921569228172302f, 0.9921569228172302f, 0.8862745761871338f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2666666805744171f, 0.9921569228172302f, 0.9921569228172302f, 0.8862745761871338f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2666666805744171f, 0.9921569228172302f, 0.9921569228172302f,
//                0.8862745761871338f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.2392157018184662f, 0.2666666805744171f, 0.19607844948768616f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.6784313917160034f, 0.9921569228172302f, 0.9921569228172302f, 0.8862745761871338f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3137255012989044f, 0.9686275124549866f,
//                0.9921569228172302f, 0.9254902601242065f, 0.30588236451148987f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3490196168422699f, 0.9411765336990356f, 0.9921569228172302f, 0.9921569228172302f,
//                0.501960813999176f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.4235294461250305f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9254902601242065f,
//                0.6823529601097107f, 0.3529411852359772f, 0.16078431904315948f, 0.16078431904315948f, 0.16078431904315948f, 0.38431376218795776f, 0.6823529601097107f, 0.9450981020927429f, 0.9921569228172302f,
//                0.9921569228172302f, 0.8000000715255737f, 0.11372549831867218f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.1568627506494522f, 0.5568627715110779f, 0.9215686917304993f,
//                0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 1.0f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f,
//                0.9921569228172302f, 0.803921639919281f, 0.11372549831867218f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.13333334028720856f, 0.5529412031173706f,
//                0.9803922176361084f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 0.9921569228172302f, 1.0f, 0.9921569228172302f, 0.9921569228172302f, 0.9490196704864502f, 0.5215686559677124f,
//                0.1098039299249649f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.3490196168422699f, 0.5490196347236633f, 0.9921569228172302f, 0.9921569228172302f,
//                0.9921569228172302f, 1.0f, 0.7686275243759155f, 0.4705882668495178f, 0.07450980693101883f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//                0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
//        };
//        tf.feed("Placeholder", input, 1, 28 * 28);
//        tf.feed("dropout/Placeholder", new float[]{1f}, 1);
//        tf.run(new String[]{"fc2/add"});
//
//        float[] out = new float[10];
//        tf.fetch("fc2/add", out);
//        Log.v(TAG, "OUT: " + out);
    }

    private OnItemClickListener listClickHandler = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SimpleItem target = activityList.get(position);
            Log.v(TAG, "Item clicked: <" + target.getLabel() + "> position (" + position + ")");

            String category = target.getCategory();
            if (categoryActivityMap.containsKey(category)) {
                Intent intent = new Intent(MainActivity.this, categoryActivityMap.get(category));
                startActivity(intent);
            } else {
                Snackbar.make(view, R.string.msg_invalid_action, Snackbar.LENGTH_SHORT).show();
            }
        }
    };
}
