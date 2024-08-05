package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemAnvilFixTrigger extends BaseCriterion<GolemAnvilFixTrigger.Ins, GolemAnvilFixTrigger> {

    public static GolemAnvilFixTrigger.Ins ins() {
        return new GolemAnvilFixTrigger.Ins(GolemTriggers.ANVIL_FIX.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static GolemAnvilFixTrigger.Ins withMat(ResourceLocation mat) {
        GolemAnvilFixTrigger.Ins ans = ins();
        ans.rl = mat;
        return ans;
    }

    public GolemAnvilFixTrigger(ResourceLocation id) {
        super(id, GolemAnvilFixTrigger.Ins::new, GolemAnvilFixTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, ResourceLocation mat) {
        this.m_66234_(player, e -> e.rl == null || e.rl.equals(mat));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemAnvilFixTrigger.Ins, GolemAnvilFixTrigger> {

        @Nullable
        @SerialField
        private ResourceLocation rl = null;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}