package seller;

import order.Order;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by miketurch on 22.12.17.
 */

public class SellerShower
{
    private SellerShowerController sellerShowerController;
    private JFrame frame = new JFrame("Текущие заказы");
    JPanel mainPanel = new JPanel();

    public SellerShower(SellerShowerController sellerShowerController)
    {
        this.sellerShowerController = sellerShowerController;
    }

    public void go()
    {
        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setSize(200,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void updateView(ArrayList<Order> orders)
    {
        frame.remove(mainPanel);
        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel ,BoxLayout.Y_AXIS));
        //mainPanel.setLayout(new GridLayout(orders.size(), 1));
        for(Order currentOrder: orders)
        {
            String items = "<html> ID заказа: " + currentOrder.getId() + "<br/>";
            items += currentOrder.getTime() + "<br/>";
            for(String currentItem: currentOrder.getOrderItems().keySet())
            {
                items += currentItem + " (количество: " + currentOrder.getOrderItems().get(currentItem) + ")<br/>";
            }
            items += "<hr><br/>";
            items += "</html>";
            JLabel currentOrderInfo = new JLabel(items);
            currentPanel.add(currentOrderInfo);
        }
        mainPanel = currentPanel;
        frame.add(mainPanel);
        frame.pack();
        frame.revalidate();
    }
}
