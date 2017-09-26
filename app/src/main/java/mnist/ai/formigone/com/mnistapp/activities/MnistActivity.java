package mnist.ai.formigone.com.mnistapp.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.views.CanvasView;

public class MnistActivity extends AppCompatActivity {
    private CanvasView canvas;
    private TextView predictionContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mnist);

        canvas = (CanvasView) findViewById(R.id.canvas);
        canvas.setOnDrawn(new CanvasView.Callback() {
            @Override
            public void onDrawn(Bitmap bitmap) {
                predictionContainer.setText("8");
            }
        });

        predictionContainer = (TextView)findViewById(R.id.prediction);
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
}
