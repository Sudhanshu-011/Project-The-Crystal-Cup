package com.cup.controller;

import com.cup.entities.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/category")
public interface CategoryController {

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody Map<String, String> requestMap);

    // Here, filterValue will be used to distinguished between the admin and user that if admin is accessing this API
    // the admin should get a List of all the categories weather it contains any item or not but if a user is accessing
    // this API they must only get the categories that contain an item, for user, we pass it true for admin, we don't provide it.
    @GetMapping("/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping("/update")
    ResponseEntity<String> updateCategory(@RequestBody Map<String, String> requestMap);
}
