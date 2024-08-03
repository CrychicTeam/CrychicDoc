package se.mickelus.tetra.effect;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

public abstract class ChargedAbilityEffect {

    protected int chargeTimeFlat;

    protected double chargeTimeSpeedMultiplier;

    protected int cooldownFlat;

    protected double cooldownSpeedMultiplier;

    protected ItemEffect effect;

    protected ChargedAbilityEffect.TargetRequirement target;

    protected String modelTransform;

    protected UseAnim useAction;

    public ChargedAbilityEffect(int chargeTimeFlat, double chargeTimeSpeedMultiplier, int cooldownFlat, double cooldownSpeedMultiplier, ItemEffect effect, ChargedAbilityEffect.TargetRequirement target, UseAnim useAction) {
        this.chargeTimeFlat = chargeTimeFlat;
        this.chargeTimeSpeedMultiplier = chargeTimeSpeedMultiplier;
        this.cooldownFlat = cooldownFlat;
        this.cooldownSpeedMultiplier = cooldownSpeedMultiplier;
        this.effect = effect;
        this.target = target;
        this.useAction = useAction;
    }

    public ChargedAbilityEffect(int chargeTimeFlat, double chargeTimeSpeedMultiplier, int cooldownFlat, double cooldownSpeedMultiplier, ItemEffect effect, ChargedAbilityEffect.TargetRequirement target, UseAnim pose, String modelTransform) {
        this(chargeTimeFlat, chargeTimeSpeedMultiplier, cooldownFlat, cooldownSpeedMultiplier, effect, target, pose);
        this.modelTransform = modelTransform;
    }

    public static double getOverchargeProgress(float progress) {
        if ((double) progress > 1.5) {
            return 0.75 * (double) progress + 0.875;
        } else {
            return (double) progress > 0.5 ? (double) progress + 0.5 : (double) (2.0F * progress);
        }
    }

    public boolean isAvailable(ItemModularHandheld item, ItemStack itemStack) {
        return item.getEffectLevel(itemStack, this.effect) > 0;
    }

    public boolean canCharge(ItemModularHandheld item, ItemStack itemStack) {
        return this.isAvailable(item, itemStack);
    }

    public boolean canOvercharge(ItemModularHandheld item, ItemStack itemStack) {
        return this.isAvailable(item, itemStack) && item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge) > 0;
    }

    public double getOverchargeProgress(ItemModularHandheld item, ItemStack itemStack, int chargedTicks) {
        int chargeTime = this.getChargeTime(item, itemStack);
        return getOverchargeProgress((float) chargedTicks * 1.0F / (float) chargeTime - 1.0F);
    }

    public int getOverchargeBonus(ItemModularHandheld item, ItemStack itemStack, int chargedTicks) {
        return (int) Mth.clamp(this.getOverchargeProgress(item, itemStack, chargedTicks), 0.0, 3.0);
    }

    public int getChargeTime(ItemModularHandheld item, ItemStack itemStack) {
        return (int) ((float) (this.chargeTimeFlat + (this.chargeTimeSpeedMultiplier != 0.0 ? (int) (item.getCooldownBase(itemStack) * 20.0 * this.chargeTimeSpeedMultiplier) : 0)) * this.getSpeedBonusMultiplier(item, itemStack));
    }

    public int getChargeTime(Player attacker, ItemModularHandheld item, ItemStack itemStack) {
        return this.getChargeTime(item, itemStack);
    }

    public int getCooldown(ItemModularHandheld item, ItemStack itemStack) {
        return (int) ((float) (this.cooldownFlat + (this.cooldownSpeedMultiplier != 0.0 ? (int) (item.getCooldownBase(itemStack) * 20.0 * this.cooldownSpeedMultiplier) : 0)) * this.getSpeedBonusMultiplier(item, itemStack));
    }

    public float getSpeedBonusMultiplier(ItemModularHandheld item, ItemStack itemStack) {
        return (float) (100 - item.getEffectLevel(itemStack, ItemEffect.abilitySpeed)) / 100.0F;
    }

    public boolean isDefensive(ItemModularHandheld item, ItemStack itemStack, InteractionHand hand) {
        return hand == InteractionHand.OFF_HAND && item.getEffectLevel(itemStack, ItemEffect.abilityDefensive) > 0;
    }

    public boolean canPerform(Player attacker, ItemModularHandheld item, ItemStack itemStack, @Nullable LivingEntity target, @Nullable BlockPos targetPos, int chargedTicks) {
        return this.isAvailable(item, itemStack) && chargedTicks >= this.getChargeTime(attacker, item, itemStack) && this.hasRequiredTarget(target, targetPos);
    }

    boolean hasRequiredTarget(@Nullable LivingEntity target, @Nullable BlockPos targetPos) {
        switch(this.target) {
            case entity:
                return target != null;
            case block:
                return targetPos != null;
            case either:
                return target != null || targetPos != null;
            case none:
                return true;
            default:
                return true;
        }
    }

    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, @Nullable LivingEntity target, @Nullable BlockPos targetPos, @Nullable Vec3 hitVec, int chargedTicks) {
        if (target != null) {
            this.perform(attacker, hand, item, itemStack, target, hitVec, chargedTicks);
        } else if (targetPos != null) {
            this.perform(attacker, hand, item, itemStack, targetPos, hitVec, chargedTicks);
        } else {
            this.perform(attacker, hand, item, itemStack, chargedTicks);
        }
    }

    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
    }

    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, BlockPos targetPos, Vec3 hitVec, int chargedTicks) {
    }

    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, int chargedTicks) {
    }

    public UseAnim getPose() {
        return this.useAction;
    }

    public String getModelTransform() {
        return this.modelTransform;
    }

    public static enum TargetRequirement {

        entity, block, either, none
    }
}