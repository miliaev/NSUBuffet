package customer;

/**
 * Created by Mike on 21.12.2017.
 */
public class MainPageController
{
    private static final int MAKE_ORDER = 1;
    private static final int VALIDATE_ORDER = 2;

    private OrderShowerController orderShowerController;

    public MainPageController(OrderShowerController orderShowerController)
    {
        this.orderShowerController = orderShowerController;
    }

    public void onButtonClick(int buttonNumber)
    {
        switch (buttonNumber)
        {
            case MAKE_ORDER:
            {
                new OrderShower(orderShowerController);
                break;
            }
            case VALIDATE_ORDER:
            {
                CustomerOrderShowerController customerOrderShowerController = new CustomerOrderShowerController(orderShowerController);
                new CustomerOrderShower(customerOrderShowerController);
                break;
            }

        }
    }
}
