package net.raphimc.immediatelyfast.apiimpl;

import net.raphimc.immediatelyfastapi.ApiAccess;
import net.raphimc.immediatelyfastapi.BatchingAccess;
import net.raphimc.immediatelyfastapi.ConfigAccess;

public class ApiAccessImpl implements ApiAccess {

    private final BatchingAccess batchingAccess = new BatchingAccessImpl();

    private final ConfigAccess configAccess = new ConfigAccessImpl();

    private final ConfigAccess runtimeConfigAccess = new RuntimeConfigAccessImpl();

    @Override
    public BatchingAccess getBatching() {
        return this.batchingAccess;
    }

    @Override
    public ConfigAccess getConfig() {
        return this.configAccess;
    }

    @Override
    public ConfigAccess getRuntimeConfig() {
        return this.runtimeConfigAccess;
    }
}