package entities;

import javax.persistence.*;

@Entity
@Table(name = "Current Orders", schema = "main", catalog = "")
public class CurrentOrdersEntity {
    private Short orderId;
    private String date;

    @Id
    @Column(name = "order_id", nullable = true)
    public Short getOrderId() {
        return orderId;
    }

    public void setOrderId(Short orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "date", nullable = true)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentOrdersEntity that = (CurrentOrdersEntity) o;

        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId != null ? orderId.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
