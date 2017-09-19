package mnist.ai.formigone.com.mnistapp.views;

/**
 * Created by rsilveira on 9/19/17.
 */

public interface OrderCalculatorView {
    public void displayQuantity(int quantity);
    public void displayPrice(double price);
    public void setMinusButtonClickable(boolean clickable);
}
