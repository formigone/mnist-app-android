package mnist.ai.formigone.com.mnistapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import mnist.ai.formigone.com.mnistapp.presenters.OrderCalculatorPresenter;
import mnist.ai.formigone.com.mnistapp.repositories.OrderCalculatorRepository;
import mnist.ai.formigone.com.mnistapp.views.OrderCalculatorView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderCalculatorPresenterTest {
    @Mock
    OrderCalculatorRepository mockRepository;

    @Mock
    OrderCalculatorView mockView;

    private OrderCalculatorPresenter presenter;
    private int totalRenders;

    @Before
    public void setUp() {
        presenter = new OrderCalculatorPresenter(mockView, mockRepository);
        totalRenders = 0;
    }

    @Test
    public void changingQuantityCausesRerender() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                totalRenders += 1;
                return null;
            }
        }).when(mockView).displayPrice(Mockito.anyDouble());

        Assert.assertEquals(0, totalRenders);
        presenter.changeQuantity(0);
        Assert.assertTrue(totalRenders > 0);
    }
}