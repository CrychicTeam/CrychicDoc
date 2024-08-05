package net.mehvahdjukaar.moonlight.core.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.MoonlightRegistry;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.jetbrains.annotations.Nullable;

public class OptionalItemPool extends LootPoolSingletonContainer {

    @Nullable
    private final Item item;

    private final String res;

    OptionalItemPool(String res, int i, int j, LootItemCondition[] lootItemConditions, LootItemFunction[] lootItemFunctions) {
        super(i, j, disableIfInvalid(res, lootItemConditions), lootItemFunctions);
        this.item = getOptional(res);
        this.res = res;
    }

    @Nullable
    private static Item getOptional(String res) {
        if (res.startsWith("#")) {
            TagKey<Item> key = TagKey.create(Registries.ITEM, new ResourceLocation(res.substring(1)));
            Iterator var2 = BuiltInRegistries.ITEM.m_206058_(key).iterator();
            if (var2.hasNext()) {
                Holder<Item> v = (Holder<Item>) var2.next();
                return v.value();
            } else {
                return null;
            }
        } else {
            return (Item) BuiltInRegistries.ITEM.m_6612_(new ResourceLocation(res)).orElse(null);
        }
    }

    private static LootItemCondition[] disableIfInvalid(String res, LootItemCondition[] lootItemConditions) {
        if (getOptional(res) == null) {
            List<LootItemCondition> newCond = new ArrayList();
            newCond.add(LootItemRandomChanceCondition.randomChance(0.0F).build());
            newCond.addAll(List.of(lootItemConditions));
            return (LootItemCondition[]) newCond.toArray(new LootItemCondition[0]);
        } else {
            return lootItemConditions;
        }
    }

    @Override
    public LootPoolEntryType getType() {
        return (LootPoolEntryType) MoonlightRegistry.LAZY_ITEM.get();
    }

    @Override
    public void createItemStack(Consumer<ItemStack> stackConsumer, LootContext lootContext) {
        if (this.item != null) {
            stackConsumer.accept(new ItemStack(this.item));
        } else {
            Moonlight.LOGGER.warn("Tried to add an item from a disabled OptionalLootPoolEntry");
        }
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableOptionalItem(String itemRes) {
        return m_79687_((i, j, lootItemConditions, lootItemFunctions) -> new OptionalItemPool(itemRes, i, j, lootItemConditions, lootItemFunctions));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<OptionalItemPool> {

        public void serializeCustom(JsonObject object, OptionalItemPool context, JsonSerializationContext conditions) {
            super.serializeCustom(object, context, conditions);
            object.addProperty("name", context.res);
        }

        protected OptionalItemPool deserialize(JsonObject object, JsonDeserializationContext context, int weight, int quality, LootItemCondition[] conditions, LootItemFunction[] functions) {
            String item = getItemLocation(object, "name");
            return new OptionalItemPool(item, weight, quality, conditions, functions);
        }

        private static String getItemLocation(JsonObject json, String memberName) {
            if (json.has(memberName)) {
                return GsonHelper.getAsString(json, memberName);
            } else {
                throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item");
            }
        }
    }
}