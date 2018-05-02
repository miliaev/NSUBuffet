package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ItemStatisticEntityPK implements Serializable
{
    private Integer itemId;
    private Integer count;
    private Integer buffetId;
    private String date;

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

    @Column(name = "count", nullable = true)
    @Id
    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

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

    @Column(name = "date", nullable = true)
    @Id
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
        ItemStatisticEntityPK that = (ItemStatisticEntityPK) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(count, that.count) &&
                Objects.equals(buffetId, that.buffetId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(itemId, count, buffetId, date);
    }
}
