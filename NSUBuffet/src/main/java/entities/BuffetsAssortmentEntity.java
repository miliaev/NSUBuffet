package entities;

import javax.persistence.*;

@Entity
@Table(name = "Buffets assortment", schema = "main", catalog = "")
@IdClass(BuffetsAssortmentEntityPK.class)
public class BuffetsAssortmentEntity {
    private Short buffetId;
    private Short itemId;
    private Short amount;

    @Id
    @Column(name = "buffet_id", nullable = true)
    public Short getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Short buffetId) {
        this.buffetId = buffetId;
    }

    @Id
    @Column(name = "item_id", nullable = true)
    public Short getItemId() {
        return itemId;
    }

    public void setItemId(Short itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "amount", nullable = true)
    public Short getAmount() {
        return amount;
    }

    public void setAmount(Short amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuffetsAssortmentEntity that = (BuffetsAssortmentEntity) o;

        if (buffetId != null ? !buffetId.equals(that.buffetId) : that.buffetId != null) return false;
        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buffetId != null ? buffetId.hashCode() : 0;
        result = 31 * result + (itemId != null ? itemId.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }
}
