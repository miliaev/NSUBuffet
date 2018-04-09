package entities;

import javax.persistence.*;

@Entity
@Table(name = "Item statistic", schema = "main", catalog = "")
public class ItemStatisticEntity {
    private Short itemId;
    private Short count;
    private Short buffetId;
    private String date;

    @Id
    @Column(name = "item_id", nullable = true)
    public Short getItemId() {
        return itemId;
    }

    public void setItemId(Short itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    @Basic
    @Column(name = "buffet_id", nullable = true)
    public Short getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Short buffetId) {
        this.buffetId = buffetId;
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

        ItemStatisticEntity that = (ItemStatisticEntity) o;

        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (buffetId != null ? !buffetId.equals(that.buffetId) : that.buffetId != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (buffetId != null ? buffetId.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
