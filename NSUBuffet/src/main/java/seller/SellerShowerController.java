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
        orders = new ArrayList<>();

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try
        {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from CurrentOrdersEntity where buffetId= :buffetId");
            query.setParameter("buffetId", buffetID);
            List allOrders = query.list();
            for(int i = 0; i < allOrders.size(); i++)
            {
                CurrentOrdersEntity currentOrdersEntity = (CurrentOrdersEntity) allOrders.get(i);
                int orderId  = currentOrdersEntity.getOrderId();
                query = session.createQuery("from OrdersEntity where orderId= :orderId");
                query.setParameter("orderId", orderId);
                List currentOrder = query.list();
                Order order = new Order();
                for(int j = 0; j < currentOrder.size(); j++)
                {
                    OrdersEntity ordersEntity = (OrdersEntity) currentOrder.get(j);
                    order.setBuffetID(buffetID);
                    order.setId(ordersEntity.getOrderId());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm");
                    order.setTime(dateFormat.parse(currentOrdersEntity.getDate()));
                    order.setPrice(order.getPrice() + ordersEntity.getPrice());
                    query = session.createQuery("from ItemsEntity where itemId= :itemId");
                    query.setParameter("itemId", ordersEntity.getItemId());
                    ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                    order.getItemsPrice().put(itemsEntity.getName(), ordersEntity.getPrice());
                    order.getOrderItems().put(itemsEntity.getName(), ordersEntity.getAmount());
                }
                orders.add(order);

            }

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        sellerShower.updateView(orders);
    }


}
