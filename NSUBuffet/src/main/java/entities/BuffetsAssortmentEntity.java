package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BuffetsAssortment", schema = "main", catalog = "")
@IdClass(BuffetsAssortmentEntityPK.class)
public class BuffetsAssortmentEntity {
    private Integer buffetId;
    private Integer itemId;
    private Integer amount;

    @Id
    @Column(name = "buffet_id", nullable = true)
    public Integer getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Integer buffetId) {
        this.buffetId = buffetId;
    }

    @Id
    @Column(name = "item_id", nullable = true)
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "amount", nullable = true)
    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuffetsAssortmentEntity that = (BuffetsAssortmentEntity) o;
        return Objects.equals(buffetId, that.buffetId) &&
                Objects.equals(itemId, that.itemId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {

        return Objects.hash(buffetId, itemId, amount);
    }
}
