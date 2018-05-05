package seller;

import builder.RequestBuilder;
import database.SessionFactorySingleton;
import entities.CurrentOrdersEntity;
import entities.ItemsEntity;
import entities.OrdersEntity;
import order.Order;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by miketurch on 22.12.17.
 */
public class SellerShowerController
{
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private SellerShower sellerShower;
    private ArrayList<Order> orders = new ArrayList<>();
    private RequestBuilder requestBuilder = new RequestBuilder();
    private final static int SERVER = 1;

    private int buffetID;

    private HashMap<String, Runnable> commands = new HashMap<>();


    public SellerShowerController(ObjectOutputStream writer, ObjectInputStream reader, int buffedID) throws IOException
    {
        this.writer = writer;
        this.reader = reader;
        writer.writeInt(SERVER);
        writer.flush();
        this.buffetID = buffedID;
        writer.writeInt(buffedID);
        writer.flush();
        this.commands.put(requestBuilder.needUpdateView(), this::needUpdateView);
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    public void setSellerShower(SellerShower sellerShower)
    {
        this.sellerShower = sellerShower;
    }

    public SellerShower getSellerShower()
    {
        return sellerShower;
    }

    class IncomingReader implements Runnable
    {
        public void run()
        {
            Object object;
            try
            {
                while ((object = reader.readObject()) != null)
                {
                    commands.get((String) object).run();
                }
            }
            catch (IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }

    public void needUpdateView()
    {
        try
        {
            orders = (ArrayList<Order>)reader.readObject();
            sellerShower.updateView(orders);

        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }


}
