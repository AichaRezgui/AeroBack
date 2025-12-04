package com.Aero.Beauty.dto;

import com.Aero.Beauty.Entities.ShippingAddress;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private List<OrderItemDTO> items;
    private double total;
    private String status;
    private LocalDate date;
    private ShippingAddress shippingAddress;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }
}
