package mnist.ai.formigone.com.mnistapp.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.models.SimpleItem;

/**
 * Created by rsilveira on 9/18/17.
 */

public class SimpleAdapter extends ArrayAdapter<SimpleItem> {
    private static final String TAG = "SimpleAdapter";

    private ArrayList<SimpleItem> items;
    private final Activity context;
    private final ListView listView;

    public SimpleAdapter(Activity context, ArrayList<SimpleItem> items, ListView listView) {
        super(context, R.layout.simple_list_item, items);

        this.context = context;
        this.items = items;
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView)super.getView(position, convertView, parent);
        SimpleItem item = (SimpleItem)listView.getItemAtPosition(position);
        if (item != null) {
            Log.v(TAG, "Rendering item: " + item);
            view.setText(item.getLabel());
            view.setEnabled(item.getActive());
            view.setClickable(!item.getActive());
        }

        return view;
    }
}
