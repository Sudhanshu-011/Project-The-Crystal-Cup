package com.cup.repositories;

import com.cup.Wrapper.ProductWrapper;
import com.cup.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);
}
