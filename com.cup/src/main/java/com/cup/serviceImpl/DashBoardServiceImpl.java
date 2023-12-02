package com.cup.serviceImpl;

import com.cup.repositories.BillRepo;
import com.cup.repositories.CategoryRepo;
import com.cup.repositories.ProductRepo;
import com.cup.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashBoardServiceImpl implements DashboardService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private BillRepo billRepo;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("category", this.categoryRepo.count());
        map.put("product", this.productRepo.count());
        map.put("bill", this.billRepo.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
