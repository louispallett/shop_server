package com.example.shop_server.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "transactions", schema = "public")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date dateCreated;

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

    public Transaction() {}

    public Transaction(PaymentMethod paymentMethod, int cardDigits) {
        this.paymentMethod = paymentMethod;
        this.cardDigits = cardDigits;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<TransactionItem> getItems() {
        return items;
    }

    public void addItem(TransactionItem item) {
        items.add(item);
        item.setTransaction(this);
    }

    public void removeItem(TransactionItem item) {
        items.remove(item);
        item.setTransaction(null);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @PrePersist
    protected void onCreate() {
        this.dateCreated = new Date();
    }
}