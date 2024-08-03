package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CWorldGen extends ConfigBase {

    public final ConfigBase.ConfigBool disable = this.b(false, "disableWorldGen", new String[] { CWorldGen.Comments.disable });

    @Override
    public String getName() {
        return "worldgen";
    }

    private static class Comments {

        static String disable = "Prevents all worldgen added by Create from taking effect";
    }
}