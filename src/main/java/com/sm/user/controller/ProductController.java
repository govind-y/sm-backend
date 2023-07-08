package com.sm.user.controller;

import com.sm.user.document.Product;
import com.sm.user.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*",exposedHeaders = "*",allowedHeaders = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product/lookup")
    public ResponseEntity<List<Product>> getProductDetailsByLotNo(){
        return ResponseEntity.ok(productRepository.findAll());
    }

}
