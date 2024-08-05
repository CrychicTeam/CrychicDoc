package net.blay09.mods.balm.api;

import java.util.ServiceLoader;

public class BalmRuntimeSpi {

    public static BalmRuntime create() {
        ServiceLoader<BalmRuntimeFactory> loader = ServiceLoader.load(BalmRuntimeFactory.class);
        BalmRuntimeFactory factory = (BalmRuntimeFactory) loader.findFirst().orElseThrow();
        return factory.create();
    }
}