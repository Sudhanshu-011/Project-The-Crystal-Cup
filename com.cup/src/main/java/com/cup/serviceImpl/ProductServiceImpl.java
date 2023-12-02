package com.cup.serviceImpl;

import com.cup.Constants.CupConstants;
import com.cup.Security.JwtAuthenticationFilter;
import com.cup.Utils.CupUtils;
import com.cup.Wrapper.ProductWrapper;
import com.cup.entities.Category;
import com.cup.entities.Product;
import com.cup.repositories.ProductRepo;
import com.cup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private JwtAuthenticationFilter filter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (this.filter.isAdmin()) {
                if (this.validateRequestMap(requestMap, false)) {
                    this.productRepo.save(this.getProductFromMap(requestMap, false));
                    return CupUtils.getResponseEntity("Product added successfully.", HttpStatus.OK);
                }
                return CupUtils.getResponseEntity(CupConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRequestMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            }
            else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if (isAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        }
        else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(this.productRepo.getAllProduct(), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (this.filter.isAdmin()) {
                if (this.validateRequestMap(requestMap, true)) {
                    Optional<Product> optionalProduct = this.productRepo.findById(Integer.parseInt(requestMap.get("id")));
                    if (optionalProduct.isPresent()) {
                        Product product = this.getProductFromMap(requestMap, true);
                        product.setStatus(requestMap.get("status"));
                        this.productRepo.save(product);
                        return CupUtils.getResponseEntity("Product updated successfully.", HttpStatus.OK);
                    }
                    else return CupUtils.getResponseEntity("Product id does not exist", HttpStatus.OK);
                }
                else return CupUtils.getResponseEntity(CupConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            else return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(int id) {
        try {
            if (this.filter.isAdmin()) {
                Optional<Product> optionalProduct = this.productRepo.findById(id);
                if (optionalProduct.isPresent()) {
                    this.productRepo.deleteById(id);
                    return CupUtils.getResponseEntity("Product deleted successfully.", HttpStatus.OK);
                }
                else return CupUtils.getResponseEntity("Product id does not exist.", HttpStatus.OK);
            }
            else return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try {
            if (this.filter.isAdmin()) {
                Product product = this.productRepo.findById(Integer.parseInt(requestMap.get("id"))).orElse(null);
                if (product != null) {
                    product.setStatus(requestMap.get("status"));
                    this.productRepo.save(product);
                    return CupUtils.getResponseEntity("Product status updated successfully.", HttpStatus.OK);
                }
                else return CupUtils.getResponseEntity("Product id does not exist.", HttpStatus.OK);
            }
            else return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(int id) {
        try {
            return new ResponseEntity<>(this.productRepo.getProductByCategory(id), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getById(int id) {
        try {
            Product product = this.productRepo.findById(id).orElse(null);
            if (product != null) {
                ProductWrapper productWrapper = new ProductWrapper(product.getId(), product.getName(), product.getDescription(), product.getPrice());
                return new ResponseEntity<>(productWrapper, HttpStatus.OK);
            }
            else return new ResponseEntity<>(new ProductWrapper(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
