package mnist.ai.formigone.com.mnistapp.presenters;

import mnist.ai.formigone.com.mnistapp.repositories.OrderCalculatorRepository;
import mnist.ai.formigone.com.mnistapp.views.OrderCalculatorView;

/**
 * Created by rsilveira on 9/19/17.
 */

public class OrderCalculatorPresenter {
    OrderCalculatorView view;
    OrderCalculatorRepository repository;

    public OrderCalculatorPresenter(OrderCalculatorView view, OrderCalculatorRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void render() {
        int quantity = repository.getQuantity();
        view.displayQuantity(quantity);
        view.displayPrice(repository.getPrice());
        view.setMinusButtonClickable(quantity > 0);
    }

    public void changeQuantity(int quantity) {
        repository.changeQuantity(quantity);
        render();
    }
}
