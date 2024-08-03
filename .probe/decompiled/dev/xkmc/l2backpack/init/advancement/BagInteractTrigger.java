package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BagInteractTrigger extends BaseCriterion<BagInteractTrigger.Ins, BagInteractTrigger> {

    public static BagInteractTrigger.Ins fromType(BagInteractTrigger.Type type) {
        BagInteractTrigger.Ins ans = new BagInteractTrigger.Ins(BackpackTriggers.INTERACT.m_7295_(), ContextAwarePredicate.ANY);
        ans.type = type;
        return ans;
    }

    public static BagInteractTrigger.Ins fromType(BagInteractTrigger.Type type, Item... items) {
        BagInteractTrigger.Ins ans = fromType(type);
        ans.ingredient = Ingredient.of(items);
        return ans;
    }

    public BagInteractTrigger(ResourceLocation id) {
        super(id, BagInteractTrigger.Ins::new, BagInteractTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, ItemStack stack, BagInteractTrigger.Type type, int count) {
        if (count > 0) {
            this.m_66234_(player, e -> e.type == type && (e.ingredient.isEmpty() || e.ingredient.test(stack)));
        }
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<BagInteractTrigger.Ins, BagInteractTrigger> {

        @SerialField
        private BagInteractTrigger.Type type;

        @SerialField
        private Ingredient ingredient = Ingredient.EMPTY;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }

    public static enum Type {

        COLLECT, EXTRACT, LOAD, DUMP
    }
}