package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Buffet", schema = "main", catalog = "")
public class BuffetEntity
{
    private Integer buffetId;
    private String location;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buffet_id", nullable = true)
    public Integer getBuffetId()
    {
        return buffetId;
    }

    public void setBuffetId(Integer buffetId)
    {
        this.buffetId = buffetId;
    }

    @Basic
    @Column(name = "Location", nullable = true)
    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuffetEntity that = (BuffetEntity) o;
        return Objects.equals(buffetId, that.buffetId) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(buffetId, location);
    }
}
