package entities;

import javax.persistence.*;

@Entity
@Table(name = "Buffet", schema = "main", catalog = "")
public class BuffetEntity {
    private Short buffetId;
    private String location;

    @Id
    @Column(name = "buffet_id", nullable = true)
    public Short getBuffetId() {
        return buffetId;
    }

    public void setBuffetId(Short buffetId) {
        this.buffetId = buffetId;
    }

    @Basic
    @Column(name = "Location", nullable = true)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BuffetEntity that = (BuffetEntity) o;

        if (buffetId != null ? !buffetId.equals(that.buffetId) : that.buffetId != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buffetId != null ? buffetId.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
