package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Price", schema = "main", catalog = "")
@IdClass(PriceEntityPK.class)
public class PriceEntity
{
    private Integer itemId;
    private Double price;
    private String date;

    @Id
    @Column(name = "item_id", nullable = true)
    public Integer getItemId()
    {
        return itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    @Id
    @Column(name = "price", nullable = true, precision = 0)
    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    @Id
    @Column(name = "date", nullable = true)
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
        PriceEntity that = (PriceEntity) o;
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
