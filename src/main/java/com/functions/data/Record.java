package com.functions.data;

public class Record {
    private String partitionKey;
    private String rowKey;
    private String place;
    private Long temperature;

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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }
}
