package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.model.request.TenantRequest;
import com.personal.ProjectQuality.model.response.TenantResponse;
import com.personal.ProjectQuality.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    @Autowired
    TenantService tenantService;
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createTenant(@RequestBody TenantRequest tenantRequest) {
        TenantResponse tenantResponse = tenantService.createTenant(tenantRequest);
        return new ResponseEntity<>(tenantResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getTenantDetails/{tenantCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTenantDetailsByCode(@PathVariable String tenantCode) {
        TenantResponse tenantResponse =  tenantService.getTenantDetailsByCode(tenantCode);
        return new ResponseEntity<>(tenantResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllTenantDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TenantResponse>> getAllTenantDetails() {
        List<TenantResponse> tenantResponseList =  tenantService.getAllTenantDetails();
        return new ResponseEntity<>(tenantResponseList, HttpStatus.OK);
    }

}
