package customer;

import builder.RequestBuilder;
import order.Order;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Mike on 21.12.2017.
 */
public class PaymentShowerController
{
    private OrderShowerController orderShowerController;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public PaymentShowerController(OrderShowerController orderShowerController)
    {
        this.orderShowerController = orderShowerController;
    }

    public OrderShowerController getOrderShowerController()
    {
        return orderShowerController;
    }

    public void setReader(ObjectInputStream reader)
    {
        this.reader = reader;
    }

    public void setWriter(ObjectOutputStream writer)
    {
        this.writer = writer;
    }

    public int addNewOrder(Order order) throws IOException
    {
        int currentID = new Random().nextInt(Integer.MAX_VALUE);
        System.out.println(currentID);
        order.setId(currentID);
        RequestBuilder requestBuilder = new RequestBuilder();
        writer.writeObject(requestBuilder.addNewOrder());
        writer.flush();
        writer.writeObject(order);
        writer.flush();

//        writer.writeObject(requestBuilder.updateCurrentAssortment());
//        writer.flush();
//        writer.writeObject(order);
//        writer.flush();

        orderShowerController.addNewOrder(currentID, order);
        return currentID;
    }

}
