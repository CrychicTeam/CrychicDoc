package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemHotFixTrigger extends BaseCriterion<GolemHotFixTrigger.Ins, GolemHotFixTrigger> {

    public static GolemHotFixTrigger.Ins ins() {
        return new GolemHotFixTrigger.Ins(GolemTriggers.HOT_FIX.m_7295_(), ContextAwarePredicate.ANY);
    }

    public GolemHotFixTrigger(ResourceLocation id) {
        super(id, GolemHotFixTrigger.Ins::new, GolemHotFixTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemHotFixTrigger.Ins, GolemHotFixTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}