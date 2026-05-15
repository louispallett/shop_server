package com.example.shop_server.controller;

import com.example.shop_server.model.Product;
import com.example.shop_server.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/get-all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/get")
    public ResponseEntity<Product> getProduct(@RequestParam Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-by-name")
    public ResponseEntity<Product> getProductByName(@RequestParam String name) {
        return productRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product incomingProduct) {

        Product savedProduct = productRepository.save(incomingProduct);
        return ResponseEntity.ok(savedProduct);
    }

    // TODO
    // We want to introduce a method which reduces stock, so that when a transaction occurs including one of those products, the stock is automatically updated.
    // Now, we could have:
    //  -> A unique method here to reduce stock
    //  -> A method inside transaction controller to do this
    //  -> Or a generic update method which could update anything, but the client is designed to autosend it with the new stock.
}