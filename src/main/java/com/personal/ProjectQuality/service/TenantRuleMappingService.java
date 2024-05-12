package com.personal.ProjectQuality.service;

import com.personal.ProjectQuality.entity.ClientInfo;
import com.personal.ProjectQuality.entity.ClientRuleMapping;
import com.personal.ProjectQuality.entity.RuleInfo;
import com.personal.ProjectQuality.entity.RuleMasterCategoryInfo;
import com.personal.ProjectQuality.model.request.ClientRuleMappingRequest;
import com.personal.ProjectQuality.model.response.ClientRuleMappingResponse;
import com.personal.ProjectQuality.repository.ClientInfoRepository;
import com.personal.ProjectQuality.repository.ClientRuleMappingRepository;
import com.personal.ProjectQuality.repository.RuleInfoRepository;
import com.personal.ProjectQuality.repository.RuleMasterCategoryInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TenantRuleMappingService {

    @Autowired
    ClientRuleMappingRepository clientRuleMappingRepository;

    @Autowired
    ClientInfoRepository clientInfoRepository;

    @Autowired
    RuleMasterCategoryInfoRepository ruleMasterCategoryInfoRepository;

    @Autowired
    RuleInfoRepository ruleInfoRepository;
    public ClientRuleMappingResponse createMappingRequest(ClientRuleMappingRequest clientRuleMappingRequest) throws RuntimeException {

            Optional<ClientInfo> optionalClientInfo = clientInfoRepository.findByTenantName(clientRuleMappingRequest.getTenantCode());
            Optional<RuleMasterCategoryInfo> optionalRuleMasterCategoryInfo = ruleMasterCategoryInfoRepository.findByCategoryName(clientRuleMappingRequest.getRuleMasterName());
            Optional<RuleInfo> optionalRuleInfo = ruleInfoRepository.findByRuleName(clientRuleMappingRequest.getRuleName());
            if(optionalClientInfo.isPresent() && optionalRuleMasterCategoryInfo.isPresent() && optionalRuleInfo.isPresent()){
                RuleMasterCategoryInfo ruleMasterCategoryInfo = optionalRuleMasterCategoryInfo.get();
                RuleInfo ruleInfo = optionalRuleInfo.get();
                ClientInfo clientInfo = optionalClientInfo.get();
                ClientRuleMapping clientRuleMapping = new ClientRuleMapping();
                clientRuleMapping.setRuleInfo(ruleInfo);
                clientRuleMapping.setClientInfo(clientInfo);
                clientRuleMapping.setRuleMasterCategoryInfo(ruleMasterCategoryInfo);
                clientRuleMapping.setMinValue(clientRuleMappingRequest.getMinValue());
                clientRuleMapping.setMaxValue(clientRuleMappingRequest.getMaxValue());
                clientRuleMapping.setRegex(clientRuleMappingRequest.getRegex());
                ClientRuleMapping clientRuleMappingResponse= clientRuleMappingRepository.save(clientRuleMapping);
                return formClientRuleMappingResponse(clientRuleMappingResponse);
            }else{
                throw new RuntimeException("Some parameters are incorrect");
            }
    }

    private ClientRuleMappingResponse formClientRuleMappingResponse(ClientRuleMapping clientRuleMapping) {
        ClientRuleMappingResponse clientRuleMappingResponse = new ClientRuleMappingResponse();
        clientRuleMappingResponse.setTenantCode(clientRuleMapping.getClientInfo().getTenantName());
        clientRuleMappingResponse.setRuleMasterName(clientRuleMapping.getRuleMasterCategoryInfo().getCategoryName());
        clientRuleMappingResponse.setRuleName(clientRuleMapping.getRuleInfo().getRuleName());
        clientRuleMappingResponse.setNullCheck(clientRuleMapping.getNullCheck());
        clientRuleMappingResponse.setMinValue(clientRuleMapping.getMinValue());
        clientRuleMappingResponse.setMaxValue(clientRuleMapping.getMaxValue());
        clientRuleMappingResponse.setRegex(clientRuleMapping.getRegex());
        clientRuleMappingResponse.setCreatedTime(clientRuleMapping.getCreatedTime());
        clientRuleMappingResponse.setUpdatedTime(clientRuleMapping.getUpdatedTime());
        clientRuleMappingResponse.setCreatedUser(clientRuleMapping.getCreatedUser());
        clientRuleMappingResponse.setUpdatedUser(clientRuleMapping.getUpdatedUser());
        return clientRuleMappingResponse;
    }

    public List<ClientRuleMappingResponse> getRuleMappingDetailsByTenantCode(String tenantCode)  throws RuntimeException{
            Optional<ClientInfo> optionalClientInfo = clientInfoRepository.findByTenantName(tenantCode);
           if(optionalClientInfo.isPresent()){
               List<ClientRuleMappingResponse> clientRuleMappingResponseList = new ArrayList<>();
                ClientInfo clientInfo = optionalClientInfo.get();
               List<ClientRuleMapping> clientRuleMappingList = clientRuleMappingRepository.findByClientInfo(clientInfo);
               for(ClientRuleMapping clientRuleMapping : clientRuleMappingList){
                   clientRuleMappingResponseList.add(formClientRuleMappingResponse(clientRuleMapping));
               }
                return clientRuleMappingResponseList;
            }else{
                throw new RuntimeException("Some parameters are incorrect");
            }
    }
}
