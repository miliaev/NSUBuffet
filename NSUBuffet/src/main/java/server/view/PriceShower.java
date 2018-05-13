package server.view;

import database.SessionFactorySingleton;
import entities.CategoryEntity;
import entities.ItemsEntity;
import entities.PriceEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PriceShower {
    private JFrame frame = new JFrame();
    private JComboBox categoryList;
    private JComboBox itemList;
    private JTextField price;
    private JScrollPane scrollPane;
    private JTable itemsTable;

    public PriceShower() {
        itemsTable = new JTable();
        scrollPane = new JScrollPane(itemsTable);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        JLabel addintionalItemsNameLabel = new JLabel("Изменение цен");
        price = new JTextField("", 30);
        price.setToolTipText("Введите цену");
        JButton priceButton = new JButton("Изменить");

        JPanel newCategoryPanel = new JPanel();
        newCategoryPanel.setLayout(new BoxLayout(newCategoryPanel, BoxLayout.Y_AXIS));
        addintionalItemsNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newCategoryPanel.add(addintionalItemsNameLabel);

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        categoryList = new JComboBox();
        itemList = new JComboBox();
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
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }


        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(3, 2));
        itemsPanel.add(new JLabel("Категория:"));
        itemsPanel.add(categoryList);
        itemsPanel.add(new JLabel("Название:"));
        itemsPanel.add(itemList);
        itemsPanel.add(new JLabel("Новая цена:"));
        itemsPanel.add(price);
        newCategoryPanel.add(itemsPanel, BorderLayout.CENTER);
        priceButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newCategoryPanel.add(priceButton);

        addChangePriceButton(priceButton);

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
                    "Название",
                    "Цена"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from ItemsEntity");
            List items = query.list();

            String[][] data = new String[items.size()][3];
            for (int i = 0; i < items.size(); i++) {
                ItemsEntity itemsEntity = (ItemsEntity) items.get(i);

                query = session.createQuery("from CategoryEntity where categoryId= :categoryId");
                query.setParameter("categoryId", itemsEntity.getCategoryId());
                CategoryEntity categoryEntity = (CategoryEntity) query.list().get(0);
                data[i][0] = categoryEntity.getName();

                data[i][1] = itemsEntity.getName();
                data[i][2] = String.valueOf(itemsEntity.getCurrentPrice());
            }
            DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
            itemsTable.setModel(tableModel);
            itemsTable.setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void addChangePriceButton(JButton priceButton) {
        priceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!price.getText().equals("")) {
                        boolean changed = false;
                        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

                        Session session = null;
                        Transaction tx = null;
                        try {
                            session = sessionFactory.openSession();
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from ItemsEntity where name= :name");
                            query.setParameter("name", itemList.getSelectedItem());
                            ItemsEntity itemsEntity = (ItemsEntity) query.list().get(0);
                            try {
                                double currentPrice = Double.parseDouble(price.getText());
                                if (currentPrice > 0) {
                                    PriceEntity priceEntity = new PriceEntity();
                                    priceEntity.setItemId(itemsEntity.getItemId());
                                    priceEntity.setPrice(currentPrice);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
                                    Date currentDate = new Date();
                                    priceEntity.setDate(dateFormat.format(currentDate));
                                    session.save(priceEntity);

                                    session.getTransaction().commit();
                                    price.setText("");
                                    changed = true;
                                } else {
                                    JOptionPane.showMessageDialog(frame, new String[]{"Цена должна быть > 0"},
                                            "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, new String[]{"Введите число"},
                                        "Изменение цен", JOptionPane.INFORMATION_MESSAGE, null);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, new String[]{"Произошла ошибка при изменении цены товара"},
                                    "Изменение цен", JOptionPane.INFORMATION_MESSAGE, null);

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
                                "Изменение цен", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}
