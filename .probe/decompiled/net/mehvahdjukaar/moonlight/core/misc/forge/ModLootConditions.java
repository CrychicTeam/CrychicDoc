package net.mehvahdjukaar.moonlight.core.misc.forge;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public class ModLootConditions {

    public static final Supplier<LootItemConditionType> ICONDITION_LOOT_CONDITION = RegHelper.register(Moonlight.res("iconditions"), () -> new LootItemConditionType(new ModLootConditions.IConditionLootCondition.ConditionSerializer()), Registries.LOOT_CONDITION_TYPE);

    public static final Supplier<LootItemConditionType> PATTERN_MATCH_CONDITION = RegHelper.register(Moonlight.res("loot_table_id_patter"), () -> new LootItemConditionType(new ModLootConditions.PatternMatchCondition.ConditionSerializer()), Registries.LOOT_CONDITION_TYPE);

    public static void register() {
    }

    public static record IConditionLootCondition(List<ICondition> conditions) implements LootItemCondition {

        public boolean test(LootContext lootContext) {
            for (ICondition c : this.conditions) {
                if (!c.test(ICondition.IContext.EMPTY)) {
                    return false;
                }
            }
            return true;
        }

        @NotNull
        @Override
        public LootItemConditionType getType() {
            return (LootItemConditionType) ModLootConditions.ICONDITION_LOOT_CONDITION.get();
        }

        public static record ConditionSerializer() implements Serializer<ModLootConditions.IConditionLootCondition> {

            public void serialize(@NotNull JsonObject json, @NotNull ModLootConditions.IConditionLootCondition value, @NotNull JsonSerializationContext context) {
                JsonArray ja = new JsonArray();
                for (ICondition c : value.conditions) {
                    ja.add(CraftingHelper.serialize(c));
                }
                json.add("values", ja);
            }

            @NotNull
            public ModLootConditions.IConditionLootCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
                JsonArray ja = GsonHelper.getAsJsonArray(json, "values");
                List<ICondition> l = new ArrayList();
                for (JsonElement c : ja) {
                    if (!c.isJsonObject()) {
                        throw new JsonSyntaxException("Conditions must be an array of JsonObjects");
                    }
                    l.add(CraftingHelper.getCondition(c.getAsJsonObject()));
                }
                return new ModLootConditions.IConditionLootCondition(l);
            }
        }
    }

    public static record PatternMatchCondition(List<Pattern> patterns) implements LootItemCondition {

        public boolean test(LootContext lootContext) {
            String id = lootContext.getQueriedLootTableId().toString();
            for (Pattern p : this.patterns) {
                if (id.equals(p.pattern())) {
                    return true;
                }
                if (p.matcher(id).find()) {
                    return true;
                }
            }
            return false;
        }

        @Nonnull
        @Override
        public LootItemConditionType getType() {
            return (LootItemConditionType) ModLootConditions.PATTERN_MATCH_CONDITION.get();
        }

        public static record ConditionSerializer() implements Serializer<ModLootConditions.PatternMatchCondition> {

            public void serialize(@Nonnull JsonObject json, @Nonnull ModLootConditions.PatternMatchCondition value, @Nonnull JsonSerializationContext context) {
                JsonArray ja = new JsonArray();
                for (Pattern c : value.patterns) {
                    ja.add(c.pattern());
                }
                json.add("matches", ja);
            }

            @Nonnull
            public ModLootConditions.PatternMatchCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
                JsonArray ja = GsonHelper.getAsJsonArray(json, "matches");
                List<Pattern> l = new ArrayList();
                for (JsonElement c : ja) {
                    l.add(Pattern.compile(c.getAsString()));
                }
                return new ModLootConditions.PatternMatchCondition(l);
            }
        }
    }
}