package com.example.shop_server.controller;

import com.example.shop_server.model.Category;
import com.example.shop_server.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/get-all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/get")
    public ResponseEntity<Category> getCategory(@RequestParam Long id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/get-by-name")
    public ResponseEntity<Category> getCategoryByName(@RequestParam String name) {
        return categoryRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody Category incomingCategory) {
        // Check if category already exists
        if (categoryRepository.findByName(incomingCategory.getName()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("Category with name already exists");
        }

        Category savedCategory = categoryRepository.save(incomingCategory);
        return ResponseEntity.ok(savedCategory);
    }
}