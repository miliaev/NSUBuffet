package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Orders", schema = "main", catalog = "")
@IdClass(OrdersEntityPK.class)
public class OrdersEntity
{
    private Integer orderId;
    private Integer itemId;
    private Integer amount;
    private Double price;

    @Id
    @Column(name = "order_id", nullable = true)
    public Integer getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Integer orderId)
    {
        this.orderId = orderId;
    }

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

    @Basic
    @Column(name = "amount", nullable = true)
    public Integer getAmount()
    {
        return amount;
    }

    public void setAmount(Integer amount)
    {
        this.amount = amount;
    }

    @Basic
    @Column(name = "price", nullable = true, precision = 0)
    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity that = (OrdersEntity) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(orderId, itemId, amount, price);
    }
}
