package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CEquipment extends ConfigBase {

    public final ConfigBase.ConfigInt maxSymmetryWandRange = this.i(50, 10, "maxSymmetryWandRange", new String[] { CEquipment.Comments.symmetryRange });

    public final ConfigBase.ConfigInt placementAssistRange = this.i(12, 3, "placementAssistRange", new String[] { CEquipment.Comments.placementRange });

    public final ConfigBase.ConfigInt toolboxRange = this.i(10, 1, "toolboxRange", new String[] { CEquipment.Comments.toolboxRange });

    public final ConfigBase.ConfigInt airInBacktank = this.i(900, 1, "airInBacktank", new String[] { CEquipment.Comments.maxAirInBacktank });

    public final ConfigBase.ConfigInt enchantedBacktankCapacity = this.i(300, 1, "enchantedBacktankCapacity", new String[] { CEquipment.Comments.enchantedBacktankCapacity });

    public final ConfigBase.ConfigInt maxExtendoGripActions = this.i(1000, 0, "maxExtendoGripActions", new String[] { CEquipment.Comments.maxExtendoGripActions });

    public final ConfigBase.ConfigInt maxPotatoCannonShots = this.i(200, 0, "maxPotatoCannonShots", new String[] { CEquipment.Comments.maxPotatoCannonShots });

    @Override
    public String getName() {
        return "equipment";
    }

    private static class Comments {

        static String symmetryRange = "The Maximum Distance to an active mirror for the symmetry wand to trigger.";

        static String maxAirInBacktank = "The Maximum volume of Air that can be stored in a backtank = Seconds of underwater breathing";

        static String enchantedBacktankCapacity = "The volume of Air added by each level of the backtanks Capacity Enchantment";

        static String placementRange = "The Maximum Distance a Block placed by Create's placement assist will have to its interaction point.";

        static String toolboxRange = "The Maximum Distance at which a Toolbox can interact with Players' Inventories.";

        static String maxExtendoGripActions = "Amount of free Extendo Grip actions provided by one filled Copper Backtank. Set to 0 makes Extendo Grips unbreakable";

        static String maxPotatoCannonShots = "Amount of free Potato Cannon shots provided by one filled Copper Backtank. Set to 0 makes Potato Cannons unbreakable";
    }
}