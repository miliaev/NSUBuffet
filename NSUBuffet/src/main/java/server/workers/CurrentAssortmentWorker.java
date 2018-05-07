package server.workers;

import builder.Builder;
import entities.BuffetsAssortmentEntity;
import entities.ItemsEntity;
import order.Order;
import order.ProductInfo;
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
import java.util.List;

public class CurrentAssortmentWorker implements Parser {

    private Builder requestBuilder;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private HashMap<String, Runnable> commands = new HashMap<>();
    private HashMap<Integer, ObjectOutputStream> buffetOutputStreams;

    public CurrentAssortmentWorker(Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        this.commands.put(requestBuilder.getCurrentItems(), this::getCurrentItems);
    }

    private void getCurrentItems() {
        try {
            Object object = reader.readObject();
            Integer buffetId = (Integer) object;
            System.out.println(buffetId);
//            System.out.println(currentAssortment.getCurrentItems(buffetId - 1).toString());
            HashMap<String, ProductInfo> assortment = new HashMap<>();
            SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

            Session session = null;
            Transaction tx = null;

            try {
                session = sessionFactory.openSession();
                tx = session.beginTransaction();

                // Fetching saved data
                System.out.println("Get data from table...");
                Query query = session.createQuery("from BuffetsAssortmentEntity where buffetId= :buffetId");
                query.setParameter("buffetId", buffetId);
                List buffetsAssortmentList = query.list();
                for (Object buffetsAssortment : buffetsAssortmentList) {
                    BuffetsAssortmentEntity buffetsAssortmentEntity = ((BuffetsAssortmentEntity) buffetsAssortment);
                    query = session.createQuery("from ItemsEntity where itemId= :itemId");
                    query.setParameter("itemId", buffetsAssortmentEntity.getItemId());
                    List itemsList = query.list();
                    for (Object item : itemsList) {
                        ItemsEntity itemsEntity = ((ItemsEntity) item);
                        String key = itemsEntity.getName();
                        Integer amount = buffetsAssortmentEntity.getAmount();
                        Double price = itemsEntity.getCurrentPrice();
                        assortment.put(key, new ProductInfo(price, amount));
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();

                tx.rollback();
            } finally {
                if (session != null) {
                    session.close();
                }
            }
            System.out.println(assortment.toString());
            writer.writeObject(assortment);
            writer.reset();
        } catch (IOException | ClassNotFoundException e) {
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
