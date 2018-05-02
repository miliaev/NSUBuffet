package order;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class Order implements Serializable{
    private HashMap<String, Integer> orderItems = new HashMap<>();
    private HashMap<String, Double> itemsPrice = new HashMap<>();
    private int id;
    private Integer buffetID;
    private Date time;
    private double price;

    public HashMap<String, Integer> getOrderItems() {
        return orderItems;
    }

    public HashMap<String, Double> getItemsPrice()
    {
        return itemsPrice;
    }

    public Integer getBuffetID() {
        return buffetID;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public void setBuffetID(Integer buffetID)
    {
        this.buffetID = buffetID;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }
}
