package com.mna.advancements.predicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.entities.constructs.animated.Construct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class SummonConstructPredicate {

    private HashMap<ConstructMaterial, Integer> materialRequirements = new HashMap();

    private HashMap<Integer, Integer> tierPartRequirements = new HashMap();

    private ArrayList<RequiredConstructPartPredicate> requiredParts = new ArrayList();

    public boolean test(Construct construct) {
        boolean pass = true;
        for (Entry<ConstructMaterial, Integer> kvp : this.materialRequirements.entrySet()) {
            pass &= this.test_material(construct, (ConstructMaterial) kvp.getKey(), (Integer) kvp.getValue());
        }
        for (RequiredConstructPartPredicate req : this.requiredParts) {
            pass &= req.matches(construct.getConstructData());
        }
        if (this.tierPartRequirements.containsKey(1)) {
            pass &= this.test_material(construct, ConstructMaterial.WICKERWOOD, (Integer) this.tierPartRequirements.get(1));
        }
        if (this.tierPartRequirements.containsKey(2)) {
            pass &= this.test_material(construct, ConstructMaterial.WOOD, (Integer) this.tierPartRequirements.get(2)) || this.test_material(construct, ConstructMaterial.STONE, (Integer) this.tierPartRequirements.get(2));
        }
        if (this.tierPartRequirements.containsKey(3)) {
            pass &= this.test_material(construct, ConstructMaterial.IRON, (Integer) this.tierPartRequirements.get(3)) || this.test_material(construct, ConstructMaterial.GOLD, (Integer) this.tierPartRequirements.get(3));
        }
        if (this.tierPartRequirements.containsKey(4)) {
            pass &= this.test_material(construct, ConstructMaterial.OBSIDIAN, (Integer) this.tierPartRequirements.get(4)) || this.test_material(construct, ConstructMaterial.DIAMOND, (Integer) this.tierPartRequirements.get(4));
        }
        return pass;
    }

    private boolean test_material(Construct construct, ConstructMaterial material, int minimum) {
        return minimum < 1 ? true : construct.getConstructData().getPartsForMaterial(material).size() >= minimum;
    }

    public static SummonConstructPredicate fromJSON(JsonObject json) {
        SummonConstructPredicate predicate = new SummonConstructPredicate();
        if (json.has("materials") && json.get("materials").isJsonObject()) {
            JsonObject materials = json.getAsJsonObject("materials");
            ConstructMaterial.ALL_MATERIALS.forEach(mat -> {
                if (materials.has(mat.getId().toString())) {
                    predicate.materialRequirements.put(mat, materials.get(mat.getId().toString()).getAsInt());
                }
            });
        }
        if (json.has("numMaterialsForTier") && json.get("numMaterialsForTier").isJsonObject()) {
            JsonObject tierMaterials = json.getAsJsonObject("numMaterialsForTier");
            String tier_prefix = "tier_";
            for (int tier = 1; tier <= 4; tier++) {
                if (tierMaterials.has(tier_prefix + tier)) {
                    predicate.tierPartRequirements.put(tier, tierMaterials.get(tier_prefix + tier).getAsInt());
                }
            }
        }
        if (json.has("partRequirements") && json.get("partRequirements").isJsonArray()) {
            JsonArray partRequirements = json.get("partRequirements").getAsJsonArray();
            partRequirements.forEach(elem -> {
                if (elem.isJsonObject()) {
                    predicate.requiredParts.add(RequiredConstructPartPredicate.fromJSON(elem.getAsJsonObject()));
                }
            });
        }
        return predicate;
    }
}