package com.personal.ProjectQuality.model;

import java.util.Map;

public class ActiveBatchDefinition {
    private Map<String, Object> batch_identifiers;
    private String data_asset_name;
    private String data_connector_name;
    private String datasource_name;

    public Map<String, Object> getBatch_identifiers() {
        return batch_identifiers;
    }

    public void setBatch_identifiers(Map<String, Object> batch_identifiers) {
        this.batch_identifiers = batch_identifiers;
    }

    public String getData_asset_name() {
        return data_asset_name;
    }

    public void setData_asset_name(String data_asset_name) {
        this.data_asset_name = data_asset_name;
    }

    public String getData_connector_name() {
        return data_connector_name;
    }

    public void setData_connector_name(String data_connector_name) {
        this.data_connector_name = data_connector_name;
    }

    public String getDatasource_name() {
        return datasource_name;
    }

    public void setDatasource_name(String datasource_name) {
        this.datasource_name = datasource_name;
    }

    // getters and setters
}
