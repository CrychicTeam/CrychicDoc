package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class AnalogSignalTrigger extends BaseCriterion<AnalogSignalTrigger.Ins, AnalogSignalTrigger> {

    public static AnalogSignalTrigger.Ins ins() {
        return new AnalogSignalTrigger.Ins(BackpackTriggers.ANALOG.m_7295_(), ContextAwarePredicate.ANY);
    }

    public AnalogSignalTrigger(ResourceLocation id) {
        super(id, AnalogSignalTrigger.Ins::new, AnalogSignalTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<AnalogSignalTrigger.Ins, AnalogSignalTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}