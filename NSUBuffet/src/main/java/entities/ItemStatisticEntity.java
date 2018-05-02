package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ItemStatistic", schema = "main", catalog = "")
@IdClass(ItemStatisticEntityPK.class)
public class ItemStatisticEntity
{
    private Integer itemId;
    private Integer count;
    private Integer buffetId;
    private String date;

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

    @Id
    @Column(name = "count", nullable = true)
    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    @Id
    @Column(name = "buffet_id", nullable = true)
    public Integer getBuffetId()
    {
        return buffetId;
    }

    public void setBuffetId(Integer buffetId)
    {
        this.buffetId = buffetId;
    }

    @Id
    @Column(name = "date", nullable = true)
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
        ItemStatisticEntity that = (ItemStatisticEntity) o;
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
