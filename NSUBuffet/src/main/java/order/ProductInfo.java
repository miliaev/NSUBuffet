package order;

import java.io.Serializable;

/**
 * Created by Mike on 21.12.2017.
 */
public class ProductInfo implements Serializable
{
    private Double price;
    private Integer amount;

    public ProductInfo(Double price, Integer amount)
    {
        this.price = price;
        this.amount = amount;
    }

    public Double getPrice()
    {
        return price;
    }

    public Integer getAmount()
    {
        return amount;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }
}
