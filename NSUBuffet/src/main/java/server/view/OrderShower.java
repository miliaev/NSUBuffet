package server.view;

import database.SessionFactorySingleton;
import entities.*;
import order.Order;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderShower {
    private JFrame frame = new JFrame();

    public OrderShower() {
        JScrollPane scrollPane = new JScrollPane();
        String[] columnNames = {
                "ID заказа",
                "Товар",
                "Количество",
                "Стоимость"
        };

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from OrdersEntity");
            List orders = query.list();

            String[][] data = new String[orders.size()][4];
            for (int i = 0; i < orders.size(); i++) {
                OrdersEntity ordersEntity = (OrdersEntity) orders.get(i);
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", ordersEntity.getItemId());
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                data[i][0] = String.valueOf(ordersEntity.getOrderId());
                data[i][1] = itemsEntity.getName();
                data[i][2] = String.valueOf(ordersEntity.getAmount());
                data[i][3] = String.valueOf(ordersEntity.getPrice());
            }

            JTable table = new JTable(data, columnNames);
            table.setEnabled(false);
            scrollPane = new JScrollPane(table);

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
