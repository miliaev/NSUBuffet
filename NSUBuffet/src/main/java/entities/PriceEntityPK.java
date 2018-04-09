package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class PriceEntityPK implements Serializable {
    private Short itemId;
    private Double price;
    private String date;

    @Column(name = "item_id", nullable = true)
    @Id
    public Short getItemId() {
        return itemId;
    }

    public void setItemId(Short itemId) {
        this.itemId = itemId;
    }

    @Column(name = "price", nullable = true, precision = 0)
    @Id
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "date", nullable = true)
    @Id
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

        PriceEntityPK that = (PriceEntityPK) o;

        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
