package com.example.shop_server.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class TransactionResponse {
    public Long id;
    public Instant dateCreated;
    public String paymentMethod;
    public List<TransactionItemResponse> items;
    public BigDecimal totalCost;
}
