package customer;

import builder.RequestBuilder;
import order.Order;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Mike on 26.12.2017.
 */
public class CustomerOrderShowerController
{
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    OrderShowerController orderShowerController;
    private RequestBuilder requestBuilder = new RequestBuilder();

    public CustomerOrderShowerController(OrderShowerController orderShowerController)
    {
        this.orderShowerController = orderShowerController;
        this.writer = orderShowerController.getWriter();
        this.reader = orderShowerController.getReader();
    }

    public Order onShowButtonClick(Integer ID) throws IOException, ClassNotFoundException
    {
        writer.writeObject(requestBuilder.getOrderByID());
        writer.flush();
        writer.writeInt(ID);
        writer.flush();

        Order order = (Order) reader.readObject();
        return order;
    }

    public void onValidateButtonClick(Integer ID) throws IOException
    {
        writer.writeObject(requestBuilder.deleteOrderByID());
        writer.flush();
        writer.writeInt(ID);
        writer.flush();
        orderShowerController.getMainPageShower().go();
    }
}
