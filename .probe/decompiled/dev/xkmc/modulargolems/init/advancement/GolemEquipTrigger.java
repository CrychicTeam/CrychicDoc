package dev.xkmc.modulargolems.init.advancement;

import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GolemEquipTrigger extends BaseCriterion<GolemEquipTrigger.Ins, GolemEquipTrigger> {

    public static GolemEquipTrigger.Ins ins(int min) {
        GolemEquipTrigger.Ins ans = new GolemEquipTrigger.Ins(GolemTriggers.EQUIP.m_7295_(), ContextAwarePredicate.ANY);
        ans.minimum = min;
        return ans;
    }

    public GolemEquipTrigger(ResourceLocation id) {
        super(id, GolemEquipTrigger.Ins::new, GolemEquipTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, int count) {
        this.m_66234_(player, e -> e.minimum <= count);
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<GolemEquipTrigger.Ins, GolemEquipTrigger> {

        @SerialField
        private int minimum = 0;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}