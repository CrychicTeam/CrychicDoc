package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CLogistics extends ConfigBase {

    public final ConfigBase.ConfigInt defaultExtractionTimer = this.i(8, 1, "defaultExtractionTimer", new String[] { CLogistics.Comments.defaultExtractionTimer });

    public final ConfigBase.ConfigInt psiTimeout = this.i(60, 1, "psiTimeout", new String[] { CLogistics.Comments.psiTimeout });

    public final ConfigBase.ConfigInt mechanicalArmRange = this.i(5, 1, "mechanicalArmRange", new String[] { CLogistics.Comments.mechanicalArmRange });

    public final ConfigBase.ConfigInt linkRange = this.i(256, 1, "linkRange", new String[] { CLogistics.Comments.linkRange });

    public final ConfigBase.ConfigInt displayLinkRange = this.i(64, 1, "displayLinkRange", new String[] { CLogistics.Comments.displayLinkRange });

    public final ConfigBase.ConfigInt vaultCapacity = this.i(20, 1, "vaultCapacity", new String[] { CLogistics.Comments.vaultCapacity });

    public final ConfigBase.ConfigInt brassTunnelTimer = this.i(10, 1, 10, "brassTunnelTimer", new String[] { CLogistics.Comments.brassTunnelTimer });

    public final ConfigBase.ConfigBool seatHostileMobs = this.b(true, "seatHostileMobs", new String[] { CLogistics.Comments.seatHostileMobs });

    @Override
    public String getName() {
        return "logistics";
    }

    private static class Comments {

        static String defaultExtractionTimer = "The amount of ticks a funnel waits between item transferrals, when it is not re-activated by redstone.";

        static String linkRange = "Maximum possible range in blocks of redstone link connections.";

        static String displayLinkRange = "Maximum possible distance in blocks between data gatherers and their target.";

        static String psiTimeout = "The amount of ticks a portable storage interface waits for transfers until letting contraptions move along.";

        static String mechanicalArmRange = "Maximum distance in blocks a Mechanical Arm can reach across.";

        static String vaultCapacity = "The total amount of stacks a vault can hold per block in size.";

        static String brassTunnelTimer = "The amount of ticks a brass tunnel waits between distributions.";

        static String seatHostileMobs = "Whether hostile mobs walking near a seat will start riding it.";
    }
}