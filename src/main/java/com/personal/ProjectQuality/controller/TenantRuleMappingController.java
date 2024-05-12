package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.model.request.ClientRuleMappingRequest;
import com.personal.ProjectQuality.model.request.RuleRequest;
import com.personal.ProjectQuality.model.response.ClientRuleMappingResponse;
import com.personal.ProjectQuality.model.response.RuleResponse;
import com.personal.ProjectQuality.model.response.TenantResponse;
import com.personal.ProjectQuality.service.PdfService;
import com.personal.ProjectQuality.service.RuleService;
import com.personal.ProjectQuality.service.TenantRuleMappingService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenant/rulemapping")
public class TenantRuleMappingController {

    @Autowired
    TenantRuleMappingService tenantRuleMappingService;
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createMappingRequest(@RequestBody ClientRuleMappingRequest clientRuleMappingRequest) {
        ClientRuleMappingResponse clientRuleMappingResponse = tenantRuleMappingService.createMappingRequest(clientRuleMappingRequest);
        return new ResponseEntity<>(clientRuleMappingResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getRuleMappingDetails/{tenantCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientRuleMappingResponse>> getRuleMappingDetailsByTenantCode(@PathVariable String tenantCode) {
        List<ClientRuleMappingResponse> clientRuleMappingResponseList =  tenantRuleMappingService.getRuleMappingDetailsByTenantCode(tenantCode);
        return new ResponseEntity<>(clientRuleMappingResponseList, HttpStatus.OK);
    }
}
