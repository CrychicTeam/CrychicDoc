package com.mna.advancements.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class RequiredConstructPartPredicate {

    private ArrayList<ResourceLocation> partIDs;

    private ConstructSlot slot = null;

    public RequiredConstructPartPredicate(ArrayList<ResourceLocation> partIDs) {
        this.partIDs = partIDs;
    }

    public RequiredConstructPartPredicate(ArrayList<ResourceLocation> partIDs, ConstructSlot slot) {
        this(partIDs);
        this.slot = slot;
    }

    public boolean matches(IConstructConstruction construct) {
        if (this.partIDs != null && this.partIDs.size() != 0) {
            if (this.slot != null) {
                return this.partIDs.stream().anyMatch(rLoc -> ForgeRegistries.ITEMS.getKey((Item) construct.getPart(this.slot).get()).equals(rLoc));
            } else {
                for (ConstructSlot cSlot : ConstructSlot.values()) {
                    if (this.partIDs.stream().anyMatch(rLoc -> ForgeRegistries.ITEMS.getKey((Item) construct.getPart(cSlot).get()).equals(rLoc))) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            return false;
        }
    }

    public static RequiredConstructPartPredicate fromJSON(JsonObject json) {
        ArrayList<ResourceLocation> partIDs = new ArrayList();
        ConstructSlot slot = null;
        if (json.has("partIDs")) {
            JsonArray allIDs = json.get("partIDs").getAsJsonArray();
            allIDs.forEach(e -> partIDs.add(new ResourceLocation(e.getAsString())));
        }
        if (json.has("slot")) {
            try {
                slot = ConstructSlot.valueOf(json.get("slot").getAsString());
            } catch (Throwable var4) {
                ManaAndArtifice.LOGGER.error("Failed to parse construct slot in RequiredPartPredicate (custom advancement).  This is a datapack issue.");
            }
        }
        return new RequiredConstructPartPredicate(partIDs, slot);
    }
}