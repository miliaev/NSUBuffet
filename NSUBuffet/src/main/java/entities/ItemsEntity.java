package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Items", schema = "main", catalog = "")
public class ItemsEntity
{
    private Integer itemId;
    private Integer categoryId;
    private String name;
    private Double currentPrice;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = true)
    public Integer getItemId()
    {
        return itemId;
    }

    public void setItemId(Integer itemId)
    {
        this.itemId = itemId;
    }

    @Basic
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

    @Basic
    @Column(name = "current_price", nullable = true, precision = 0)
    public Double getCurrentPrice()
    {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice)
    {
        this.currentPrice = currentPrice;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemsEntity that = (ItemsEntity) o;
        return Objects.equals(itemId, that.itemId) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(currentPrice, that.currentPrice);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(itemId, categoryId, name, currentPrice);
    }
}
