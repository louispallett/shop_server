package com.example.shop_server.controller;

import com.example.shop_server.dto.TransactionItemRequest;
import com.example.shop_server.dto.TransactionItemResponse;
import com.example.shop_server.dto.TransactionRequest;
import com.example.shop_server.dto.TransactionResponse;
import com.example.shop_server.model.Product;
import com.example.shop_server.model.Transaction;
import com.example.shop_server.model.TransactionItem;
import com.example.shop_server.repository.ProductRepository;
import com.example.shop_server.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.id = transaction.getId();
        response.dateCreated = transaction.getDateCreated();
        response.paymentMethod = transaction.getPaymentMethod().name();

        response.items = transaction.getItems().stream().map(i -> {
            TransactionItemResponse r = new TransactionItemResponse();
            r.productId = i.getProduct().getId();
            r.quantity = i.getQuantity();
            r.price = i.getPrice();
            return r;
        }).toList();

        response.totalCost = transaction.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return response;
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> responses = transactionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/get")
    public ResponseEntity<TransactionResponse> getTransaction(@RequestParam Long id) {
        return transactionRepository.findById(id)
                .map(t -> ResponseEntity.ok(mapToResponse(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = new Transaction(
                Transaction.PaymentMethod.valueOf(request.paymentMethod),
                request.cardDigits
        );

        for (TransactionItemRequest itemReq : request.items) {
            Product product = productRepository.findById(itemReq.productId)
                    .orElseThrow();

            TransactionItem item = new TransactionItem(
                    product,
                    itemReq.quantity,
                    itemReq.price
            );

            transaction.addItem(item);
        }

        Transaction saved = transactionRepository.save(transaction);

        return ResponseEntity.ok(mapToResponse(saved));
    }
}