package harmonised.pmmo.features.loot_modifiers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import harmonised.pmmo.core.Core;
import javax.annotation.Nonnull;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class SkillLootConditionKill implements LootItemCondition {

    private String skill;

    private Integer levelMin;

    private Integer levelMax;

    public SkillLootConditionKill(Integer levelMin, Integer levelMax, String skill) {
        this.levelMin = levelMin;
        this.levelMax = levelMax;
        this.skill = skill;
    }

    public boolean test(LootContext t) {
        Entity player = t.getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (player != null && this.skill != null) {
            int actualLevel = Core.get(player.level()).getData().getPlayerSkillLevel(this.skill, player.getUUID());
            return (this.levelMin == null || actualLevel >= this.levelMin) && (this.levelMax == null || actualLevel <= this.levelMax);
        } else {
            return false;
        }
    }

    @Override
    public LootItemConditionType getType() {
        return GLMRegistry.SKILL_KILL.get();
    }

    public static final class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SkillLootConditionKill> {

        public void serialize(JsonObject json, SkillLootConditionKill itemCondition, @Nonnull JsonSerializationContext context) {
            json.addProperty("skill", itemCondition.skill);
            json.addProperty("level_min", itemCondition.levelMin);
            json.addProperty("level_max", itemCondition.levelMax);
        }

        @Nonnull
        public SkillLootConditionKill deserialize(JsonObject json, @Nonnull JsonDeserializationContext context) {
            Integer levelMin = GsonHelper.getAsInt(json, "level_min");
            Integer levelMax = GsonHelper.getAsInt(json, "level_max");
            String skill = GsonHelper.getAsString(json, "skill");
            return new SkillLootConditionKill(levelMin, levelMax, skill);
        }
    }
}