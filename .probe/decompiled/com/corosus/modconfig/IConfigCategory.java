package com.corosus.modconfig;

public interface IConfigCategory {

    String getConfigFileName();

    String getCategory();

    void hookUpdatedValues();

    String getName();

    String getRegistryName();
}