package server.view;

import database.SessionFactorySingleton;
import entities.BuffetEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BuffetShower {
    private JFrame frame = new JFrame();
    private JTextField buffetName;
    private JScrollPane scrollPane;

    public BuffetShower() {
        JLabel buffetNameLabel = new JLabel("Добавление нового буффета");
        buffetName = new JTextField("Введите месторасположение нового буффета", 30);
        JButton buffetButton = new JButton("Добавить");

        JPanel newBuffetPanel = new JPanel();
        newBuffetPanel.setLayout(new BoxLayout(newBuffetPanel, BoxLayout.Y_AXIS));
        buffetNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buffetName.setAlignmentX(Component.CENTER_ALIGNMENT);
        buffetName.setHorizontalAlignment(JTextField.CENTER);
        buffetButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newBuffetPanel.add(buffetNameLabel);
        newBuffetPanel.add(buffetName);
        newBuffetPanel.add(buffetButton);

        addBuffetButton(buffetButton);

        init();

        frame.getContentPane().add(newBuffetPanel, BorderLayout.CENTER);
        frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void init() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {

            String[] columnNames = {
                    "ID",
                    "Месторасположение"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from BuffetEntity");
            List buffetNames = query.list();

            String[][] data = new String[buffetNames.size()][2];
            for (int i = 0; i < buffetNames.size(); i++) {
                BuffetEntity buffetEntity = (BuffetEntity) buffetNames.get(i);
                data[i][0] = String.valueOf(buffetEntity.getBuffetId());
                data[i][1] = buffetEntity.getLocation();
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

    private void addBuffetButton(JButton buffetButton) {
        buffetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!buffetName.getText().equals("Введите месторасположение нового буффета")
                            && !buffetName.getText().equals("")) {
                        boolean changed = false;
                        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

                        Session session = null;
                        Transaction tx = null;
                        try {
                            session = sessionFactory.openSession();
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from BuffetEntity where location= :location");
                            query.setParameter("location", buffetName.getText());
                            if (query.list().size() == 0) {
                                BuffetEntity buffetEntity = new BuffetEntity();
                                buffetEntity.setLocation(buffetName.getText());
                                session.save(buffetEntity);
                                session.getTransaction().commit();
                                buffetName.setText("");
                                changed = true;
                            } else {
                                JOptionPane.showMessageDialog(frame, new String[]{"Такой буффет уже существует"},
                                        "Добавление буффета", JOptionPane.INFORMATION_MESSAGE, null);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, new String[]{"Произошла ошибка при добавлении нового буффета"},
                                    "Добавление буффета", JOptionPane.INFORMATION_MESSAGE, null);

                            tx.rollback();
                        } finally {
                            if (session != null) {
                                session.close();
                            }
                        }

                        if (changed) {
                            frame.getContentPane().remove(scrollPane);
                            init();
                            frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
                            frame.revalidate();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, new String[]{"Введите название буффета"},
                                "Добавление буффета", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}