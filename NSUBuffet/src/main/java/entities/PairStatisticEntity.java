package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PairStatistic", schema = "main", catalog = "")
@IdClass(PairStatisticEntityPK.class)
public class PairStatisticEntity
{
    private Integer item1Id;
    private Integer item2Id;
    private Integer count;

    @Id
    @Column(name = "item1_id", nullable = true)
    public Integer getItem1Id()
    {
        return item1Id;
    }

    public void setItem1Id(Integer item1Id)
    {
        this.item1Id = item1Id;
    }

    @Id
    @Column(name = "item2_id", nullable = true)
    public Integer getItem2Id()
    {
        return item2Id;
    }

    public void setItem2Id(Integer item2Id)
    {
        this.item2Id = item2Id;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairStatisticEntity that = (PairStatisticEntity) o;
        return Objects.equals(item1Id, that.item1Id) &&
                Objects.equals(item2Id, that.item2Id) &&
                Objects.equals(count, that.count);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(item1Id, item2Id, count);
    }
}
