package com.example.shop_server.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionResponse {
    public Long id;
    public Date dateCreated;
    public String paymentMethod;
    public List<TransactionItemResponse> items;
    public BigDecimal totalCost;
}
