package server.view;

import database.SessionFactorySingleton;
import entities.CategoryEntity;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoryShower {
    private JFrame frame = new JFrame();
    private JTextField categoryName;
    private JScrollPane scrollPane;

    public CategoryShower() {

        JLabel categoryNameLabel = new JLabel("Добавление новой категории");
        categoryName = new JTextField("Введите название новой категории", 30);
        JButton categoryButton = new JButton("Добавить");

        JPanel newCategoryPanel = new JPanel();
        newCategoryPanel.setLayout(new BoxLayout(newCategoryPanel, BoxLayout.Y_AXIS));
        categoryNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryName.setAlignmentX(Component.CENTER_ALIGNMENT);
        categoryName.setHorizontalAlignment(JTextField.CENTER);
        categoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newCategoryPanel.add(categoryNameLabel);
        newCategoryPanel.add(categoryName);
        newCategoryPanel.add(categoryButton);

        addCategoryButton(categoryButton);

        init();

        frame.getContentPane().add(newCategoryPanel, BorderLayout.CENTER);
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
                    "Название"
            };

            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Query query = session.createQuery("from CategoryEntity");
            List categoryNames = query.list();

            String[][] data = new String[categoryNames.size()][2];
            for (int i = 0; i < categoryNames.size(); i++) {
                CategoryEntity categoryEntity = (CategoryEntity) categoryNames.get(i);
                data[i][0] = String.valueOf(categoryEntity.getCategoryId());
                data[i][1] = categoryEntity.getName();
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

    private void addCategoryButton(JButton categoryButton) {
        categoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!categoryName.getText().equals("Введите название новой категории")
                            && !categoryName.getText().equals("")) {
                        boolean changed = false;
                        SessionFactory sessionFactory = SessionFactorySingleton.getInstance().getSessionFactory();

                        Session session = null;
                        Transaction tx = null;
                        try {
                            session = sessionFactory.openSession();
                            tx = session.beginTransaction();
                            Query query = session.createQuery("from CategoryEntity where name= :name");
                            query.setParameter("name", categoryName.getText());
                            if (query.list().size() == 0) {
                                CategoryEntity categoryEntity = new CategoryEntity();
                                categoryEntity.setName(categoryName.getText());
                                session.save(categoryEntity);
                                session.getTransaction().commit();
                                categoryName.setText("");
                                changed = true;
                            } else {
                                JOptionPane.showMessageDialog(frame, new String[]{"Такая категория уже существует"},
                                        "Добавление категории", JOptionPane.INFORMATION_MESSAGE, null);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, new String[]{"Произошла ошибка при добавлении новой категории"},
                                    "Добавление категории", JOptionPane.INFORMATION_MESSAGE, null);

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
                        JOptionPane.showMessageDialog(frame, new String[]{"Введите название категории"},
                                "Добавление категории", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                }
            }
        });
    }
}
