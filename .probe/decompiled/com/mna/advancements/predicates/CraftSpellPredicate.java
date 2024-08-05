package com.mna.advancements.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.Registries;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.base.ISpellDefinition;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistry;

public class CraftSpellPredicate {

    int complexity;

    int tier;

    ResourceLocation factionRequirement;

    ArrayList<ResourceLocation> requiredParts;

    public CraftSpellPredicate(int min_tier, int min_complexity, ResourceLocation factionRequirement) {
        this.tier = min_tier;
        this.complexity = min_complexity;
        this.factionRequirement = factionRequirement;
        this.requiredParts = new ArrayList();
    }

    public boolean test(ISpellDefinition craftedSpell, Level world) {
        boolean pass = true;
        if (this.complexity > -1) {
            pass &= craftedSpell.getComplexity() >= (float) this.complexity;
        }
        if (this.tier > -1) {
            pass &= craftedSpell.getTier(world) >= this.tier;
        }
        if (this.factionRequirement != null) {
            IFaction faction = (IFaction) ((IForgeRegistry) Registries.Factions.get()).getValue(this.factionRequirement);
            if (faction != null) {
                pass &= craftedSpell.isFactionSpell(faction);
            }
        }
        for (ResourceLocation part : this.requiredParts) {
            pass &= craftedSpell.containsPart(part);
        }
        return pass;
    }

    public static CraftSpellPredicate fromJSON(JsonObject json) {
        CraftSpellPredicate predicate = new CraftSpellPredicate(-1, -1, null);
        if (json.has("tier")) {
            predicate.tier = json.get("tier").getAsInt();
        }
        if (json.has("complexity")) {
            predicate.complexity = json.get("complexity").getAsInt();
        }
        if (json.has("faction")) {
            predicate.factionRequirement = new ResourceLocation(json.get("faction").getAsString());
        }
        if (json.has("requiredParts") && json.get("requiredParts").isJsonArray()) {
            JsonArray requiredParts = json.getAsJsonArray("requiredParts");
            requiredParts.forEach(e -> predicate.requiredParts.add(new ResourceLocation(e.getAsString())));
        }
        return predicate;
    }
}