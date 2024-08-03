package com.simibubi.create.foundation.data;

/**
 * @deprecated
 */
public class LangEntry {

    static final String ENTRY_FORMAT = "\t\"%s\": %s,\n";

    private String key;

    private String value;

    LangEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return String.format("\t\"%s\": %s,\n", this.key, LangMerger.GSON.toJson(this.value, String.class));
    }
}