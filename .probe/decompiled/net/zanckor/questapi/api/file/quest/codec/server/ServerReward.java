package net.zanckor.questapi.api.file.quest.codec.server;

import java.util.List;

public class ServerReward {

    private String type;

    private String tag;

    private Integer amount;

    private Integer additionalIntegerData;

    private Double additionalDoubleData;

    private String additionalStringData;

    private List<?> additionalListData;

    private Object additionalClassData;

    public Double getAdditionalDoubleData() {
        return this.additionalDoubleData;
    }

    public void setAdditionalDoubleData(Double additionalDoubleData) {
        this.additionalDoubleData = additionalDoubleData;
    }

    public String getAdditionalStringData() {
        return this.additionalStringData;
    }

    public void setAdditionalStringData(String additionalStringData) {
        this.additionalStringData = additionalStringData;
    }

    public List<?> getAdditionalListData() {
        return this.additionalListData;
    }

    public void setAdditionalListData(List<?> additionalListData) {
        this.additionalListData = additionalListData;
    }

    public Object getAdditionalClassData() {
        return this.additionalClassData;
    }

    public void setAdditionalClassData(Object additionalClassData) {
        this.additionalClassData = additionalClassData;
    }

    public Integer getAdditionalIntegerData() {
        return this.additionalIntegerData;
    }

    public void setAdditionalIntegerData(Integer additionalIntegerData) {
        this.additionalIntegerData = additionalIntegerData;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}