package server;

import entities.BuffetEntity;
import entities.BuffetsAssortmentEntity;
import entities.ItemsEntity;
import order.Order;
import order.ProductInfo;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import server.database.SessionFactorySingleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class CurrentAssortment {
    private HashMap<Integer, HashMap<String, ProductInfo>> assortment;
    private ArrayList<HashMap<String, ProductInfo>> buffets;
    private static final int NUMBER_OF_BUFFETS = 2;

    public CurrentAssortment() {
        this.buffets = new ArrayList<>(NUMBER_OF_BUFFETS);
        for (int i = 0; i < NUMBER_OF_BUFFETS; ++i) {
            buffets.add(new HashMap<>());
        }
        this.assortment = new HashMap<>();
//        loadAssortment();
    }

    public HashMap<Integer, HashMap<String, ProductInfo>> getCurrentAssortment() {
        return assortment;
    }

    private void loadAssortment() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            // Fetching saved data
            System.out.println("Get data from table...");
            Query query = session.createQuery("from BuffetsAssortmentEntity");
            List buffetsAssortmentList = query.list();
            for (Object buffetsAssortment : buffetsAssortmentList) {
                BuffetsAssortmentEntity buffetsAssortmentEntity = ((BuffetsAssortmentEntity) buffetsAssortment);
                query = session.createQuery("from ItemEntity where itemID= :itemID");
                query.setParameter("itemID", buffetsAssortmentEntity.getItemId());
                List itemsList = query.list();
                for (Object item : itemsList) {
                    ItemsEntity itemsEntity = ((ItemsEntity) item);
                    String key = itemsEntity.getName();
                    Integer amount = buffetsAssortmentEntity.getAmount();
                    Double price = itemsEntity.getCurrentPrice();
                    Integer buffetNumber = buffetsAssortmentEntity.getBuffetId();
                    buffets.get(buffetNumber - 1).put(key, new ProductInfo(price, amount));
                }
                for (int i = 0; i < NUMBER_OF_BUFFETS; ++i) {
                    assortment.put(i, buffets.get(i));
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
    }

    public HashMap<String, ProductInfo> getCurrentItems(Integer buffetID) {
        return assortment.get(buffetID);
    }

    public void updateCurrentAssortment(Order order) {
        HashMap<String, Integer> newOrder = order.getOrderItems();
        int buffetID = order.getBuffetID() - 1;
        for (String orderItem : newOrder.keySet()) {
            ProductInfo productInfo = assortment.get(order.getBuffetID() - 1).get(orderItem);
            productInfo.setAmount(productInfo.getAmount() - newOrder.get(orderItem));
            assortment.get(buffetID).put(orderItem, productInfo);
        }
    }

    public void printAssortment(String assortmentFile) {
        try {
            FileWriter fileWriter = new FileWriter(assortmentFile, false);
            for (int buffet : assortment.keySet()) {
                for (HashMap.Entry<String, ProductInfo> item : assortment.get(buffet).entrySet()) {
                    fileWriter.write(item.getKey());
                    fileWriter.append('|');
                    fileWriter.append(item.getValue().getAmount().toString());
                    fileWriter.append('|');
                    fileWriter.append(item.getValue().getPrice().toString());
                    fileWriter.append('|');
                    Integer buffetNumber = buffet + 1;
                    fileWriter.append(buffetNumber.toString());
                    fileWriter.append("\n");
                    fileWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
