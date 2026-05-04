package com.example.shop_server.dto;

import java.util.List;

public class TransactionRequest {
    public String paymentMethod;
    public int cardDigits;
    public List<TransactionItemRequest> items;
}