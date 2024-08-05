package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PartCraftTrigger extends BaseCriterion<PartCraftTrigger.Ins, PartCraftTrigger> {

    public static PartCraftTrigger.Ins ins() {
        return new PartCraftTrigger.Ins(GolemTriggers.PART_CRAFT.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static PartCraftTrigger.Ins withMat(ResourceLocation mat) {
        PartCraftTrigger.Ins ans = ins();
        ans.rl = mat;
        return ans;
    }

    public PartCraftTrigger(ResourceLocation id) {
        super(id, PartCraftTrigger.Ins::new, PartCraftTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, ResourceLocation rl) {
        this.m_66234_(player, e -> e.rl == null || e.rl.equals(rl));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<PartCraftTrigger.Ins, PartCraftTrigger> {

        @Nullable
        @SerialField
        private ResourceLocation rl = null;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}