package com.cup.service;

import com.cup.Wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import javax.xml.transform.sax.SAXResult;
import java.util.List;
import java.util.Map;

public interface ProductService {

    ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    ResponseEntity<String> deleteProduct(int id);

    ResponseEntity<String> updateProductStatus(Map<String, String> requstMap);

    ResponseEntity<List<ProductWrapper>> getByCategory(int id);

    ResponseEntity<ProductWrapper> getById(int id);
}
