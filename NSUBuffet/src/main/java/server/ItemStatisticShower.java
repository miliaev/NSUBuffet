package server;

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

public class ItemStatisticShower
{
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();

    private JTextArea incoming;
    private JTextField outgoing;

    public ItemStatisticShower()
    {
        JScrollPane scrollPane = new JScrollPane();
        String[] columnNames = {
                "Товар",
                "Количество",
                "Буффет",
                "Дата"
        };

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from ItemStatisticEntity");
            List itemsStatistic = query.list();

            String[][] data = new String[itemsStatistic.size()][4];
            for(int i = 0 ; i < itemsStatistic.size(); i++)
            {
                ItemStatisticEntity itemStatisticEntity = (ItemStatisticEntity) itemsStatistic.get(i);
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", itemStatisticEntity.getItemId());
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                data[i][0] = itemsEntity.getName();

                data[i][1] = itemStatisticEntity.getCount().toString();

                query = session.createQuery("from BuffetEntity where buffetId= :buffetId");
                query.setParameter("buffetId", itemStatisticEntity.getBuffetId());
                BuffetEntity buffetEntity = (BuffetEntity) query.list().get(0);
                data[i][2] = buffetEntity.getLocation();

                data[i][3] = itemStatisticEntity.getDate();
            }

            JTable table = new JTable(data, columnNames);
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setPreferredSize(new Dimension(780, 500));
            scrollPane = new JScrollPane(table);
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }


}
