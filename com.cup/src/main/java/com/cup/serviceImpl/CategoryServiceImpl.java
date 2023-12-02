package com.cup.serviceImpl;

import com.cup.Constants.CupConstants;
import com.cup.Security.JwtAuthenticationFilter;
import com.cup.Utils.CupUtils;
import com.cup.entities.Category;
import com.cup.repositories.CategoryRepo;
import com.cup.service.CategoryService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    JwtAuthenticationFilter filter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (this.filter.isAdmin()) {
                if (this.validateCategoryMap(requestMap, false)) {
                    this.categoryRepo.save(this.getCategoryFromMap(requestMap, false));
                    return CupUtils.getResponseEntity("Category Successfully Added.", HttpStatus.OK);
                }
            }
            else return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // boolean validateId is used because for adding new category we only use name but for updating a category we need
    // both name and id this is indicating that the following isn't an update method
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            }
            else if (!validateId)
                return true;
        }
        return false;
    }

    // boolean isAdd is used because to verify that the following isn't an update method because update
    // need both id and name but add only need name
    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        if (isAdd) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));

        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                System.out.println("Filter Value was true, inside if.");
                return new ResponseEntity<>(this.categoryRepo.getAllCategory(), HttpStatus.OK);
            }
            else return new ResponseEntity<>(this.categoryRepo.findAll(), HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (this.filter.isAdmin()) {
                if (this.validateCategoryMap(requestMap, true)) {
                    Optional<Category> categoryOptional = this.categoryRepo.findById(Integer.parseInt(requestMap.get("id")));
                    if (categoryOptional.isPresent()) {
                        this.categoryRepo.save(this.getCategoryFromMap(requestMap, true));
                        return CupUtils.getResponseEntity("Category updated successfully.", HttpStatus.OK);
                    }
                    else return CupUtils.getResponseEntity("Category id does not exist.", HttpStatus.OK);
                }
                else return CupUtils.getResponseEntity(CupConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            else return CupUtils.getResponseEntity(CupConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
