package com.personal.ProjectQuality.controller;

import com.personal.ProjectQuality.model.request.RuleRequest;
import com.personal.ProjectQuality.model.response.RuleResponse;
import com.personal.ProjectQuality.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rule")
public class RuleController {

    @Autowired
    RuleService ruleService;
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRuleMaster(@RequestBody RuleRequest ruleRequest) {
        RuleResponse ruleResponse = ruleService.createRule(ruleRequest);
        return new ResponseEntity<>(ruleResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getRuleDetails/{ruleName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRuleDetailsByName(@PathVariable String ruleName) {
        RuleResponse ruleResponse =  ruleService.getRuleDetailsByName(ruleName);
        return new ResponseEntity<>(ruleResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/getAllRules",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RuleResponse>> getAllRules() {
        List<RuleResponse> ruleResponseList =  ruleService.getAllRules();
        return new ResponseEntity<>(ruleResponseList, HttpStatus.OK);
    }
}
