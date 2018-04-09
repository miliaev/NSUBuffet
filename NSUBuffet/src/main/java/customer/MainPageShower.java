package customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Mike on 21.12.2017.
 */
public class MainPageShower
{
    private static final int MAKE_ORDER = 1;
    private static final int VALIDATE_ORDER = 2;

    private JFrame frame = new JFrame();
    private MainPageController mainPageController;

    public MainPageShower(MainPageController mainPageController)
    {
        this.mainPageController = mainPageController;
    }

    public void go()
    {
        JPanel mainPanel = new JPanel();
        JButton makeOrder = new JButton("Новый заказ");
        JButton validateOrder  = new JButton("Подтвердить заказ");
        mainPanel.add(makeOrder);
        mainPanel.add(validateOrder);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        addMakeOrderListener(makeOrder);
        addValidateOrderListener(validateOrder);
    }

    public void addMakeOrderListener(JButton makeOrder)
    {
        makeOrder.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    frame.setVisible(false);
                    mainPageController.onButtonClick(MAKE_ORDER);
                }
            }
        });
    }

    public void addValidateOrderListener(JButton validateOrder)
    {
        validateOrder.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    frame.setVisible(false);
                    mainPageController.onButtonClick(VALIDATE_ORDER);
                }
            }
        });
    }

}
