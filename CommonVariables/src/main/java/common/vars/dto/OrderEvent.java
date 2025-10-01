package common.vars.dto;

import java.time.LocalDateTime;

public class OrderEvent {
    private Long orderId;
    private Long userId;
    private LocalDateTime createdAt;
    private String productId;


    public OrderEvent(Long orderId, Long userId, LocalDateTime createdAt,
                      String productId) {
        this.orderId = orderId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.productId = productId;
    }

    public OrderEvent() {}

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
