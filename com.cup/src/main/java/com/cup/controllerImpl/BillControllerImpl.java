package com.cup.controllerImpl;

import com.cup.Constants.CupConstants;
import com.cup.Utils.CupUtils;
import com.cup.controller.BillController;
import com.cup.entities.Bill;
import com.cup.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillControllerImpl implements BillController {

    @Autowired
    private BillService billService;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return this.billService.generateReport(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return this.billService.getBills();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return this.billService.getPdf(requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            return this.billService.deleteBill(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return CupUtils.getResponseEntity(CupConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
