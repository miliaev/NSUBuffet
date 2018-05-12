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
    private JPanel mainPanel = new JPanel();

    public OrderShower() {
        JScrollPane scrollPane = new JScrollPane();
        String[] columnNames = {
                "Товар_1",
                "Товар_2",
                "Количество"
        };

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from PairStatisticEntity");
            List pairStatistic = query.list();

            String[][] data = new String[pairStatistic.size()][3];
            for (int i = 0; i < pairStatistic.size(); i++) {
                PairStatisticEntity pairStatisticEntity = (PairStatisticEntity) pairStatistic.get(i);
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", pairStatisticEntity.getItem1Id());
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                data[i][0] = itemsEntity.getName();
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", pairStatisticEntity.getItem2Id());
                itemsEntity = (ItemsEntity) query.list().get(0);
                data[i][1] = itemsEntity.getName();
                data[i][2] = pairStatisticEntity.getCount().toString();
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
