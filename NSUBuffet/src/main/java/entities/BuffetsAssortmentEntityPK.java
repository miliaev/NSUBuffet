package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class BuffetsAssortmentEntityPK implements Serializable {
    private Short buffetId;
    private Short itemId;

    @Column(name = "buffet_id", nullable = true)
    @Id
    public Short getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Short buffetId) {
        this.buffetId = buffetId;
    }

    @Column(name = "item_id", nullable = true)
    @Id
    public Short getItemId() {
        return itemId;
    }

    public void setItemId(Short itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuffetsAssortmentEntityPK that = (BuffetsAssortmentEntityPK) o;

        if (buffetId != null ? !buffetId.equals(that.buffetId) : that.buffetId != null) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buffetId != null ? buffetId.hashCode() : 0;
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        return result;
    }
}
