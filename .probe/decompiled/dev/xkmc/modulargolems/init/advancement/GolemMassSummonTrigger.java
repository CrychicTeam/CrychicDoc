package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemMassSummonTrigger extends BaseCriterion<GolemMassSummonTrigger.Ins, GolemMassSummonTrigger> {

    public static GolemMassSummonTrigger.Ins ins() {
        return new GolemMassSummonTrigger.Ins(GolemTriggers.MAS_SUMMON.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static GolemMassSummonTrigger.Ins atLeast(int count) {
        GolemMassSummonTrigger.Ins ans = ins();
        ans.count = count;
        return ans;
    }

    public GolemMassSummonTrigger(ResourceLocation id) {
        super(id, GolemMassSummonTrigger.Ins::new, GolemMassSummonTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, int count) {
        this.m_66234_(player, e -> e.count <= count);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemMassSummonTrigger.Ins, GolemMassSummonTrigger> {

        @SerialField
        private int count;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}