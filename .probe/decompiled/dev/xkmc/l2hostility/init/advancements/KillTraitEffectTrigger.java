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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

public class KillTraitEffectTrigger extends BaseCriterion<KillTraitEffectTrigger.Ins, KillTraitEffectTrigger> {

    public static KillTraitEffectTrigger.Ins ins(MobTrait traits, MobEffect effect) {
        KillTraitEffectTrigger.Ins ans = new KillTraitEffectTrigger.Ins(HostilityTriggers.TRAIT_EFFECT.m_7295_(), ContextAwarePredicate.ANY);
        ans.trait = traits;
        ans.effect = effect;
        return ans;
    }

    public KillTraitEffectTrigger(ResourceLocation id) {
        super(id, KillTraitEffectTrigger.Ins::new, KillTraitEffectTrigger.Ins.class);
    }

    public void trigger(ServerPlayer player, LivingEntity le, MobTraitCap cap) {
        this.m_66234_(player, e -> e.matchAll(le, cap));
    }

    @SerialClass
    public static class Ins extends BaseCriterionInstance<KillTraitEffectTrigger.Ins, KillTraitEffectTrigger> {

        @SerialField
        public MobTrait trait;

        @SerialField
        public MobEffect effect;

        public Ins(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matchAll(LivingEntity le, MobTraitCap cap) {
            return cap.hasTrait(this.trait) && le.hasEffect(this.effect);
        }
    }
}