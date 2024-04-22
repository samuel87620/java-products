package com.example.demo.dto;

import java.beans.ConstructorProperties;

/**
 * 訂單資料的資料傳輸對象（DTO）。
 */
public class OrderDto {
    private Long id;
    private Long productId;
    private Integer quantity;
    private String status;

    // Spring 框架所需的無參數構造函數
    public OrderDto() {
    }

    // 所有字段都包含的構造函數
    @SuppressWarnings("unused")
    @ConstructorProperties({"id", "productId", "quantity", "status"})
    public OrderDto(Long id, Long productId, Integer quantity, String status) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.status = status;
    }

    // getter 和 setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 用於日誌記錄和調試目的的 toString 方法
    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                '}';
    }
}
