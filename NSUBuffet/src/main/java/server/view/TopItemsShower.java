package server.view;

import database.SessionFactorySingleton;
import entities.ItemsEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TopItemsShower {
    private JFrame frame = new JFrame();
    private JTextField limit;
    private JScrollPane scrollPane;

    private static final int DEFAULT_LIMIT = 100;

    public TopItemsShower() {
        JLabel limitLabel = new JLabel("Показать ТОП N товаров:");
        limit = new JTextField(30);
        limit.setToolTipText("Введите N");
        JButton limitButton = new JButton("Применить");

        JPanel newBuffetPanel = new JPanel();
        newBuffetPanel.setLayout(new BoxLayout(newBuffetPanel, BoxLayout.Y_AXIS));
        limitLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        limit.setAlignmentX(Component.CENTER_ALIGNMENT);
        limit.setHorizontalAlignment(JTextField.CENTER);
        limitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newBuffetPanel.add(limitLabel);
        newBuffetPanel.add(limit);
        newBuffetPanel.add(limitButton);

        addBuffetButton(limitButton);

        init(DEFAULT_LIMIT);

        frame.getContentPane().add(newBuffetPanel, BorderLayout.CENTER);
        frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void init(int currentLimit) {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {

            String[] columnNames = {
                    "Товар",
                    "Количество"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createSQLQuery(
                    "SELECT item_id, SUM(count) " +
                            "FROM ItemStatistic " +
                            "GROUP BY item_id " +
                            "ORDER BY SUM(count) DESC " +
                            "LIMIT :currentLimit").setInteger("currentLimit", currentLimit);
            List topItems = query.list();

            String[][] data = new String[topItems.size()][2];
            for (int i = 0; i < topItems.size(); i++) {
                Object[] row = (Object[]) topItems.get(i);
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", row[0]);
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                data[i][0] = itemsEntity.getName();
                data[i][1] = String.valueOf(row[1]);
            }

            JTable table = new JTable(data, columnNames);
            table.setEnabled(false);
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400, 200));

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void addBuffetButton(JButton limitButton) {
        limitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!limit.getText().equals("")) {
                        try {
                            int currentLimit = Integer.parseInt(limit.getText());
                            if (currentLimit > 0) {
                                frame.getContentPane().remove(scrollPane);
                                init(currentLimit);
                                frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
                                frame.revalidate();
                            } else {
                                JOptionPane.showMessageDialog(frame, new String[]{"Введите натуральное число"},
                                        "ТОП товаров", JOptionPane.INFORMATION_MESSAGE, null);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, new String[]{"Введите число"},
                                    "ТОП товаров", JOptionPane.INFORMATION_MESSAGE, null);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, new String[]{"Введите N"},
                                "ТОП товаров", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}
