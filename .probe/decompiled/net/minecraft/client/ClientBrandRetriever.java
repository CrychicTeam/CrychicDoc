package net.minecraft.client;

import net.minecraft.obfuscate.DontObfuscate;

public class ClientBrandRetriever {

    public static final String VANILLA_NAME = "vanilla";

    @DontObfuscate
    public static String getClientModName() {
        return "vanilla";
    }
}