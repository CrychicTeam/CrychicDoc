package net.raphimc.immediatelyfastapi;

public interface ApiAccess {

    BatchingAccess getBatching();

    ConfigAccess getConfig();

    ConfigAccess getRuntimeConfig();
}