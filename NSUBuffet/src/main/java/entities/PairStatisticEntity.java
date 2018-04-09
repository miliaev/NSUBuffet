package entities;

import javax.persistence.*;

@Entity
@Table(name = "Pair statistic", schema = "main", catalog = "")
@IdClass(PairStatisticEntityPK.class)
public class PairStatisticEntity {
    private Short item1Id;
    private Short item2Id;
    private Short count;

    @Id
    @Column(name = "item1_id", nullable = true)
    public Short getItem1Id() {
        return item1Id;
    }

    public void setItem1Id(Short item1Id) {
        this.item1Id = item1Id;
    }

    @Id
    @Column(name = "item2_id", nullable = true)
    public Short getItem2Id() {
        return item2Id;
    }

    public void setItem2Id(Short item2Id) {
        this.item2Id = item2Id;
    }

    @Basic
    @Column(name = "count", nullable = true)
    public Short getCount() {
        return count;
    }

    public void setCount(Short count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PairStatisticEntity that = (PairStatisticEntity) o;

        if (item1Id != null ? !item1Id.equals(that.item1Id) : that.item1Id != null) return false;
        if (item2Id != null ? !item2Id.equals(that.item2Id) : that.item2Id != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = item1Id != null ? item1Id.hashCode() : 0;
        result = 31 * result + (item2Id != null ? item2Id.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        return result;
    }
}
