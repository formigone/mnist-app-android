package mnist.ai.formigone.com.mnistapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import mnist.ai.formigone.com.mnistapp.R;
import mnist.ai.formigone.com.mnistapp.presenters.OrderCalculatorPresenter;
import mnist.ai.formigone.com.mnistapp.repositories.OrderCalculatorRepositoryImpl;
import mnist.ai.formigone.com.mnistapp.repositories.OrderCalculatorRepository;
import mnist.ai.formigone.com.mnistapp.views.OrderCalculatorView;

public class OrderCalculatorActivity extends AppCompatActivity implements OrderCalculatorView, View.OnClickListener {

    private OrderCalculatorPresenter presenter;
    private TextView quantityView;
    private TextView priceView;
    private Button minusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_calculator);
        OrderCalculatorRepository repository = new OrderCalculatorRepositoryImpl(0, 2.5);
        presenter = new OrderCalculatorPresenter(this, repository);

        minusButton = (Button)findViewById(R.id.btn_minus);
        quantityView = (TextView)findViewById(R.id.quantity);
        priceView = (TextView)findViewById(R.id.price);

        minusButton.setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);

        presenter.render();
    }

    @Override
    public void displayQuantity(int quantity) {
        quantityView.setText(String.valueOf(quantity));
    }

    @Override
    public void displayPrice(double price) {
        priceView.setText(String.valueOf(price));
    }

    @Override
    public void setMinusButtonClickable(boolean clickable) {
        minusButton.setEnabled(clickable);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_minus) {
            presenter.changeQuantity(-1);
        } else if (view.getId() == R.id.btn_plus) {
            presenter.changeQuantity(1);
        }
    }
}
