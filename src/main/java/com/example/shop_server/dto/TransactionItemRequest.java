package com.example.shop_server.dto;

import java.math.BigDecimal;

public class TransactionItemRequest {
    public Long productId;
    public int quantity;
    public BigDecimal price;
}