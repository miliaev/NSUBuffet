package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PriceEntityPK implements Serializable
{
    private Integer itemId;
    private Double price;
    private String date;

    @Column(name = "item_id", nullable = true)
    @Id
    public Integer getItemId()
    {
        return itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    @Column(name = "price", nullable = true, precision = 0)
    @Id
    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    @Column(name = "date", nullable = true)
    @Id
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceEntityPK that = (PriceEntityPK) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(price, that.price) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(itemId, price, date);
    }
}
