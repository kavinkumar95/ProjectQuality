package com.personal.ProjectQuality.service;

import com.personal.ProjectQuality.entity.ClientInfo;
import com.personal.ProjectQuality.model.request.TenantRequest;
import com.personal.ProjectQuality.model.response.TenantResponse;
import com.personal.ProjectQuality.repository.ClientInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    ClientInfoRepository clientInfoRepository;
    public TenantResponse createTenant(TenantRequest tenantRequest) {
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setTenantName(tenantRequest.getClinetCode());
        clientInfo.setTenantDescription(tenantRequest.getClinetDescription());
        ClientInfo clientInfoResponse = clientInfoRepository.save(clientInfo);
        return formTenantResponse(clientInfoResponse);
    }

    public TenantResponse getTenantDetailsByCode(String tenantCode) {
        Optional<ClientInfo> optionalClientInfoResponse = clientInfoRepository.findByTenantName(tenantCode);
        try{
            if(optionalClientInfoResponse.isPresent()){
                ClientInfo clientInfoResponse = optionalClientInfoResponse.get();
                return formTenantResponse(clientInfoResponse);
            }
        }catch (NoSuchElementException exception){
            throw exception;
        }
        return null;
    }

    public List<TenantResponse> getAllTenantDetails() {
        List<ClientInfo> clientInfoResponseList = clientInfoRepository.findAll();
        List<TenantResponse> tenantResponseList = new ArrayList<>();
        for (ClientInfo clientInfo : clientInfoResponseList){
            tenantResponseList.add(formTenantResponse(clientInfo));
        }
        return tenantResponseList;
    }

    private TenantResponse formTenantResponse (ClientInfo clientInfo){
        TenantResponse tenantResponse = new TenantResponse();
        tenantResponse.setClinetCode(clientInfo.getTenantName());
        tenantResponse.setClinetDescription(clientInfo.getTenantDescription());
        tenantResponse.setCreatedTime(clientInfo.getCreatedTime());
        tenantResponse.setUpdatedTime(clientInfo.getUpdatedTime());
        tenantResponse.setCreatedUser(clientInfo.getCreatedUser());
        tenantResponse.setUpdatedUser(clientInfo.getUpdatedUser());
        return tenantResponse;
    }
}
