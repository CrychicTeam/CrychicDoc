package com.simibubi.create.infrastructure.config;

import com.simibubi.create.foundation.config.ConfigBase;

public class CServer extends ConfigBase {

    public final ConfigBase.ConfigGroup infrastructure = this.group(0, "infrastructure", new String[] { CServer.Comments.infrastructure });

    public final ConfigBase.ConfigInt tickrateSyncTimer = this.i(20, 5, "tickrateSyncTimer", new String[] { "[in Ticks]", CServer.Comments.tickrateSyncTimer, CServer.Comments.tickrateSyncTimer2 });

    public final CRecipes recipes = this.nested(0, CRecipes::new, new String[] { CServer.Comments.recipes });

    public final CKinetics kinetics = this.nested(0, CKinetics::new, new String[] { CServer.Comments.kinetics });

    public final CFluids fluids = this.nested(0, CFluids::new, new String[] { CServer.Comments.fluids });

    public final CLogistics logistics = this.nested(0, CLogistics::new, new String[] { CServer.Comments.logistics });

    public final CSchematics schematics = this.nested(0, CSchematics::new, new String[] { CServer.Comments.schematics });

    public final CEquipment equipment = this.nested(0, CEquipment::new, new String[] { CServer.Comments.equipment });

    public final CTrains trains = this.nested(0, CTrains::new, new String[] { CServer.Comments.trains });

    @Override
    public String getName() {
        return "server";
    }

    private static class Comments {

        static String recipes = "Packmakers' control panel for internal recipe compat";

        static String schematics = "Everything related to Schematic tools";

        static String kinetics = "Parameters and abilities of Create's kinetic mechanisms";

        static String fluids = "Create's liquid manipulation tools";

        static String logistics = "Tweaks for logistical components";

        static String equipment = "Equipment and gadgets added by Create";

        static String trains = "Create's builtin Railway systems";

        static String infrastructure = "The Backbone of Create";

        static String tickrateSyncTimer = "The amount of time a server waits before sending out tickrate synchronization packets.";

        static String tickrateSyncTimer2 = "These packets help animations to be more accurate when tps is below 20.";
    }
}