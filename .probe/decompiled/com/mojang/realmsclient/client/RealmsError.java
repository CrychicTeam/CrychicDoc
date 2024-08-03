package com.mojang.realmsclient.client;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.realmsclient.util.JsonUtils;
import javax.annotation.Nullable;
import org.slf4j.Logger;

public class RealmsError {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final String errorMessage;

    private final int errorCode;

    private RealmsError(String string0, int int1) {
        this.errorMessage = string0;
        this.errorCode = int1;
    }

    @Nullable
    public static RealmsError parse(String string0) {
        if (Strings.isNullOrEmpty(string0)) {
            return null;
        } else {
            try {
                JsonObject $$1 = JsonParser.parseString(string0).getAsJsonObject();
                String $$2 = JsonUtils.getStringOr("errorMsg", $$1, "");
                int $$3 = JsonUtils.getIntOr("errorCode", $$1, -1);
                return new RealmsError($$2, $$3);
            } catch (Exception var4) {
                LOGGER.error("Could not parse RealmsError: {}", var4.getMessage());
                LOGGER.error("The error was: {}", string0);
                return null;
            }
        }
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public int getErrorCode() {
        return this.errorCode;
    }
}