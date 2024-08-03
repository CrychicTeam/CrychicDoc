package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetLoreFunction extends LootItemConditionalFunction {

    final boolean replace;

    final List<Component> lore;

    @Nullable
    final LootContext.EntityTarget resolutionContext;

    public SetLoreFunction(LootItemCondition[] lootItemCondition0, boolean boolean1, List<Component> listComponent2, @Nullable LootContext.EntityTarget lootContextEntityTarget3) {
        super(lootItemCondition0);
        this.replace = boolean1;
        this.lore = ImmutableList.copyOf(listComponent2);
        this.resolutionContext = lootContextEntityTarget3;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_LORE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.resolutionContext != null ? ImmutableSet.of(this.resolutionContext.getParam()) : ImmutableSet.of();
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        ListTag $$2 = this.getLoreTag(itemStack0, !this.lore.isEmpty());
        if ($$2 != null) {
            if (this.replace) {
                $$2.clear();
            }
            UnaryOperator<Component> $$3 = SetNameFunction.createResolver(lootContext1, this.resolutionContext);
            this.lore.stream().map($$3).map(Component.Serializer::m_130703_).map(StringTag::m_129297_).forEach($$2::add);
        }
        return itemStack0;
    }

    @Nullable
    private ListTag getLoreTag(ItemStack itemStack0, boolean boolean1) {
        CompoundTag $$2;
        if (itemStack0.hasTag()) {
            $$2 = itemStack0.getTag();
        } else {
            if (!boolean1) {
                return null;
            }
            $$2 = new CompoundTag();
            itemStack0.setTag($$2);
        }
        CompoundTag $$5;
        if ($$2.contains("display", 10)) {
            $$5 = $$2.getCompound("display");
        } else {
            if (!boolean1) {
                return null;
            }
            $$5 = new CompoundTag();
            $$2.put("display", $$5);
        }
        if ($$5.contains("Lore", 9)) {
            return $$5.getList("Lore", 8);
        } else if (boolean1) {
            ListTag $$8 = new ListTag();
            $$5.put("Lore", $$8);
            return $$8;
        } else {
            return null;
        }
    }

    public static SetLoreFunction.Builder setLore() {
        return new SetLoreFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetLoreFunction.Builder> {

        private boolean replace;

        private LootContext.EntityTarget resolutionContext;

        private final List<Component> lore = Lists.newArrayList();

        public SetLoreFunction.Builder setReplace(boolean boolean0) {
            this.replace = boolean0;
            return this;
        }

        public SetLoreFunction.Builder setResolutionContext(LootContext.EntityTarget lootContextEntityTarget0) {
            this.resolutionContext = lootContextEntityTarget0;
            return this;
        }

        public SetLoreFunction.Builder addLine(Component component0) {
            this.lore.add(component0);
            return this;
        }

        protected SetLoreFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetLoreFunction(this.m_80699_(), this.replace, this.lore, this.resolutionContext);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetLoreFunction> {

        public void serialize(JsonObject jsonObject0, SetLoreFunction setLoreFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setLoreFunction1, jsonSerializationContext2);
            jsonObject0.addProperty("replace", setLoreFunction1.replace);
            JsonArray $$3 = new JsonArray();
            for (Component $$4 : setLoreFunction1.lore) {
                $$3.add(Component.Serializer.toJsonTree($$4));
            }
            jsonObject0.add("lore", $$3);
            if (setLoreFunction1.resolutionContext != null) {
                jsonObject0.add("entity", jsonSerializationContext2.serialize(setLoreFunction1.resolutionContext));
            }
        }

        public SetLoreFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            boolean $$3 = GsonHelper.getAsBoolean(jsonObject0, "replace", false);
            List<Component> $$4 = (List<Component>) Streams.stream(GsonHelper.getAsJsonArray(jsonObject0, "lore")).map(Component.Serializer::m_130691_).collect(ImmutableList.toImmutableList());
            LootContext.EntityTarget $$5 = GsonHelper.getAsObject(jsonObject0, "entity", null, jsonDeserializationContext1, LootContext.EntityTarget.class);
            return new SetLoreFunction(lootItemCondition2, $$3, $$4, $$5);
        }
    }
}