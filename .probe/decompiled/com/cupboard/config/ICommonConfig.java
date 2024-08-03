package com.cupboard.config;

import com.google.gson.JsonObject;

public interface ICommonConfig {

    JsonObject serialize();

    void deserialize(JsonObject var1);
}