package mnist.ai.formigone.com.mnistapp;

import org.junit.Assert;
import org.junit.Test;

import mnist.ai.formigone.com.mnistapp.presenters.OrderCalculatorPresenter;
import mnist.ai.formigone.com.mnistapp.repositories.OrderCalculatorRepository;
import mnist.ai.formigone.com.mnistapp.views.OrderCalculatorView;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OrderCalculatorPresenterTest {
    @Test
    public void changingQuantityCausesRerender() throws Exception {
        OrderCalculatorRepository mockRepository = new MockOrderCalculatorRepository();
        OrderCalculatorView mockView = new MockOrderCalculatorView();
        OrderCalculatorPresenter presenter = new OrderCalculatorPresenter(mockView, mockRepository);

        Assert.assertEquals(0, ((MockOrderCalculatorView)mockView).renderCount);
        presenter.changeQuantity(0);
        Assert.assertTrue(((MockOrderCalculatorView)mockView).renderCount > 0);
    }

    private class MockOrderCalculatorRepository implements OrderCalculatorRepository {
        @Override
        public int changeQuantity(int quantity) {
            return 0;
        }

        @Override
        public int getQuantity() {
            return 0;
        }

        @Override
        public double getPrice() {
            return 0;
        }
    }

    private class MockOrderCalculatorView implements OrderCalculatorView {
        int renderCount = 0;

        @Override
        public void displayQuantity(int quantity) {
            renderCount += 1;
        }

        @Override
        public void displayPrice(double price) {
            renderCount += 1;
        }

        @Override
        public void setMinusButtonClickable(boolean clickable) {
            renderCount += 1;
        }
    }
}