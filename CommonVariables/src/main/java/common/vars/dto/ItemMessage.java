package common.vars.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ItemMessage {
    private String productId;
    private String name;
    private String description;
    private Long amount;
    private String currency;
    private String category;
    private String brand;
    private Long available;
    private Long reserved;
    private List<String> tags;
    private Map<String, String> specifications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String index;
    private String storeId;

    public ItemMessage(String productId, String name, String description,
                       Long amount, String currency, String category,
                       String brand, Long available, Long reserved,
                       List<String> tags, Map<String, String> specifications, LocalDateTime createdAt,
                       LocalDateTime updatedAt, String index, String storeId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.brand = brand;
        this.available = available;
        this.reserved = reserved;
        this.tags = tags;
        this.specifications = specifications;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.index = index;
        this.storeId = storeId;
    }

    public ItemMessage() {}


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "ItemMessage{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", available=" + available +
                ", reserved=" + reserved +
                ", tags=" + tags +
                ", specifications=" + specifications +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", index='" + index + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }
}
