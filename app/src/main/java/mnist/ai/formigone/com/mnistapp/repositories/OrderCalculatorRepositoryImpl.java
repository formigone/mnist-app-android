package mnist.ai.formigone.com.mnistapp.repositories;

/**
 * Created by rsilveira on 9/19/17.
 */

public class OrderCalculatorRepositoryImpl implements OrderCalculatorRepository {
    private int quantity;
    private double price;

    public OrderCalculatorRepositoryImpl(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public int changeQuantity(int quantity) {
        if (this.quantity + quantity >= 0) {
            this.quantity += quantity;
        }

        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return quantity * price;
    }
}
