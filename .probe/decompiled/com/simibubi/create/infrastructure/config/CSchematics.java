package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CSchematics extends ConfigBase {

    public final ConfigBase.ConfigBool creativePrintIncludesAir = this.b(false, "creativePrintIncludesAir", new String[] { CSchematics.Comments.creativePrintIncludesAir });

    public final ConfigBase.ConfigInt maxSchematics = this.i(10, 1, "maxSchematics", new String[] { CSchematics.Comments.maxSchematics });

    public final ConfigBase.ConfigInt maxTotalSchematicSize = this.i(256, 16, "maxSchematics", new String[] { CSchematics.Comments.kb, CSchematics.Comments.maxSize });

    public final ConfigBase.ConfigInt maxSchematicPacketSize = this.i(1024, 256, 32767, "maxSchematicPacketSize", new String[] { CSchematics.Comments.b, CSchematics.Comments.maxPacketSize });

    public final ConfigBase.ConfigInt schematicIdleTimeout = this.i(600, 100, "schematicIdleTimeout", new String[] { CSchematics.Comments.idleTimeout });

    public final ConfigBase.ConfigGroup schematicannon = this.group(0, "schematicannon", new String[] { "Schematicannon" });

    public final ConfigBase.ConfigInt schematicannonDelay = this.i(10, 1, "schematicannonDelay", new String[] { CSchematics.Comments.delay });

    public final ConfigBase.ConfigFloat schematicannonGunpowderWorth = this.f(20.0F, 0.0F, 100.0F, "schematicannonGunpowderWorth", new String[] { CSchematics.Comments.gunpowderWorth });

    public final ConfigBase.ConfigFloat schematicannonFuelUsage = this.f(0.05F, 0.0F, 100.0F, "schematicannonFuelUsage", new String[] { CSchematics.Comments.fuelUsage });

    @Override
    public String getName() {
        return "schematics";
    }

    private static class Comments {

        static String kb = "[in KiloBytes]";

        static String b = "[in Bytes]";

        static String maxSchematics = "The amount of Schematics a player can upload until previous ones are overwritten.";

        static String maxSize = "The maximum allowed file size of uploaded Schematics.";

        static String maxPacketSize = "The maximum packet size uploaded Schematics are split into.";

        static String idleTimeout = "Amount of game ticks without new packets arriving until an active schematic upload process is discarded.";

        static String delay = "Amount of game ticks between shots of the cannon. Higher => Slower";

        static String gunpowderWorth = "% of Schematicannon's Fuel filled by 1 Gunpowder.";

        static String fuelUsage = "% of Schematicannon's Fuel used for each fired block.";

        static String creativePrintIncludesAir = "Whether placing a Schematic directly in Creative Mode should replace world blocks with Air";
    }
}