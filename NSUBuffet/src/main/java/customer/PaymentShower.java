package customer;

import order.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Created by Mike on 21.12.2017.
 */
public class PaymentShower
{
    private JFrame frame = new JFrame();
    JPanel paymentPanel = new JPanel();
    JPanel idPanel = new JPanel();
    private PaymentShowerController paymentShowerController;
    private Order order;

    public PaymentShower(PaymentShowerController paymentShowerController, Order order)
    {
        this.paymentShowerController = paymentShowerController;
        this.order = order;

        JLabel totalPrice = new JLabel("Итоговая стоимость: " + Double.toString(order.getPrice()));
        paymentPanel.add(totalPrice);
        JButton payButton = new JButton("Оплатить");
        addPayButtonListener(payButton);
        paymentPanel.add(payButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(paymentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void addPayButtonListener(JButton payButton)
    {
        payButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    int currentID = 0;
                    try
                    {
                        currentID = paymentShowerController.addNewOrder(order);
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    showID(currentID);
                }
            }
        });
    }

    public void showID(Integer currentID)
    {
        frame.remove(paymentPanel);
        JLabel ID = new JLabel("ID Вашего заказа: " + currentID.toString()); //TODO установить нотификацию
        idPanel.add(ID);

        JButton backToMain = new JButton("Вернуться на главную");
        addBackToMainButtonListener(backToMain);
        idPanel.add(backToMain);

        frame.add(idPanel);
        frame.revalidate();
    }

    public void addBackToMainButtonListener(JButton backToMain)
    {
        backToMain.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    frame.remove(idPanel);
                    frame.setVisible(false);
                    paymentShowerController.getOrderShowerController().getMainPageShower().go();
                }
            }
        });
    }

}
