package com.yungnickyoung.minecraft.yungsapi.api;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import com.yungnickyoung.minecraft.yungsapi.services.Services;

public class YungAutoRegister {

    public static void scanPackageForAnnotations(String packageName) {
        if (Services.PLATFORM.isFabric()) {
            AutoRegistrationManager.initAutoRegPackage(packageName);
        }
    }
}