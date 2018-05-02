package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class PairStatisticEntityPK implements Serializable
{
    private Integer item1Id;
    private Integer item2Id;

    @Column(name = "item1_id", nullable = true)
    @Id
    public Integer getItem1Id()
    {
        return item1Id;
    }

    public void setItem1Id(Integer item1Id)
    {
        this.item1Id = item1Id;
    }

    @Column(name = "item2_id", nullable = true)
    @Id
    public Integer getItem2Id()
    {
        return item2Id;
    }

    public void setItem2Id(Integer item2Id)
    {
        this.item2Id = item2Id;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairStatisticEntityPK that = (PairStatisticEntityPK) o;
        return Objects.equals(item1Id, that.item1Id) &&
                Objects.equals(item2Id, that.item2Id);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(item1Id, item2Id);
    }
}
