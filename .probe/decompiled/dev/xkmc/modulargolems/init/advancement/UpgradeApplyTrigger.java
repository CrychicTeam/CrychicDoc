package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class UpgradeApplyTrigger extends BaseCriterion<UpgradeApplyTrigger.Ins, UpgradeApplyTrigger> {

    public static UpgradeApplyTrigger.Ins ins() {
        return new UpgradeApplyTrigger.Ins(GolemTriggers.UPGRADE_APPLY.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static UpgradeApplyTrigger.Ins withUpgrade(UpgradeItem item) {
        UpgradeApplyTrigger.Ins ans = ins();
        ans.ingredient = Ingredient.of(item);
        return ans;
    }

    public static UpgradeApplyTrigger.Ins withRemain(int remain) {
        UpgradeApplyTrigger.Ins ans = ins();
        ans.remain = remain;
        return ans;
    }

    public static UpgradeApplyTrigger.Ins withTotal(int total) {
        UpgradeApplyTrigger.Ins ans = ins();
        ans.total = total;
        return ans;
    }

    public UpgradeApplyTrigger(ResourceLocation id) {
        super(id, UpgradeApplyTrigger.Ins::new, UpgradeApplyTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, ItemStack upgrade, int remain, int total) {
        this.m_66234_(player, e -> (e.ingredient.isEmpty() || e.ingredient.test(upgrade)) && (e.remain < 0 || e.remain >= remain) && (e.total < 0 || e.total <= total));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<UpgradeApplyTrigger.Ins, UpgradeApplyTrigger> {

        @SerialField
        private Ingredient ingredient = Ingredient.EMPTY;

        @SerialField
        private int remain = -1;

        @SerialField
        private int total = -1;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}