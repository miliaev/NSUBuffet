package seller;

import order.Order;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miketurch on 22.12.17.
 */

public class SellerShower
{
    private SellerShowerController sellerShowerController;
    private JFrame frame = new JFrame("Текущие заказы");
    JPanel mainPanel = new JPanel();
    JScrollPane scrollPane = new JScrollPane();

    public SellerShower(SellerShowerController sellerShowerController)
    {
        this.sellerShowerController = sellerShowerController;
    }

    public void go()
    {
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateView(ArrayList<Order> orders)
    {
        frame.remove(scrollPane);

        int totalItems = 0;
        for(Order currentOrder: orders)
        {
            totalItems += currentOrder.getOrderItems().size();
        }

        String[] columnNames = {
                "Текущие",
                "заказы"
        };

        String[][] data = new String[orders.size() * 3 + totalItems + orders.size() - 1][2];

        int currentOrder = 0;
        int i = 0;
        while(i < orders.size() * 3 + totalItems + orders.size() - 1)
        {
            data[i][0] = "Order ID:";
            data[i++][1] = String.valueOf(orders.get(currentOrder).getId());

            data[i][0] = "Время:";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm");
            data[i++][1] = dateFormat.format(orders.get(currentOrder).getTime());

            data[i][0] = "Товар:";
            data[i++][1] = "Количество:";

            for(Map.Entry<String, Integer> currentItem: orders.get(currentOrder).getOrderItems().entrySet())
            {
                data[i][0] = currentItem.getKey();
                data[i++][1] = String.valueOf(currentItem.getValue());
            }

            currentOrder++;
            i++;
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.setTableHeader(null);
        scrollPane = new JScrollPane(table);

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.revalidate();
    }
}
