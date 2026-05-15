package com.example.shop_server.dto;

import com.example.shop_server.model.User;

import java.util.List;

public class TransactionRequest {
    public String paymentMethod;
    public int cardDigits;
    public List<TransactionItemRequest> items;
    public User user;
}