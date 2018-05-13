package server.view;

import database.SessionFactorySingleton;
import entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class BuffetAssortmentShower {
    private JFrame frame = new JFrame();
    private JComboBox<String> categoryList;
    private JComboBox itemList;
    private JComboBox buffetList;
    private JTextField amount;
    private JScrollPane scrollPane;
    private JTable buffetAssortment;

    public BuffetAssortmentShower() {
        buffetAssortment = new JTable();
        scrollPane = new JScrollPane(buffetAssortment);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JLabel additionalItemsNameLabel = new JLabel("Обновление ассортимента буфета");
        amount = new JTextField("", 30);
        amount.setToolTipText("Количество");
        JButton addButton = new JButton("Добавить");

        JPanel newCategoryPanel = new JPanel();
        newCategoryPanel.setLayout(new BoxLayout(newCategoryPanel, BoxLayout.Y_AXIS));
        additionalItemsNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newCategoryPanel.add(additionalItemsNameLabel);

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        categoryList = new JComboBox<>();
        itemList = new JComboBox<>();
        buffetList = new JComboBox<>();
        try {

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from CategoryEntity");
            List categories = query.list();

            String[] categoriesNames = new String[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
                CategoryEntity categoryEntity = (CategoryEntity) categories.get(i);
                categoriesNames[i] = categoryEntity.getName();
            }
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(categoriesNames);
            categoryList.setModel(model);

            categoryList.addActionListener(e -> {
                Session innerSession = null;
                Transaction innerTx = null;
                try {

                    innerSession = sessionFactory.openSession();
                    innerTx = innerSession.beginTransaction();
                    Query innerQuery = innerSession.createQuery("from CategoryEntity where name= :name");
                    innerQuery.setParameter("name", categoryList.getSelectedItem());
                    CategoryEntity categoryEntity = (CategoryEntity) innerQuery.list().get(0);
                    innerQuery = innerSession.createQuery("from ItemsEntity where categoryId= :categoryId");
                    innerQuery.setParameter("categoryId", categoryEntity.getCategoryId());
                    List items = innerQuery.list();

                    String[] itemsNames = new String[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        ItemsEntity itemsEntity = (ItemsEntity) items.get(i);
                        itemsNames[i] = itemsEntity.getName();
                    }
                    DefaultComboBoxModel<String> innerModel = new DefaultComboBoxModel<>(itemsNames);
                    itemList.setModel(innerModel);
                } catch (Exception ex) {
                    ex.printStackTrace();

                    innerTx.rollback();
                } finally {
                    if (innerSession != null) {
                        innerSession.close();
                    }
                }
            });

            query = session.createQuery("from CategoryEntity where name= :name");
            query.setParameter("name", categoryList.getSelectedItem());
            CategoryEntity categoryEntity = (CategoryEntity) query.list().get(0);
            query = session.createQuery("from ItemsEntity where categoryId= :categoryId");
            query.setParameter("categoryId", categoryEntity.getCategoryId());
            List items = query.list();

            String[] itemsNames = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                ItemsEntity itemsEntity = (ItemsEntity) items.get(i);
                itemsNames[i] = itemsEntity.getName();
            }
            model = new DefaultComboBoxModel<>(itemsNames);
            itemList.setModel(model);

            query = session.createQuery("from BuffetEntity");
            List buffets = query.list();

            String[] buffetsNames = new String[buffets.size()];
            for (int i = 0; i < buffets.size(); i++) {
                BuffetEntity buffetEntity = (BuffetEntity) buffets.get(i);
                buffetsNames[i] = buffetEntity.getLocation();
            }
            model = new DefaultComboBoxModel<>(buffetsNames);
            buffetList.setModel(model);
            buffetList.addActionListener(e -> init());

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }


        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(4, 2));
        itemsPanel.add(new JLabel("Буфет:"));
        itemsPanel.add(buffetList);
        itemsPanel.add(new JLabel("Категория:"));
        itemsPanel.add(categoryList);
        itemsPanel.add(new JLabel("Товар:"));
        itemsPanel.add(itemList);
        itemsPanel.add(new JLabel("Количество"));
        itemsPanel.add(amount);
        newCategoryPanel.add(itemsPanel, BorderLayout.CENTER);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newCategoryPanel.add(addButton);

        addItemButton(addButton);

        init();

        frame.getContentPane().add(newCategoryPanel, BorderLayout.CENTER);
        frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void init() {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {

            String[] columnNames = {
                    "Категория",
                    "Товар",
                    "Количество"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from BuffetEntity where location= :location");
            query.setParameter("location", buffetList.getSelectedItem());
            BuffetEntity buffetEntity = (BuffetEntity) query.list().get(0);
            query = session.createQuery("from BuffetsAssortmentEntity where buffetId= :buffetId");
            query.setParameter("buffetId", buffetEntity.getBuffetId());
            List assortment = query.list();

            String[][] data = new String[assortment.size()][3];
            for (int i = 0; i < assortment.size(); i++) {
                BuffetsAssortmentEntity buffetsAssortmentEntity = (BuffetsAssortmentEntity) assortment.get(i);
                query = session.createQuery("from ItemsEntity where itemId= :itemId");
                query.setParameter("itemId", buffetsAssortmentEntity.getItemId());
                ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                query = session.createQuery("from CategoryEntity where categoryId= :categoryId");
                query.setParameter("categoryId", itemsEntity.getCategoryId());
                CategoryEntity categoryEntity = (CategoryEntity) query.list().get(0);
                data[i][0] = categoryEntity.getName();
                data[i][1] = itemsEntity.getName();
                data[i][2] = String.valueOf(buffetsAssortmentEntity.getAmount());
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            buffetAssortment.setModel(tableModel);
            buffetAssortment.setEnabled(false);

        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void addItemButton(JButton addButton) {
        addButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!amount.getText().equals("")) {
                        boolean changed = false;
                        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

                        Session session = null;
                        Transaction tx = null;
                        try {
                            session = sessionFactory.openSession();
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from BuffetEntity where location= :location");
                            query.setParameter("location", buffetList.getSelectedItem());
                            BuffetEntity buffetEntity = (BuffetEntity) query.list().get(0);
                            query = session.createQuery("from ItemsEntity where name= :name");
                            query.setParameter("name", itemList.getSelectedItem());
                            ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                            query = session.createQuery("from BuffetsAssortmentEntity where buffetId= :buffetId and itemId= :itemId");
                            query.setParameter("buffetId", buffetEntity.getBuffetId());
                            query.setParameter("itemId", itemsEntity.getItemId());
                            List buffetAssortment = query.list();
                            try {
                                int newAmount = Integer.parseInt(amount.getText());
                                if (newAmount > 0) {
                                    if (buffetAssortment.size() == 0) {
                                        BuffetsAssortmentEntity buffetsAssortmentEntity = new BuffetsAssortmentEntity();
                                        buffetsAssortmentEntity.setItemId(itemsEntity.getItemId());
                                        buffetsAssortmentEntity.setBuffetId(buffetEntity.getBuffetId());
                                        buffetsAssortmentEntity.setAmount(newAmount);
                                        session.save(buffetsAssortmentEntity);
                                    } else {
                                        BuffetsAssortmentEntity buffetsAssortmentEntity = (BuffetsAssortmentEntity) buffetAssortment.get(0);
                                        buffetsAssortmentEntity.setAmount(((BuffetsAssortmentEntity) buffetAssortment.get(0)).getAmount() + newAmount);
                                        session.save(buffetsAssortmentEntity);
                                    }
                                    session.getTransaction().commit();
                                    amount.setText("");
                                    changed = true;
                                } else {
                                    JOptionPane.showMessageDialog(frame, new String[]{"Введите целое положительное число"},
                                            "Обновление ассортимента", JOptionPane.INFORMATION_MESSAGE, null);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, new String[]{"Введите целое положительное число"},
                                        "Обновление ассортимента", JOptionPane.INFORMATION_MESSAGE, null);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, new String[]{"Произошла ошибка при добавлении нового товара"},
                                    "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);

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
                        JOptionPane.showMessageDialog(frame, new String[]{"Заполните все данные"},
                                "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}
