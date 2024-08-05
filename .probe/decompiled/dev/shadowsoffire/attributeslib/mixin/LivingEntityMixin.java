package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.api.ALCombatRules;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.shadowsoffire.attributeslib.util.IAttributeManager;
import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import javax.annotation.Nullable;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity {

    @Shadow
    private AttributeMap attributes;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(at = { @At("TAIL") }, method = { "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V" }, require = 1, remap = false)
    public void apoth_ownedAttrMap(EntityType<?> type, Level level, CallbackInfo ci) {
        ((IEntityOwned) this.attributes).setOwner((LivingEntity) this);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(FF)F"), method = { "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F" })
    public float apoth_sunderingApplyEffect(float value, float max, DamageSource source, float damage) {
        if (this.hasEffect(ALObjects.MobEffects.SUNDERING.get()) && !source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            int level = this.getEffect(ALObjects.MobEffects.SUNDERING.get()).getAmplifier() + 1;
            value += damage * (float) level * 0.2F;
        }
        return Math.max(value, max);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z"), method = { "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F" })
    public boolean apoth_sunderingHasEffect(LivingEntity ths, MobEffect effect) {
        return true;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getAmplifier()I"), method = { "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F" })
    public int apoth_sunderingGetAmplifier(@Nullable MobEffectInstance inst) {
        return inst == null ? -1 : inst.getAmplifier();
    }

    @Shadow
    public abstract boolean hasEffect(MobEffect var1);

    @Shadow
    public abstract MobEffectInstance getEffect(MobEffect var1);

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"), method = { "getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F" }, require = 1)
    public float apoth_applyArmorPen(float amount, float armor, float toughness, DamageSource src, float amt2) {
        return ALCombatRules.getDamageAfterArmor((LivingEntity) this, src, amount, armor, toughness);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"), method = { "getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F" }, require = 1)
    public float apoth_applyProtPen(float amount, float protPoints, DamageSource src, float amt2) {
        return ALCombatRules.getDamageAfterProtection((LivingEntity) this, src, amount, protPoints);
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;removeAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V") }, method = { "onEffectUpdated" }, require = 1)
    public void apoth_onEffectUpdateRemoveAttribute(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        ((IAttributeManager) this.attributes).setAttributesUpdating(true);
    }

    @Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifiers(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/ai/attributes/AttributeMap;I)V", shift = Shift.AFTER) }, method = { "onEffectUpdated" }, require = 1)
    public void apoth_onEffectUpdateAddAttribute(MobEffectInstance pEffectInstance, boolean pForced, Entity pEntity, CallbackInfo ci) {
        ((IAttributeManager) this.attributes).setAttributesUpdating(false);
    }
}