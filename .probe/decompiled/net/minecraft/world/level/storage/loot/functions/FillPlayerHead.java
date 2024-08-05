package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.authlib.GameProfile;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class FillPlayerHead extends LootItemConditionalFunction {

    final LootContext.EntityTarget entityTarget;

    public FillPlayerHead(LootItemCondition[] lootItemCondition0, LootContext.EntityTarget lootContextEntityTarget1) {
        super(lootItemCondition0);
        this.entityTarget = lootContextEntityTarget1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.FILL_PLAYER_HEAD;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(this.entityTarget.getParam());
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.is(Items.PLAYER_HEAD)) {
            Entity $$2 = lootContext1.getParamOrNull(this.entityTarget.getParam());
            if ($$2 instanceof Player) {
                GameProfile $$3 = ((Player) $$2).getGameProfile();
                itemStack0.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), $$3));
            }
        }
        return itemStack0;
    }

    public static LootItemConditionalFunction.Builder<?> fillPlayerHead(LootContext.EntityTarget lootContextEntityTarget0) {
        return m_80683_(p_165211_ -> new FillPlayerHead(p_165211_, lootContextEntityTarget0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<FillPlayerHead> {

        public void serialize(JsonObject jsonObject0, FillPlayerHead fillPlayerHead1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, fillPlayerHead1, jsonSerializationContext2);
            jsonObject0.add("entity", jsonSerializationContext2.serialize(fillPlayerHead1.entityTarget));
        }

        public FillPlayerHead deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            LootContext.EntityTarget $$3 = GsonHelper.getAsObject(jsonObject0, "entity", jsonDeserializationContext1, LootContext.EntityTarget.class);
            return new FillPlayerHead(lootItemCondition2, $$3);
        }
    }
}