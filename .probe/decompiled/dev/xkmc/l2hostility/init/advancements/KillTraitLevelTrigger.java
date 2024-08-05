package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KillTraitLevelTrigger extends BaseCriterion<KillTraitLevelTrigger.Ins, KillTraitLevelTrigger> {

    public static KillTraitLevelTrigger.Ins ins(MobTrait traits, int rank) {
        KillTraitLevelTrigger.Ins ans = new KillTraitLevelTrigger.Ins(HostilityTriggers.TRAIT_LEVEL.m_7295_(), ContextAwarePredicate.ANY);
        ans.trait = traits;
        ans.rank = rank;
        return ans;
    }

    public KillTraitLevelTrigger(ResourceLocation id) {
        super(id, KillTraitLevelTrigger.Ins::new, KillTraitLevelTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, MobTraitCap cap) {
        this.m_66234_(player, e -> e.matchAll(cap));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<KillTraitLevelTrigger.Ins, KillTraitLevelTrigger> {

        @SerialField
        public MobTrait trait;

        @SerialField
        public int rank;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matchAll(MobTraitCap cap) {
            return this.trait != null && cap.getTraitLevel(this.trait) >= this.rank;
        }
    }
}