package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class GolemKillTrigger extends BaseCriterion<GolemKillTrigger.Ins, GolemKillTrigger> {

    public static GolemKillTrigger.Ins ins() {
        return new GolemKillTrigger.Ins(GolemTriggers.KILL.m_7295_(), ContextAwarePredicate.ANY);
    }

    public static GolemKillTrigger.Ins byType(EntityType<?> type) {
        GolemKillTrigger.Ins ans = ins();
        ans.type = type;
        return ans;
    }

    public GolemKillTrigger(ResourceLocation id) {
        super(id, GolemKillTrigger.Ins::new, GolemKillTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, Entity killed) {
        this.m_66234_(player, e -> e.type == null || e.type == killed.getType());
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemKillTrigger.Ins, GolemKillTrigger> {

        @Nullable
        @SerialField
        private EntityType<?> type;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}