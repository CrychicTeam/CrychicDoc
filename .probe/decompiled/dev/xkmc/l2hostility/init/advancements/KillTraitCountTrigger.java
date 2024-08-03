package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitCountTrigger extends BaseCriterion<KillTraitCountTrigger.Ins, KillTraitCountTrigger> {

    public static KillTraitCountTrigger.Ins ins(int count) {
        KillTraitCountTrigger.Ins ans = new KillTraitCountTrigger.Ins(HostilityTriggers.TRAIT_COUNT.m_7295_(), ContextAwarePredicate.ANY);
        ans.count = count;
        return ans;
    }

    public KillTraitCountTrigger(ResourceLocation id) {
        super(id, KillTraitCountTrigger.Ins::new, KillTraitCountTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, MobTraitCap cap) {
        this.m_66234_(player, e -> e.matchAll(cap));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<KillTraitCountTrigger.Ins, KillTraitCountTrigger> {

        @SerialField
        public int count;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matchAll(MobTraitCap cap) {
            return cap.traits.size() >= this.count;
        }
    }
}