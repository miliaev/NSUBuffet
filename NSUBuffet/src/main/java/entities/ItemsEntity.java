package entities;

import javax.persistence.*;

@Entity
@Table(name = "Items", schema = "main", catalog = "")
public class ItemsEntity {
    private Short itemId;
    private Short categoryId;
    private String name;
    private Double currentPrice;

    @Id
    @Column(name = "item_id", nullable = true)
    public Short getItemId() {
        return itemId;
    }

    public void setItemId(Short itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "category_id", nullable = true)
    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "name", nullable = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "current_price", nullable = true, precision = 0)
    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsEntity that = (ItemsEntity) o;

        if (itemId != null ? !itemId.equals(that.itemId) : that.itemId != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (currentPrice != null ? !currentPrice.equals(that.currentPrice) : that.currentPrice != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = itemId != null ? itemId.hashCode() : 0;
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (currentPrice != null ? currentPrice.hashCode() : 0);
        return result;
    }
}
