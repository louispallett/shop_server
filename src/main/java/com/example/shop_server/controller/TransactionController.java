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

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/get-all")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/get")
    public ResponseEntity<Transaction> getTransaction(@RequestParam Long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
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

        TransactionResponse response = new TransactionResponse();
        response.id = saved.getId();
        response.dateCreated = saved.getDateCreated();
        response.paymentMethod = saved.getPaymentMethod().name();

        response.items = saved.getItems().stream().map(i -> {
            TransactionItemResponse r = new TransactionItemResponse();
            r.productId = i.getProduct().getId();
            r.quantity = i.getQuantity();
            r.price = i.getPrice();
            return r;
        }).toList();

        return ResponseEntity.ok(response);
    }
}