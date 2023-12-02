package com.cup.controller;

import com.cup.Wrapper.ProductWrapper;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductController {

    @PostMapping("/add")
    public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<ProductWrapper>> getAllProduct();

    @PostMapping("/update")
    public ResponseEntity<String> updateProduct(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PutMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap);

    @GetMapping("/getByCategory/{id}")
    public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductWrapper> getById(@PathVariable Integer id);
}
