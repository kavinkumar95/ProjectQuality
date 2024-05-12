package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.model.request.RuleMasterRequest;
import com.personal.ProjectQuality.model.request.TenantRequest;
import com.personal.ProjectQuality.model.response.RuleMasterResponse;
import com.personal.ProjectQuality.model.response.TenantResponse;
import com.personal.ProjectQuality.service.PdfService;
import com.personal.ProjectQuality.service.RuleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rulemaster")
public class RuleMasterController {

    @Autowired
    RuleMasterService ruleMasterService;
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRuleMaster(@RequestBody RuleMasterRequest ruleMasterRequest) {
        RuleMasterResponse ruleMasterResponse = ruleMasterService.createRuleMaster(ruleMasterRequest);
        return new ResponseEntity<>(ruleMasterResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getRuleMasterDetails/{ruleMasterName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRuleMasterDetailsByName(@PathVariable String ruleMasterName) {
        RuleMasterResponse ruleMasterResponse =  ruleMasterService.getRuleMasterDetailsByName(ruleMasterName);
        return new ResponseEntity<>(ruleMasterResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllDetails",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RuleMasterResponse>> getAllRuleMaster() {
        List<RuleMasterResponse> ruleMasterResponseList =  ruleMasterService.getAllRuleMaster();
        return new ResponseEntity<>(ruleMasterResponseList, HttpStatus.OK);
    }
}
