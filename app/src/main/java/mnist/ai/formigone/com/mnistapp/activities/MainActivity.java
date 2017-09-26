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
