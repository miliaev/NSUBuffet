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
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ItemsShower
{
    private JFrame frame = new JFrame();
    private JTextField itemsName;
    private JTextField price;
    private JComboBox categoryList;
    private JScrollPane scrollPane;

    public ItemsShower()
    {

        JLabel addintionalItemsNameLabel = new JLabel("Добавление нового товара");
        itemsName = new JTextField("Введите название товара", 30);
        price = new JTextField("Введите цену", 30);
        JButton categoryButton = new JButton("Добавить");

        JPanel newCategoryPanel = new JPanel();
        newCategoryPanel.setLayout(new BoxLayout(newCategoryPanel, BoxLayout.Y_AXIS));
        addintionalItemsNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newCategoryPanel.add(addintionalItemsNameLabel);

        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        categoryList = new JComboBox();
        try {

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from CategoryEntity");
            List categories = query.list();

            String[] categoriesNames = new String[categories.size()];
            for(int i = 0 ; i < categories.size(); i++)
            {
                CategoryEntity categoryEntity = (CategoryEntity) categories.get(i);
                categoriesNames[i] = categoryEntity.getName();
            }
            categoryList =  new JComboBox(categoriesNames);

        }
        catch (Exception ex) {
            ex.printStackTrace();

            tx.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }


        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(3, 2));
        itemsPanel.add( new JLabel("Навание:"));
        itemsPanel.add( itemsName);
        itemsPanel.add( new JLabel("Категория:"));
        itemsPanel.add( categoryList);
        itemsPanel.add( new JLabel("Цена:"));
        itemsPanel.add( price);
        newCategoryPanel.add(itemsPanel, BorderLayout.CENTER);
        categoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newCategoryPanel.add(categoryButton);

        addCategoryButton(categoryButton);

        init();

        frame.getContentPane().add(newCategoryPanel, BorderLayout.CENTER);
        frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
        frame.setSize(600,400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void init()
    {
        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

        Session session = null;
        Transaction tx = null;

        try {

            String[] columnNames = {
                    "ID",
                    "Категория",
                    "Название",
                    "Цена"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from ItemsEntity");
            List items = query.list();

            String[][] data = new String[items.size()][4];
            for(int i = 0 ; i < items.size(); i++)
            {
                ItemsEntity itemsEntity = (ItemsEntity) items.get(i);
                data[i][0] = String.valueOf(itemsEntity.getItemId());

                query = session.createQuery("from CategoryEntity where categoryId= :categoryId");
                query.setParameter("categoryId", itemsEntity.getCategoryId());
                CategoryEntity categoryEntity = (CategoryEntity) query.list().get(0);
                data[i][1] = categoryEntity.getName();

                data[i][2] = itemsEntity.getName();
                data[i][3] = String.valueOf(itemsEntity.getCurrentPrice());
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

    private void addCategoryButton(JButton categoryButton)
    {
        categoryButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if(!itemsName.getText().equals("Введите название товара")
                            && !itemsName.getText().equals("")
                            && !price.getText().equals("Введите цену")
                            && !price.getText().equals(""))
                    {
                        boolean changed = false;
                        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

                        Session session = null;
                        Transaction tx = null;
                        try
                        {
                            session = sessionFactory.openSession();
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from ItemsEntity where name= :name");
                            query.setParameter("name", itemsName.getText());
                            if(query.list().size() == 0)
                            {
                                ItemsEntity itemsEntity = new ItemsEntity();
                                itemsEntity.setName(itemsName.getText());
                                try
                                {
                                    double currentPrice = Double.parseDouble(price.getText());
                                    if (currentPrice > 0)
                                    {
                                        query = session.createQuery("from CategoryEntity where name= :name");
                                        query.setParameter("name", categoryList.getSelectedItem());
                                        itemsEntity.setCategoryId(((CategoryEntity) query.list().get(0)).getCategoryId());
                                        session.save(itemsEntity);

                                        PriceEntity priceEntity = new PriceEntity();
                                        priceEntity.setItemId(itemsEntity.getItemId());
                                        priceEntity.setPrice(currentPrice);
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
                                        Date currentDate = new Date();
                                        priceEntity.setDate(dateFormat.format(currentDate));
                                        session.save(priceEntity);

                                        session.getTransaction().commit();
                                        itemsName.setText("");
                                        price.setText("");
                                        changed = true;
                                    } else
                                    {
                                        JOptionPane.showMessageDialog(frame, new String[]{"Цена должна быть > 0"},
                                                "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);
                                    }
                                }
                                catch (NumberFormatException ex)
                                {
                                    JOptionPane.showMessageDialog(frame, new String[]{"Введите число"},
                                            "ТОП товаров", JOptionPane.INFORMATION_MESSAGE, null);
                                }
                            }
                            else {
                                JOptionPane.showMessageDialog(frame, new String[]{"Такой товар уже существует"},
                                        "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);
                            }

                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, new String[]{"Произошла ошибка при добавлении нового товара"},
                                    "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);

                            tx.rollback();
                        } finally {
                            if (session != null) {
                                session.close();
                            }
                        }

                        if(changed)
                        {
                            frame.getContentPane().remove(scrollPane);
                            init();
                            frame.getContentPane().add(scrollPane, BorderLayout.NORTH);
                            frame.revalidate();
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, new String[]{"Заполните все данные"},
                                "Добавление товара", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}
