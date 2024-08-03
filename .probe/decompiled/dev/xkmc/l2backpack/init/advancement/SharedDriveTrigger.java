package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SharedDriveTrigger extends BaseCriterion<SharedDriveTrigger.Ins, SharedDriveTrigger> {

    public static SharedDriveTrigger.Ins ins() {
        return new SharedDriveTrigger.Ins(BackpackTriggers.SHARE.m_7295_(), ContextAwarePredicate.ANY);
    }

    public SharedDriveTrigger(ResourceLocation id) {
        super(id, SharedDriveTrigger.Ins::new, SharedDriveTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<SharedDriveTrigger.Ins, SharedDriveTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}