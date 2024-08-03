package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;

public class PlayerInfo extends ValueObject implements ReflectionBasedSerialization {

    @SerializedName("name")
    private String name;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("operator")
    private boolean operator;

    @SerializedName("accepted")
    private boolean accepted;

    @SerializedName("online")
    private boolean online;

    public String getName() {
        return this.name;
    }

    public void setName(String string0) {
        this.name = string0;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String string0) {
        this.uuid = string0;
    }

    public boolean isOperator() {
        return this.operator;
    }

    public void setOperator(boolean boolean0) {
        this.operator = boolean0;
    }

    public boolean getAccepted() {
        return this.accepted;
    }

    public void setAccepted(boolean boolean0) {
        this.accepted = boolean0;
    }

    public boolean getOnline() {
        return this.online;
    }

    public void setOnline(boolean boolean0) {
        this.online = boolean0;
    }
}