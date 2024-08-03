package dev.xkmc.l2backpack.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class RemoteHopperTrigger extends BaseCriterion<RemoteHopperTrigger.Ins, RemoteHopperTrigger> {

    public static RemoteHopperTrigger.Ins ins() {
        return new RemoteHopperTrigger.Ins(BackpackTriggers.REMOTE.m_7295_(), ContextAwarePredicate.ANY);
    }

    public RemoteHopperTrigger(ResourceLocation id) {
        super(id, RemoteHopperTrigger.Ins::new, RemoteHopperTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, e -> true);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<RemoteHopperTrigger.Ins, RemoteHopperTrigger> {

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}