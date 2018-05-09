package server.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerShower
{
    private JFrame frame = new JFrame();

    public ServerShower()
    {
        JPanel mainPanel = new JPanel();
        JButton itemStatisticButton = new JButton("Посмотреть статистику товаров");
        JButton pairStatisticButton  = new JButton("Посмотреть парную статистику");
        JButton categoryButton = new JButton("Посмотреть/добавить категорию");
        mainPanel.add(itemStatisticButton);
        mainPanel.add(pairStatisticButton);
        mainPanel.add(categoryButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        addItemStatisticButton(itemStatisticButton);
        addCategoryButton(categoryButton);
    }

    public void addItemStatisticButton(JButton itemStatisticButton)
    {
        itemStatisticButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    //frame.setVisible(false);
                    new ItemStatisticShower();
                }
            }
        });
    }

    public void addCategoryButton(JButton itemStatisticButton)
    {
        itemStatisticButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    new CategoryShower();
                }
            }
        });
    }

}
