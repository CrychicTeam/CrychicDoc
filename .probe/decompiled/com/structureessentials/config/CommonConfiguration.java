package com.structureessentials.config;

import com.cupboard.config.ICommonConfig;
import com.google.gson.JsonObject;

public class CommonConfiguration implements ICommonConfig {

    public boolean structurePlacementLogging = false;

    public int structureSearchTimeout = 50;

    public boolean useFastStructureLookup = true;

    public boolean warnMissingRegistryEntry = true;

    public boolean disableLegacyRandomCrashes = true;

    @Override
    public JsonObject serialize() {
        JsonObject root = new JsonObject();
        JsonObject entry = new JsonObject();
        entry.addProperty("desc:", "Enables debug logging of structure placement, does spam logs, only recommenced for debugging. Default: false");
        entry.addProperty("structurePlacementLogging", this.structurePlacementLogging);
        root.add("structurePlacementLogging", entry);
        JsonObject entry2 = new JsonObject();
        entry2.addProperty("desc:", "Maximum time in seconds a structure search is allowed to take: default 50");
        entry2.addProperty("structureSearchTimeout", this.structureSearchTimeout);
        root.add("structureSearchTimeout", entry2);
        JsonObject entry3 = new JsonObject();
        entry3.addProperty("desc:", "Whether the fast structure search is used, default: true");
        entry3.addProperty("useFastStructureLookup", this.useFastStructureLookup);
        root.add("useFastStructureLookup", entry3);
        JsonObject entry4 = new JsonObject();
        entry4.addProperty("desc:", "Prevents crashes for missing registry entries(e.g. a mod update structure ids) and turns them into a log error message instead, default: true");
        entry4.addProperty("warnMissingRegistryEntry", this.warnMissingRegistryEntry);
        root.add("warnMissingRegistryEntry", entry4);
        JsonObject entry5 = new JsonObject();
        entry5.addProperty("desc:", "Prevents crashes for multithreaded access of thread specific randoms, default: true");
        entry5.addProperty("disableLegacyRandomCrashes", this.disableLegacyRandomCrashes);
        root.add("disableLegacyRandomCrashes", entry5);
        return root;
    }

    @Override
    public void deserialize(JsonObject data) {
        this.structurePlacementLogging = data.get("structurePlacementLogging").getAsJsonObject().get("structurePlacementLogging").getAsBoolean();
        this.structureSearchTimeout = data.get("structureSearchTimeout").getAsJsonObject().get("structureSearchTimeout").getAsInt();
        this.useFastStructureLookup = data.get("useFastStructureLookup").getAsJsonObject().get("useFastStructureLookup").getAsBoolean();
        this.warnMissingRegistryEntry = data.get("warnMissingRegistryEntry").getAsJsonObject().get("warnMissingRegistryEntry").getAsBoolean();
        this.disableLegacyRandomCrashes = data.get("disableLegacyRandomCrashes").getAsJsonObject().get("disableLegacyRandomCrashes").getAsBoolean();
    }
}