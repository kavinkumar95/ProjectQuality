package com.personal.ProjectQuality.service;

import com.personal.ProjectQuality.entity.ClientInfo;
import com.personal.ProjectQuality.entity.RuleMasterCategoryInfo;
import com.personal.ProjectQuality.model.request.RuleMasterRequest;
import com.personal.ProjectQuality.model.response.RuleMasterResponse;
import com.personal.ProjectQuality.model.response.TenantResponse;
import com.personal.ProjectQuality.repository.ClientInfoRepository;
import com.personal.ProjectQuality.repository.RuleMasterCategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RuleMasterService {

    @Autowired
    RuleMasterCategoryInfoRepository ruleMasterCategoryInfoRepository;
    public RuleMasterResponse createRuleMaster(RuleMasterRequest ruleMasterRequest) {
        RuleMasterCategoryInfo ruleMasterCategoryInfo = new RuleMasterCategoryInfo();
        ruleMasterCategoryInfo.setCategoryName(ruleMasterRequest.getRuleMasterName());
        ruleMasterCategoryInfo.setCategoryDescription(ruleMasterRequest.getRuleMasterDescription());
        RuleMasterCategoryInfo ruleMasterCategoryInfoResponse = ruleMasterCategoryInfoRepository.save(ruleMasterCategoryInfo);
        return formRuleMasterResponse(ruleMasterCategoryInfoResponse);
    }

    public RuleMasterResponse getRuleMasterDetailsByName(String ruleMasterName) {
        Optional<RuleMasterCategoryInfo> optionalRuleMasterCategoryInfo = ruleMasterCategoryInfoRepository.findByCategoryName(ruleMasterName);
        try{
            if(optionalRuleMasterCategoryInfo.isPresent()){
                RuleMasterCategoryInfo ruleMasterCategoryInfo = optionalRuleMasterCategoryInfo.get();
                return formRuleMasterResponse(ruleMasterCategoryInfo);
            }
        }catch (NoSuchElementException exception){
            throw exception;
        }
        return null;
    }

    public List<RuleMasterResponse> getAllRuleMaster() {
        List<RuleMasterCategoryInfo> ruleMasterCategoryInfoList = ruleMasterCategoryInfoRepository.findAll();
        List<RuleMasterResponse> ruleMasterResponseList = new ArrayList<>();
        for (RuleMasterCategoryInfo ruleMasterCategoryInfo : ruleMasterCategoryInfoList){
            ruleMasterResponseList.add(formRuleMasterResponse(ruleMasterCategoryInfo));
        }
        return ruleMasterResponseList;
    }


    private RuleMasterResponse formRuleMasterResponse (RuleMasterCategoryInfo ruleMasterCategoryInfo){
        RuleMasterResponse ruleMasterResponse = new RuleMasterResponse();
        ruleMasterResponse.setRuleMasterName(ruleMasterCategoryInfo.getCategoryName());
        ruleMasterResponse.setRuleMasterDescription(ruleMasterCategoryInfo.getCategoryDescription());
        ruleMasterResponse.setCreatedTime(ruleMasterCategoryInfo.getCreatedTime());
        ruleMasterResponse.setUpdatedTime(ruleMasterCategoryInfo.getUpdatedTime());
        ruleMasterResponse.setCreatedUser(ruleMasterCategoryInfo.getCreatedUser());
        ruleMasterResponse.setUpdatedUser(ruleMasterCategoryInfo.getUpdatedUser());
        return ruleMasterResponse;
    }
}
