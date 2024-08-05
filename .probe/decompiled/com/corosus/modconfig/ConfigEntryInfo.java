package com.corosus.modconfig;

public class ConfigEntryInfo {

    public int index;

    public String name;

    public Object value;

    public String comment;

    public boolean markForUpdate = false;

    public ConfigEntryInfo(int parIndex, String parName, Object parVal, String parComment) {
        this.index = parIndex;
        this.name = parName;
        this.value = parVal;
        this.comment = parComment;
    }
}