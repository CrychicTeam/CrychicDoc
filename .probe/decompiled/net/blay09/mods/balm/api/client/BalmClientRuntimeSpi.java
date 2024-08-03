package net.blay09.mods.balm.api.client;

import java.util.ServiceLoader;

public class BalmClientRuntimeSpi {

    public static BalmClientRuntime create() {
        ServiceLoader<BalmClientRuntimeFactory> loader = ServiceLoader.load(BalmClientRuntimeFactory.class);
        BalmClientRuntimeFactory factory = (BalmClientRuntimeFactory) loader.findFirst().orElseThrow();
        return factory.create();
    }
}