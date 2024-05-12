package com.personal.ProjectQuality.service;

import com.personal.ProjectQuality.entity.RuleInfo;
import com.personal.ProjectQuality.entity.RuleMasterCategoryInfo;
import com.personal.ProjectQuality.model.request.RuleRequest;
import com.personal.ProjectQuality.model.response.RuleMasterResponse;
import com.personal.ProjectQuality.model.response.RuleResponse;
import com.personal.ProjectQuality.repository.RuleInfoRepository;
import com.personal.ProjectQuality.repository.RuleMasterCategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RuleService {

    @Autowired
    RuleInfoRepository ruleInfoRepository;
    public RuleResponse createRule(RuleRequest ruleRequest) {
        RuleInfo ruleInfo = new RuleInfo();
        ruleInfo.setRuleName(ruleRequest.getRuleName());
        ruleInfo.setRuleDescription(ruleRequest.getRuleDescription());
        RuleInfo ruleInfoResponse = ruleInfoRepository.save(ruleInfo);
        return formRuleResponse(ruleInfoResponse);
    }

    public RuleResponse getRuleDetailsByName(String ruleName) {
        Optional<RuleInfo> optionalRuleInfo = ruleInfoRepository.findByRuleName(ruleName);
        try{
            if(optionalRuleInfo.isPresent()){
                RuleInfo ruleInfo = optionalRuleInfo.get();
                return formRuleResponse(ruleInfo);
            }
        }catch (NoSuchElementException exception){
            throw exception;
        }
        return null;
    }

    public List<RuleResponse> getAllRules() {
        List<RuleInfo> ruleInfoList = ruleInfoRepository.findAll();
        List<RuleResponse> ruleResponseList = new ArrayList<>();
        for (RuleInfo ruleInfo : ruleInfoList){
            ruleResponseList.add(formRuleResponse(ruleInfo));
        }
        return ruleResponseList;
    }


    private RuleResponse formRuleResponse (RuleInfo ruleInfo){
        RuleResponse ruleResponse = new RuleResponse();
        ruleResponse.setRuleName(ruleInfo.getRuleName());
        ruleResponse.setRuleDescription(ruleInfo.getRuleDescription());
        ruleResponse.setCreatedTime(ruleInfo.getCreatedTime());
        ruleResponse.setUpdatedTime(ruleInfo.getUpdatedTime());
        ruleResponse.setCreatedUser(ruleInfo.getCreatedUser());
        ruleResponse.setUpdatedUser(ruleInfo.getUpdatedUser());
        return ruleResponse;
    }
}
