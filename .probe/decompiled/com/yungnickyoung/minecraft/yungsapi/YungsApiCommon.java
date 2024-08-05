package com.yungnickyoung.minecraft.yungsapi;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YungsApiCommon {

    public static final String MOD_ID = "yungsapi";

    public static final Logger LOGGER = LogManager.getLogger("yungsapi");

    public static void init() {
        AutoRegistrationManager.initAutoRegPackage("com.yungnickyoung.minecraft.yungsapi.module");
    }
}