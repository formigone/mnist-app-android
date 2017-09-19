package mnist.ai.formigone.com.mnistapp.repositories;

/**
 * Created by rsilveira on 9/19/17.
 */

public interface OrderCalculatorRepository {
    public int changeQuantity(int quantity);
    public int getQuantity();
    public double getPrice();
}
