package customer;

import builder.RequestBuilder;
import order.Order;
import order.ProductInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mike on 21.12.2017.
 */
public class OrderShowerController
{
    private static final int CLIENT = 0;

    private Socket socket;
    private MainPageShower mainPageShower;
    private boolean firstConnect = true;
    ObjectOutputStream writer;
    ObjectInputStream reader;
    private HashMap<Integer, Order> currentOrders = new HashMap<>();

    public void setMainPageShower(MainPageShower mainPageShower) throws IOException
    {
        this.mainPageShower = mainPageShower;
        if(firstConnect)
        {
            writer.writeInt(CLIENT);
            writer.flush();
            firstConnect = !firstConnect;
        }
    }

    public ObjectOutputStream getWriter()
    {
        return writer;
    }

    public ObjectInputStream getReader()
    {
        return reader;
    }

    public MainPageShower getMainPageShower()
    {
        return mainPageShower;
    }

    public OrderShowerController(Socket socket) throws IOException
    {
        this.socket = socket;
        writer = new ObjectOutputStream(socket.getOutputStream());
        reader = new ObjectInputStream(socket.getInputStream());
    }

    public HashMap<String, ProductInfo> getBuffetAssortment(int buffetID) throws IOException, ClassNotFoundException
    {

        writer.writeObject(new RequestBuilder().getCurrentItems());
        writer.flush();
        writer.writeObject(buffetID);
        writer.flush();

        Object object = reader.readObject();
        HashMap<String,ProductInfo> currentBuffetAssortment = (HashMap<String, ProductInfo>) object;
        return currentBuffetAssortment;
    }

    public void onPayButtonClick(Order order)
    {
        PaymentShowerController paymentShowerController = new PaymentShowerController(this);
        paymentShowerController.setReader(reader);
        paymentShowerController.setWriter(writer);
        new PaymentShower(paymentShowerController, order);
    }

    public void addNewOrder(Integer ID, Order order)
    {
        currentOrders.put(ID, order);
    }

}
