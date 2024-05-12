package com.personal.ProjectQuality.service;

import com.linkedin.common.*;
import com.linkedin.common.url.Url;
import com.linkedin.common.urn.TagUrn;
import com.linkedin.common.urn.Urn;
import com.linkedin.tag.TagProperties;
import com.personal.ProjectQuality.entity.QualityIndexColumnInfo;
import com.personal.ProjectQuality.entity.QualityIndexFailureInfo;
import com.personal.ProjectQuality.entity.QualityIndexTableInfo;
import com.personal.ProjectQuality.model.PartialUnexpectedCount;
import com.personal.ProjectQuality.model.ResultItem;
import com.personal.ProjectQuality.model.ValidationResult;
import com.personal.ProjectQuality.repository.QualityIndexColumnRepository;
import com.personal.ProjectQuality.repository.QualityIndexFailureInfoRepository;
import com.personal.ProjectQuality.repository.QualityIndexTableRepository;
import datahub.client.MetadataWriteResponse;
import datahub.client.rest.RestEmitter;
import datahub.event.MetadataChangeProposalWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

@Service
public class DatahubService {

    @Autowired
    QualityIndexTableRepository qualityIndexTableRepository;

    @Autowired
    QualityIndexColumnRepository qualityIndexColumnRepository;

    @Autowired
    QualityIndexFailureInfoRepository qualityIndexFailureInfoRepository;
    public void sendResulttoCatalog(String dataplatForm, ValidationResult validationResult) {
        DecimalFormat df = new DecimalFormat("#.##");
        String tagName = "DQI:";
        tagName = tagName + df.format(validationResult.getStatistics().getSuccess_percent());
        System.out.println(tagName);
        RestEmitter emitter = RestEmitter.create(b -> {
            b.server("http://34.16.202.109:8080").customizeHttpAsyncClient(custom -> {
                custom.setMaxConnPerRoute(20).setMaxConnTotal(20).build();
            }).build();
        });
        createTagData(emitter,tagName);
        Map<String, Object> metaMap = validationResult.getMeta();
        Map<String, Object> batchSpecMap = (Map<String, Object>) metaMap.get("batch_spec");
        String schemaName = (String) batchSpecMap.get("schema_name");
        String tableName = (String) batchSpecMap.get("table_name");
        attachtoDataset(emitter,tagName,tableName,schemaName,dataplatForm);
        attachLinktoDataset(emitter,tableName,schemaName,dataplatForm);

    }



    private void attachLinktoDataset(RestEmitter emitter, String tableName, String schemaName, String dataplatForm) {
        InstitutionalMemory institutionalMemory = new InstitutionalMemory();
        InstitutionalMemoryMetadataArray institutionalMemoryMetadataArray = new InstitutionalMemoryMetadataArray();
        InstitutionalMemoryMetadata institutionalMemoryMetadata = new InstitutionalMemoryMetadata();
        Url url = new Url("http://localhost:8084/api/generate/pdf/"+dataplatForm+"/"+schemaName+"/"+tableName);
        institutionalMemoryMetadata.setUrl(url);
        institutionalMemoryMetadata.setDescription(tableName +" Quality Report");
        AuditStamp auditStamp = new AuditStamp();
        try {
            auditStamp.setActor(new Urn("urn:li:corpuser:datahub"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        auditStamp.setTime(Instant.now().toEpochMilli());
        institutionalMemoryMetadata.setCreateStamp(auditStamp);
        institutionalMemoryMetadataArray.add(institutionalMemoryMetadata);
        institutionalMemory.setElements(institutionalMemoryMetadataArray);

        try {
            MetadataChangeProposalWrapper mcpw = MetadataChangeProposalWrapper.builder()
                    .entityType("dataset")
                    .entityUrn("urn:li:dataset"  + ":(urn:li:dataPlatform:" + dataplatForm + "," + schemaName+"."+tableName + "," + "PROD" + ")")
                    .upsert()
                    .aspect(institutionalMemory)
                    .build();
            Future<MetadataWriteResponse> responseFuture = emitter.emit(mcpw);
            MetadataWriteResponse response = responseFuture.get();
            if (response.isSuccess()){
                System.out.println("Link added to dataset Successfully");
            }
        } catch (Exception e) {

        }

    }

    private void attachtoDataset(RestEmitter emitter, String tagName,String tableName, String schemaName,String dataplatForm) {
        GlobalTags globalTags = new GlobalTags();
        TagUrn entity = new TagUrn(tagName.toLowerCase());
        TagAssociation association = new TagAssociation().setTag(entity);
        TagAssociationArray tagAssociationArray = new TagAssociationArray();
        tagAssociationArray.add(association);
        globalTags.setTags(new TagAssociationArray(tagAssociationArray));
        try {
            MetadataChangeProposalWrapper mcpw = MetadataChangeProposalWrapper.builder()
                    .entityType("dataset")
                    .entityUrn("urn:li:dataset"  + ":(urn:li:dataPlatform:" + dataplatForm + "," + schemaName+"."+tableName + "," + "PROD" + ")")
                    .upsert()
                    .aspect(globalTags)
                    .build();
            Future<MetadataWriteResponse> responseFuture = emitter.emit(mcpw);
            MetadataWriteResponse response = responseFuture.get();
            if (response.isSuccess()){
             System.out.println("tag added to dataset Successfully");
            }
        } catch (Exception e) {

        }
    }

    private void createTagData(RestEmitter emitter, String tagName) {
        TagProperties tagProperties = new TagProperties().setName(tagName).setDescription(tagName);
        Urn tagUrn = new TagUrn(tagName.toLowerCase());
        try {
            MetadataChangeProposalWrapper mcpw = MetadataChangeProposalWrapper.builder()
                    .entityType("tag")
                    .entityUrn(tagUrn)
                    .upsert()
                    .aspect(tagProperties)
                    .build();
            Future<MetadataWriteResponse> responseFuture = emitter.emit(mcpw);
            MetadataWriteResponse response = responseFuture.get();
            if (response.isSuccess()) {
                System.out.println("Tag Created ");
            }
        } catch (Exception e) {

        }
    }

    public void storeValidationResult(String dataPlatform, ValidationResult validationResult) {
        QualityIndexTableInfo qualityIndexTableInfo = saveTableInfo(validationResult,dataPlatform);
        saveColumnInfo(validationResult.getResults(),qualityIndexTableInfo);
        saveDetailedInfo(validationResult.getResults(),qualityIndexTableInfo);
    }

    private void saveDetailedInfo(List<ResultItem> validationResultList, QualityIndexTableInfo qualityIndexTableInfo) {
        List<QualityIndexFailureInfo> qualityIndexFailureInfoList = new ArrayList<>();
        for(ResultItem resultItem:validationResultList){
            if(resultItem.getResult().getUnexpected_count()>0){
                for(PartialUnexpectedCount partialUnexpectedCount:resultItem.getResult().getPartial_unexpected_counts()){
                    QualityIndexFailureInfo qualityIndexFailureInfo = new QualityIndexFailureInfo ();
                    Map<String,Object> ValueInfoMap = resultItem.getExpectation_config().getKwargs();
                    qualityIndexFailureInfo.setJobId(qualityIndexTableInfo.getJobId());
                    qualityIndexFailureInfo.setSchemaName(qualityIndexTableInfo.getSchemaName());
                    qualityIndexFailureInfo.setTableName(qualityIndexTableInfo.getTableName());
                    qualityIndexFailureInfo.setColumnName(String.valueOf(ValueInfoMap.get("column")));
                    qualityIndexFailureInfo.setFailedCount(partialUnexpectedCount.getCount());
                    qualityIndexFailureInfo.setFailedData(partialUnexpectedCount.getValue());
                    qualityIndexFailureInfo.setCreatedUser("System");
                    qualityIndexFailureInfo.setUpdatedUser("System");
                    qualityIndexFailureInfoList.add(qualityIndexFailureInfo);
                }
            }
        }
        qualityIndexFailureInfoRepository.saveAll(qualityIndexFailureInfoList);
    }

    private void saveColumnInfo(List<ResultItem> validationResultList, QualityIndexTableInfo qualityIndexTableInfo) {
        List<QualityIndexColumnInfo> qualityIndexColumnInfoList = new ArrayList<>();
        for(ResultItem resultItem:validationResultList){
            Map<String,Object> ValueInfoMap = resultItem.getExpectation_config().getKwargs();
            String validation = "";
            QualityIndexColumnInfo qualityIndexColumnInfo = new QualityIndexColumnInfo();
            qualityIndexColumnInfo.setJobId(qualityIndexTableInfo.getJobId());
            qualityIndexColumnInfo.setTableName(qualityIndexTableInfo.getTableName());
            qualityIndexColumnInfo.setSchemaName(qualityIndexTableInfo.getSchemaName());
            qualityIndexColumnInfo.setColumnName(String.valueOf(ValueInfoMap.get("column")));
            qualityIndexColumnInfo.setTotalRowCount(resultItem.getResult().getElement_count());
            qualityIndexColumnInfo.setSuccessfulRowCount(Integer.valueOf((int) (resultItem.getResult().getElement_count()- resultItem.getResult().getUnexpected_count())));
            qualityIndexColumnInfo.setFailedRowCount(Integer.valueOf((int)resultItem.getResult().getUnexpected_count()));
            for (Map.Entry<String,Object> entry : ValueInfoMap.entrySet()){
                if(!entry.getKey().equals("column")&& !entry.getKey().equals("batch_id")){
                    validation = validation+ entry.getKey() +" : "+entry.getValue()+" ";
                }
            }
            qualityIndexColumnInfo.setExpectationType(resultItem.getExpectation_config().getExpectation_type()+" "+validation);
            qualityIndexColumnInfo.setValidationStatus(resultItem.isSuccess());
            qualityIndexColumnInfo.setCreatedUser("System");
            qualityIndexColumnInfo.setUpdatedUser("System");

            qualityIndexColumnInfoList.add(qualityIndexColumnInfo);
        }
        qualityIndexColumnRepository.saveAll(qualityIndexColumnInfoList);

    }

    private QualityIndexTableInfo saveTableInfo(ValidationResult validationResult, String dataPlatform) {
        DecimalFormat df = new DecimalFormat("#.##");
        String validationPercentage = df.format(validationResult.getStatistics().getSuccess_percent());
        Map<String, Object> metaMap = validationResult.getMeta();
        Map<String, Object> batchSpecMap = (Map<String, Object>) metaMap.get("batch_spec");
        String schemaName = (String) batchSpecMap.get("schema_name");
        String tableName = (String) batchSpecMap.get("table_name");
        QualityIndexTableInfo qualityIndexTableInfo = new QualityIndexTableInfo();
        qualityIndexTableInfo.setJobId(UUID.randomUUID().toString());
        qualityIndexTableInfo.setTableName(tableName);
        qualityIndexTableInfo.setSchemaName(schemaName);
        qualityIndexTableInfo.setValidationResult(Float.valueOf(validationPercentage));
        qualityIndexTableInfo.setTotalValidationCount(validationResult.getStatistics().getEvaluated_expectations());
        qualityIndexTableInfo.setSuccessfullValidationCount(validationResult.getStatistics().getSuccessful_expectations());
        qualityIndexTableInfo.setFailedValidationCount(validationResult.getStatistics().getUnsuccessful_expectations());
        qualityIndexTableInfo.setStatus(validationResult.isSuccess());
        qualityIndexTableInfo.setDatasource(dataPlatform);
        qualityIndexTableInfo.setCreatedUser("System");
        qualityIndexTableInfo.setUpdatedUser("System");
        return qualityIndexTableRepository.save(qualityIndexTableInfo);
    }
}
