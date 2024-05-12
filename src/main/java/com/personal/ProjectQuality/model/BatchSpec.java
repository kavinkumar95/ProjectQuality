package com.personal.ProjectQuality.model;

import java.util.Map;

public class BatchSpec {
    private Map<String, Object> batch_identifiers;
    private String data_asset_name;
    private String schema_name;
    private String table_name;
    private String type;

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

    public String getSchema_name() {
        return schema_name;
    }

    public void setSchema_name(String schema_name) {
        this.schema_name = schema_name;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // getters and setters
}
