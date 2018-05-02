package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Category", schema = "main", catalog = "")
public class CategoryEntity
{
    private Integer categoryId;
    private String name;

    @Id
    @Column(name = "category_id", nullable = true)
    public Integer getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId)
    {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "name", nullable = true)
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(categoryId, name);
    }
}
