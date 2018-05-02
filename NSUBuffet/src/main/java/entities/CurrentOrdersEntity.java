package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "CurrentOrders", schema = "main", catalog = "")
public class CurrentOrdersEntity
{
    private Integer orderId;
    private Integer buffetId;
    private String date;
    private String status;

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

    @Basic
    @Column(name = "buffet_id", nullable = true)
    public Integer getBuffetId()
    {
        return buffetId;
    }

    public void setBuffetId(Integer buffetId)
    {
        this.buffetId = buffetId;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Basic
    @Column(name = "status", nullable = true, length = -1)
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentOrdersEntity that = (CurrentOrdersEntity) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(buffetId, that.buffetId) &&
                Objects.equals(date, that.date) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(orderId, buffetId, date, status);
    }
}
