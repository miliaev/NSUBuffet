package customer;

import order.Order;
import order.ProductInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Mike on 21.12.2017.
 */
public class OrderShower
{
    private JFrame frame = new JFrame();
    private JPanel orderPanel = new JPanel();

    private ArrayList<JCheckBox> checkBoxes = new ArrayList<>();
    private ArrayList<String> products = new ArrayList<>();
    private ArrayList<ProductInfo> productInfos = new ArrayList<>();
    private ArrayList<JTextField> howManyFields = new ArrayList<>();
    JTextField hours;
    JTextField minutes;

    private int selectedBuffer;

    private ButtonGroup buffets = new ButtonGroup();
    ArrayList<JRadioButton> radioButtons = new ArrayList<>();

    private OrderShowerController orderShowerController;

    private static final String FIRST_BUFFET = "Первый буффет";
    private static final String SECOND_BUFFET = "Второй буффет";

    public OrderShower(OrderShowerController orderShowerController)
    {
        this.orderShowerController = orderShowerController;

        addRadioButtons();

        JButton next =  new JButton("Далее");
        addNextListener(next);
        orderPanel.add(next);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(orderPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void addNextListener(JButton next)
    {
        next.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    for (int i=0; i < radioButtons.size(); i++)
                    {
                        if(radioButtons.get(i).isSelected())
                        {
                            selectedBuffer = i + 1;
                            try
                            {
                                showCurrentBuffetAssortment(orderShowerController.getBuffetAssortment(selectedBuffer));
                            }
                            catch (IOException e1)
                            {
                                e1.printStackTrace();
                            }
                            catch (ClassNotFoundException e1)
                            {
                                e1.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    public void showCurrentBuffetAssortment( HashMap<String, ProductInfo> currentBuffetAssortment)
    {
        frame.remove(orderPanel);
        JPanel currentBuffetPanel = new JPanel();
        currentBuffetPanel.setLayout(new GridLayout(currentBuffetAssortment.size() + 2,4));

        JLabel product = new JLabel("Выберите товар:");
        currentBuffetPanel.add(product);
        JLabel amount = new JLabel("Количество:");
        currentBuffetPanel.add(amount);
        JLabel price = new JLabel("Цена:");
        currentBuffetPanel.add(price);
        JLabel howMany = new JLabel("Сколько:");
        currentBuffetPanel.add(howMany);

        for(String currentItem: currentBuffetAssortment.keySet())
        {
            JCheckBox currentCheckBox = new JCheckBox(currentItem);
            checkBoxes.add(currentCheckBox);
            currentBuffetPanel.add(currentCheckBox);
            products.add(currentItem);

            JLabel currentAmount = new JLabel(currentBuffetAssortment.get(currentItem).getAmount().toString());
            currentBuffetPanel.add(currentAmount);
            productInfos.add(currentBuffetAssortment.get(currentItem));

            JLabel currentPrice = new JLabel(currentBuffetAssortment.get(currentItem).getPrice().toString());
            currentBuffetPanel.add(currentPrice);

            JTextField howManyThisItem = new JTextField(0);
            currentBuffetPanel.add(howManyThisItem);
            howManyFields.add(howManyThisItem);
        }

        JLabel chooseTime = new JLabel("Укажите время:");
        currentBuffetPanel.add(chooseTime);

        hours = new JTextField("Часы");
        minutes = new JTextField("Минуты");
        currentBuffetPanel.add(hours);
        currentBuffetPanel.add(minutes);

        JButton makeOrder = new JButton("Сделать заказ");
        addMakeOrderListener(makeOrder);
        currentBuffetPanel.add(makeOrder);
        frame.add(currentBuffetPanel);
        frame.revalidate();
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
                    boolean buySomething = false;
                    Order order = new Order();
                    boolean breaked = checkTime(order);
                    if(!breaked)
                    {
                        for (int i = 0; i < howManyFields.size(); i++)
                        {
                            if (checkBoxes.get(i).isSelected())
                            {
                                JTextField currentField = howManyFields.get(i);
                                try
                                {
                                    Integer currentHowMany = Integer.parseInt(currentField.getText());
                                    if (currentHowMany > productInfos.get(i).getAmount() || currentHowMany < 1)
                                    {
                                        JOptionPane.showMessageDialog(frame, new String[]{"Нет товара " + products.get(i) + " в таком количестве"},
                                                "Слишком много!", JOptionPane.INFORMATION_MESSAGE, null);
                                        buySomething = false;
                                        breaked = true;
                                        break;
                                    } else
                                    {
                                        buySomething = true;
                                        order.setPrice(order.getPrice() + productInfos.get(i).getPrice() * currentHowMany);
                                        order.getOrderItems().put(products.get(i), currentHowMany);
                                        order.getItemsPrice().put(products.get(i), productInfos.get(i).getPrice() * currentHowMany);
                                    }
                                } catch (NumberFormatException ex)
                                {
                                    breaked = true;
                                    JOptionPane.showMessageDialog(frame, new String[]{"Вводить можно только цифры!"},
                                            "Проверка данных", JOptionPane.INFORMATION_MESSAGE, null);
                                    break;
                                }
                            }
                        }
                    }
                    if(!breaked)
                    {
                        if (!buySomething)
                        {
                            JOptionPane.showMessageDialog(frame, new String[]{"Вы ничего не выбрали :("}, "Купите хоть что-нибудь, пожалуйста!", JOptionPane.INFORMATION_MESSAGE, null);
                        } else
                        {
                            frame.setVisible(false);
                            order.setBuffetID(selectedBuffer);
                            orderShowerController.onPayButtonClick(order);
                        }
                    }
                }
            }
        });
    }

    public boolean checkTime(Order order)
    {
        try
        {
            if (Integer.parseInt(hours.getText()) > 8 && Integer.parseInt(hours.getText()) < 23 &&
                    Integer.parseInt(minutes.getText()) >= 0 && Integer.parseInt(minutes.getText()) <= 59)
            {
                Date currentDate = new Date();

                int Day;
                int Month;
                int Year;
                Calendar calendar = Calendar.getInstance();
                Day = calendar.get(Calendar.DAY_OF_MONTH);
                Month = calendar.get(Calendar.MONTH) + 1;
                Year = calendar.get(Calendar.YEAR);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm");
                String strDate = Day + "-" + Month + "-" + Year + " " + hours.getText() + ":" + minutes.getText();
                try
                {
                    Date newDate = dateFormat.parse(strDate);
                    if(newDate.getTime() >= currentDate.getTime())
                    {
                        order.setTime(newDate);
                        return false;
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(frame, new String[]{"Введите коректное время"},
                                "Неверно введно время", JOptionPane.INFORMATION_MESSAGE, null);
                        return true;
                    }
                } catch (ParseException e)
                {
                    e.printStackTrace();
                    return true;
                }

            }
            else
            {
                JOptionPane.showMessageDialog(frame, new String[]{"Введите коректное время"},
                        "Неверно введно время", JOptionPane.INFORMATION_MESSAGE, null);
                return true;
            }
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(frame, new String[]{"Введите коректное время"},
                    "Неверно введно время", JOptionPane.INFORMATION_MESSAGE, null);
            return true;
        }
    }


    public void addRadioButtons()
    {
        orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));

        JRadioButton firstBuffet = new JRadioButton();
        firstBuffet.setText(FIRST_BUFFET);
        radioButtons.add(firstBuffet);

        JRadioButton secondBuffet = new JRadioButton();
        secondBuffet.setText(SECOND_BUFFET);
        radioButtons.add(secondBuffet);

        buffets.add(firstBuffet);
        buffets.add(secondBuffet);
        buffets.getSelection();

        orderPanel.add(firstBuffet);
        orderPanel.add(secondBuffet);
    }

}
