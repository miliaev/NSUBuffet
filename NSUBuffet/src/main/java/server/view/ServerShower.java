package server.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerShower {

    public ServerShower() {
        JPanel mainPanel = new JPanel();
        JButton itemStatisticButton = new JButton("Посмотреть статистику товаров");
        JButton pairStatisticButton = new JButton("Посмотреть парную статистику");
        JButton categoryButton = new JButton("Посмотреть/добавить категорию");
        JButton buffetButton = new JButton("Посмотреть/добавить буффет");
        JButton topItemsButton = new JButton("Показать ТОП товаров");
        JButton itemsButton = new JButton("Посмотреть/добавить товар");
        JButton buffetAssortmentButton = new JButton("Посмотреть/добавить ассортимент в буфет");
        JButton priceButton = new JButton("Посмотреть/изменить цены на товары");

        mainPanel.add(itemStatisticButton);
        mainPanel.add(pairStatisticButton);
        mainPanel.add(categoryButton);
        mainPanel.add(buffetButton);
        mainPanel.add(topItemsButton);
        mainPanel.add(itemsButton);
        mainPanel.add(buffetAssortmentButton);
        mainPanel.add(priceButton);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        addItemStatisticButton(itemStatisticButton);
        addPairStatisticButton(pairStatisticButton);
        addCategoryButton(categoryButton);
        addBuffetButton(buffetButton);
        addTopItemsButton(topItemsButton);
        addItemsButton(itemsButton);
        addBuffetAssortment(buffetAssortmentButton);
        addPriceButton(priceButton);
    }

    private void addItemStatisticButton(JButton itemStatisticButton) {
        itemStatisticButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //frame.setVisible(false);
                    new ItemStatisticShower();
                }
            }
        });
    }

    private void addPairStatisticButton(JButton pairStatisticButton) {
        pairStatisticButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    //frame.setVisible(false);
                    new PairStatisticShower();
                }
            }
        });
    }

    private void addCategoryButton(JButton categoryButton) {
        categoryButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new CategoryShower();
                }
            }
        });
    }

    private void addBuffetButton(JButton buffetButton) {
        buffetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new BuffetShower();
                }
            }
        });
    }

    private void addTopItemsButton(JButton topItemsButton) {
        topItemsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new TopItemsShower();
                }
            }
        });
    }

    private void addItemsButton(JButton itemsButton) {
        itemsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new ItemsShower();
                }
            }
        });
    }

    private void addBuffetAssortment(JButton buffetAssortmentButton) {
        buffetAssortmentButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new BuffetAssortmentShower();
                }
            }
        });
    }

    private void addPriceButton(JButton priceButton) {
        priceButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    new PriceShower();
                }
            }
        });
    }
}
