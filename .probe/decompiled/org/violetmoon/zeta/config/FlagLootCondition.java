package org.violetmoon.zeta.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;

public record FlagLootCondition(ConfigFlagManager manager, String flag, LootItemConditionType selfType) implements LootItemCondition {

    public boolean test(LootContext lootContext) {
        return this.manager.getFlag(this.flag);
    }

    @NotNull
    @Override
    public LootItemConditionType getType() {
        return this.selfType;
    }

    public static final class FlagSerializer implements Serializer<FlagLootCondition> {

        private final ConfigFlagManager manager;

        public final LootItemConditionType selfType;

        public FlagSerializer(ConfigFlagManager manager) {
            this.manager = manager;
            this.selfType = new LootItemConditionType(this);
        }

        public void serialize(@NotNull JsonObject json, @NotNull FlagLootCondition value, @NotNull JsonSerializationContext context) {
            json.addProperty("flag", value.flag);
        }

        @NotNull
        public FlagLootCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            String flag = json.getAsJsonPrimitive("flag").getAsString();
            return new FlagLootCondition(this.manager, flag, this.selfType);
        }
    }
}