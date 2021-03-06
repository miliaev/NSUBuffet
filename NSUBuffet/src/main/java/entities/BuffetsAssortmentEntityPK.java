package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class BuffetsAssortmentEntityPK implements Serializable
{
    private Integer buffetId;
    private Integer itemId;

    @Column(name = "buffet_id", nullable = true)
    @Id
    public Integer getBuffetId()
    {
        return buffetId;
    }

    public void setBuffetId(Integer buffetId)
    {
        this.buffetId = buffetId;
    }

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuffetsAssortmentEntityPK that = (BuffetsAssortmentEntityPK) o;
        return Objects.equals(buffetId, that.buffetId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(buffetId, itemId);
    }
}
