package dev.xkmc.l2hostility.init.advancements;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2library.serial.advancements.BaseCriterion;
import dev.xkmc.l2library.serial.advancements.BaseCriterionInstance;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class KillTraitFlameTrigger extends BaseCriterion<KillTraitFlameTrigger.Ins, KillTraitFlameTrigger> {

    public static KillTraitFlameTrigger.Ins ins(MobTrait traits, KillTraitFlameTrigger.Type effect) {
        KillTraitFlameTrigger.Ins ans = new KillTraitFlameTrigger.Ins(HostilityTriggers.TRAIT_FLAME.m_7295_(), ContextAwarePredicate.ANY);
        ans.trait = traits;
        ans.effect = effect;
        return ans;
    }

    public KillTraitFlameTrigger(ResourceLocation id) {
        super(id, KillTraitFlameTrigger.Ins::new, KillTraitFlameTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, LivingEntity le, MobTraitCap cap) {
        this.m_66234_(player, e -> e.matchAll(le, cap));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<KillTraitFlameTrigger.Ins, KillTraitFlameTrigger> {

        @SerialField
        public MobTrait trait;

        @SerialField
        public KillTraitFlameTrigger.Type effect;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matchAll(LivingEntity le, MobTraitCap cap) {
            return cap.hasTrait(this.trait) && this.effect.match(le);
        }
    }

    public static enum Type {

        FLAME(le -> le.m_6060_());

        private final Predicate<LivingEntity> pred;

        private Type(Predicate<LivingEntity> pred) {
            this.pred = pred;
        }

        public boolean match(LivingEntity le) {
            return this.pred.test(le);
        }
    }
}