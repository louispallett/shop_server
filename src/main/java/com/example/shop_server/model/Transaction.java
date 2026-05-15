package com.example.shop_server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transactions", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Transaction extends Base {
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TransactionItem> items = new ArrayList<>();

    public enum PaymentMethod {
        CASH,
        CARD
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column
    private int cardDigits;

    public Transaction(PaymentMethod paymentMethod, int cardDigits) {
        this.paymentMethod = paymentMethod;
        this.cardDigits = cardDigits;
    }

    public void addItem(TransactionItem item) {
        items.add(item);
        item.setTransaction(this);
    }

    public void removeItem(TransactionItem item) {
        items.remove(item);
        item.setTransaction(null);
    }
}