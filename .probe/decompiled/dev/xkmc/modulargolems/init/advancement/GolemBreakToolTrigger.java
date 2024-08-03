package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemBreakToolTrigger extends BaseCriterion<GolemBreakToolTrigger.Ins, GolemBreakToolTrigger> {

    public static GolemBreakToolTrigger.Ins ins() {
        return new GolemBreakToolTrigger.Ins(GolemTriggers.BREAK.m_7295_(), ContextAwarePredicate.ANY);
    }

    public GolemBreakToolTrigger(ResourceLocation id) {
        super(id, GolemBreakToolTrigger.Ins::new, GolemBreakToolTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemBreakToolTrigger.Ins, GolemBreakToolTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}