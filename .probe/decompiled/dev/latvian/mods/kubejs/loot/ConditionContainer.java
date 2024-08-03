package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.world.level.storage.loot.LootContext;

public interface ConditionContainer {

    ConditionContainer addCondition(JsonObject var1);

    default ConditionContainer randomChance(double chance) {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:random_chance");
        json.addProperty("chance", chance);
        return this.addCondition(json);
    }

    default ConditionContainer randomChanceWithLooting(double chance, double multiplier) {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:random_chance_with_looting");
        json.addProperty("chance", chance);
        json.addProperty("looting_multiplier", multiplier);
        return this.addCondition(json);
    }

    default ConditionContainer survivesExplosion() {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:survives_explosion");
        return this.addCondition(json);
    }

    default ConditionContainer entityProperties(LootContext.EntityTarget entity, JsonObject properties) {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:entity_properties");
        json.addProperty("entity", entity.name);
        json.add("predicate", properties);
        return this.addCondition(json);
    }

    default ConditionContainer killedByPlayer() {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:killed_by_player");
        return this.addCondition(json);
    }

    default ConditionContainer entityScores(LootContext.EntityTarget entity, Map<String, Object> scores) {
        JsonObject json = new JsonObject();
        json.addProperty("condition", "minecraft:entity_scores");
        json.addProperty("entity", entity.name);
        JsonObject s = new JsonObject();
        for (Entry<String, Object> entry : scores.entrySet()) {
            s.add((String) entry.getKey(), UtilsJS.numberProviderJson(UtilsJS.numberProviderOf(entry.getValue())));
        }
        json.add("scores", s);
        return this.addCondition(json);
    }
}