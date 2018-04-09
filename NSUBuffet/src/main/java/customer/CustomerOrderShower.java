package customer;

import order.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Mike on 26.12.2017.
 */
public class CustomerOrderShower
{
    private CustomerOrderShowerController customerOrderShowerController;
    private JFrame frame = new JFrame("Подтвердить заказ");
    JTextField myID;
    JPanel mainPanel;

    public CustomerOrderShower(CustomerOrderShowerController customerOrderShowerController)
    {
        this.customerOrderShowerController = customerOrderShowerController;

        mainPanel = new JPanel();
        myID = new JTextField("Введите Ваш ID заказа");
        mainPanel.add(myID);
        JButton showButton = new JButton("Посмотреть заказ");
        addShowButtonListener(showButton);
        mainPanel.add(showButton);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void addShowButtonListener(JButton showButton)
    {
        showButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    try
                    {
                        Order order = customerOrderShowerController.onShowButtonClick(Integer.parseInt(myID.getText()));
                        if(order != null)
                        {
                            updateView(order);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(frame, new String[]{"Такого заказа нет, проверьте ID заказа"},
                                    "Проверьте ID", JOptionPane.INFORMATION_MESSAGE, null);
                        }
                    }
                    catch (IOException e1)
                    {
                        JOptionPane.showMessageDialog(frame, new String[]{"Проверьте ID заказа"},
                                "Неверный ID", JOptionPane.INFORMATION_MESSAGE, null);
                    }
                    catch (ClassNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public void updateView(Order order)
    {
        frame.remove(mainPanel);

        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new BoxLayout(currentPanel ,BoxLayout.Y_AXIS));

        String items = "<html> ID заказа: " + order.getId() + "<br/>";
        items += order.getTime() + "<br/>";
        for(String currentItem: order.getOrderItems().keySet())
        {
            items += currentItem + " (количество: " + order.getOrderItems().get(currentItem) + ")<br/>";
        }
        items += "<hr><br/>";
        items += "</html>";

        JLabel currentOrderInfo = new JLabel(items);
        currentPanel.add(currentOrderInfo);
        mainPanel = currentPanel;

        JButton validateButton = new JButton("Подтвердить получение заказа");
        addValidateButtonListener(validateButton, order.getId());
        mainPanel.add(validateButton);
        frame.add(mainPanel);

        frame.revalidate();
    }

    public void addValidateButtonListener(JButton validateButton, Integer ID)
    {
        validateButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    try
                    {
                        customerOrderShowerController.onValidateButtonClick(ID);
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }
}

