package server;

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
import java.util.HashMap;

public class CurrentOrdersWorker implements Parser {

    private CurrentOrders currentOrders;
    private Builder requestBuilder;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private HashMap<Integer, ObjectOutputStream> buffetOutputStreams;
    private HashMap<String, Runnable> commands = new HashMap<>();

    public CurrentOrdersWorker(Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        this.currentOrders = new CurrentOrders();
        this.commands.put(requestBuilder.addNewOrder(), this::addNewOrder);
        this.commands.put(requestBuilder.getOrderByID(), this::getOrderByID);
        this.commands.put(requestBuilder.deleteOrderByID(), this::deleteOrderByID);
    }

    private void addNewOrder() {
//        try {
//            //ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
//            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(new RequestBuilder().needUpdateView());
////            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(currentOrders.getCurrentOrders(order.getBuffetID() - 1));
//            System.out.println("sent to seller");
////            buffetOutputStreams.get(order.getBuffetID()).
//            //ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            //System.out.println(currentAssortment.getCurrentItems(buffetId - 1).toString());
//            //writer.writeObject(currentAssortment.getCurrentItems(buffetId - 1));
//            //writer.flush();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }

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
            currentOrdersEntity.setDate(order.getTime().toString());
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
                ordersEntity.setItemId(((ItemsEntity)query.list().get(0)).getItemId());
                session.save(ordersEntity);
//                session.getTransaction().commit();
            }
            currentOrdersEntity.setStatus("ready");
            session.getTransaction().commit();
//            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(new RequestBuilder().needUpdateView());
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

    }

    private void getOrderByID() {
        try {
            int orderID = reader.readInt();
            writer.writeObject(currentOrders.getOrderByID(orderID));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderByID() {
        try {
            int orderID = reader.readInt();
            Order order = currentOrders.getOrderByID(orderID);
            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(new RequestBuilder().needUpdateView());
            currentOrders.deleteOrderByID(orderID);
            buffetOutputStreams.get(order.getBuffetID() - 1).writeObject(currentOrders.getCurrentOrders(order.getBuffetID() - 1));
        } catch (IOException e) {
            e.printStackTrace();
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
