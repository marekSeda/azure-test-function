package com.functions.data;

public class MenuRecord {
    private String partitionKey;
    private String rowKey;
    private String name;

    public String getPartitionKey() {
        return this.partitionKey;
    }

    public void setPartitionKey(String key) {
        this.partitionKey = key;
    }

    public String getRowKey() {
        return this.rowKey;
    }

    public void setRowKey(String key) {
        this.rowKey = key;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
