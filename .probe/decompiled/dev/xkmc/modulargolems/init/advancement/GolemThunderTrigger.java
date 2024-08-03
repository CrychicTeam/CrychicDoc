package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemThunderTrigger extends BaseCriterion<GolemThunderTrigger.Ins, GolemThunderTrigger> {

    public static GolemThunderTrigger.Ins ins() {
        return new GolemThunderTrigger.Ins(GolemTriggers.THUNDER.m_7295_(), ContextAwarePredicate.ANY);
    }

    public GolemThunderTrigger(ResourceLocation id) {
        super(id, GolemThunderTrigger.Ins::new, GolemThunderTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemThunderTrigger.Ins, GolemThunderTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}