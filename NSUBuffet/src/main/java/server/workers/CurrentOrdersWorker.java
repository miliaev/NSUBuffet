package server.workers;

import builder.Builder;
import builder.RequestBuilder;
import entities.CurrentOrdersEntity;
import entities.ItemsEntity;
import entities.OrdersEntity;
import order.Order;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import parser.Parser;
import database.SessionFactorySingleton;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CurrentOrdersWorker implements Parser {

    private Builder requestBuilder;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private HashMap<Integer, ObjectOutputStream> buffetOutputStreams;
    private HashMap<String, Runnable> commands = new HashMap<>();

    public CurrentOrdersWorker(Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        this.commands.put(requestBuilder.addNewOrder(), this::addNewOrder);
        this.commands.put(requestBuilder.getOrderByID(), this::getOrderByID);
        this.commands.put(requestBuilder.deleteOrderByID(), this::deleteOrderByID);
    }

    private void addNewOrder() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Object object = reader.readObject();
            Order order = (Order) object;
//            currentOrders.addNewOrder(order);
            CurrentOrdersEntity currentOrdersEntity = new CurrentOrdersEntity();
            currentOrdersEntity.setOrderId(order.getId());
            currentOrdersEntity.setBuffetId(order.getBuffetID());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
            currentOrdersEntity.setDate(dateFormat.format(order.getTime()));
//            currentOrdersEntity.setStatus(null);
            session.save(currentOrdersEntity);
//            session.getTransaction().commit();

            for (String itemName : order.getOrderItems().keySet()) {
                OrdersEntity ordersEntity = new OrdersEntity();
                ordersEntity.setOrderId(order.getId());
                ordersEntity.setAmount(order.getOrderItems().get(itemName));
                ordersEntity.setPrice(order.getItemsPrice().get(itemName));
                Query query = session.createQuery("from ItemsEntity where name= :name");
                query.setParameter("name", itemName);
                ordersEntity.setItemId(((ItemsEntity) query.list().get(0)).getItemId());
                session.save(ordersEntity);
//                session.getTransaction().commit();
            }
            currentOrdersEntity.setStatus("ready");
            ArrayList<Order> orders = getOrdersForBuffet(order.getBuffetID(), session);
            session.getTransaction().commit();
            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(new RequestBuilder().needUpdateView());
            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(orders);

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    private ArrayList<Order> getOrdersForBuffet(int buffetID, Session session)
    {
        ArrayList<Order> orders = new ArrayList<>();

        Query query = session.createQuery("from CurrentOrdersEntity where buffetId= :buffetId");
        query.setParameter("buffetId", buffetID);
        List allOrders = query.list();
        for (Object allOrder : allOrders)
        {
            CurrentOrdersEntity currentOrdersEntity = (CurrentOrdersEntity) allOrder;
            int orderId = currentOrdersEntity.getOrderId();
            query = session.createQuery("from OrdersEntity where orderId= :orderId");
            query.setParameter("orderId", orderId);
            List currentOrder = query.list();
            Order order = new Order();
            order.setBuffetID(buffetID);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
            try
            {
                order.setTime(dateFormat.parse(currentOrdersEntity.getDate()));
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            for (Object aCurrentOrder : currentOrder)
            {
                OrdersEntity ordersEntity = (OrdersEntity) aCurrentOrder;
                order.setId(ordersEntity.getOrderId());
                order.setPrice(order.getPrice() + ordersEntity.getPrice());
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", ordersEntity.getItemId());
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                order.getItemsPrice().put(itemsEntity.getName(), ordersEntity.getPrice());
                order.getOrderItems().put(itemsEntity.getName(), ordersEntity.getAmount());
            }
            orders.add(order);

        }
        return  orders;
    }

    private void getOrderByID() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            int orderID = reader.readInt();
            Query query = session.createQuery("from CurrentOrdersEntity where orderId= :orderId");
            query.setParameter("orderId", orderID);
            Order order = new Order();
            order.setId(orderID);
            CurrentOrdersEntity currentOrdersEntity = ((CurrentOrdersEntity) query.list().get(0));
            order.setBuffetID(currentOrdersEntity.getBuffetId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
            order.setTime(dateFormat.parse(currentOrdersEntity.getDate()));
            query = session.createQuery("from OrdersEntity where orderId= :orderId");
            query.setParameter("orderId", orderID);
            List<OrdersEntity> orders = query.list();
            for (OrdersEntity ordersEntity : orders) {
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", ordersEntity.getItemId());
                order.getOrderItems().put(((ItemsEntity) query.list().get(0)).getName(), ordersEntity.getAmount());
                order.getItemsPrice().put(((ItemsEntity) query.list().get(0)).getName(), ordersEntity.getPrice());
            }
            writer.writeObject(order);
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void deleteOrderByID() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            int orderID = reader.readInt();
            Query query = session.createQuery("from CurrentOrdersEntity where orderId= :orderId");
            query.setParameter("orderId", orderID);
            int buffetId = ((CurrentOrdersEntity) query.list().get(0)).getBuffetId();
            query = session.createQuery("delete from CurrentOrdersEntity where orderId= :orderId");
            query.setParameter("orderId", orderID);
            if (query.executeUpdate() > 0) {
                System.out.println("deleted");
            }
            ArrayList<Order> orders = getOrdersForBuffet(buffetId, session);
            session.getTransaction().commit();
            buffetOutputStreams.get(buffetId - 1).writeObject(new RequestBuilder().needUpdateView());
            buffetOutputStreams.get(buffetId - 1).writeObject(orders);
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean parse(String request, ObjectInputStream reader, ObjectOutputStream writer, HashMap<Integer, ObjectOutputStream> buffets) {
        this.reader = reader;
        this.writer = writer;
        this.buffetOutputStreams = buffets;
        if (commands.containsKey(request)) {
            commands.get(request).run();
            return true;
        }
        return false;
    }
}
