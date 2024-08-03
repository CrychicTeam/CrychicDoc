package se.mickelus.tetra.effect;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class PryChargedEffect extends ChargedAbilityEffect {

    public static final PryChargedEffect instance = new PryChargedEffect();

    PryChargedEffect() {
        super(20, 0.0, 40, 3.0, ItemEffect.pry, ChargedAbilityEffect.TargetRequirement.entity, UseAnim.SPEAR, "raised");
    }

    @Override
    public boolean isAvailable(ItemModularHandheld item, ItemStack itemStack) {
        return super.isAvailable(item, itemStack) && item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge) > 0;
    }

    @Override
    public void perform(Player attacker, InteractionHand hand, ItemModularHandheld item, ItemStack itemStack, LivingEntity target, Vec3 hitVec, int chargedTicks) {
        if (!target.m_9236_().isClientSide) {
            int amplifier = item.getEffectLevel(itemStack, ItemEffect.pry);
            amplifier += (int) ((float) this.getOverchargeBonus(item, itemStack, chargedTicks) * item.getEffectEfficiency(itemStack, ItemEffect.abilityOvercharge));
            double damageMultiplier = 0.5;
            damageMultiplier += (double) (this.getOverchargeBonus(item, itemStack, chargedTicks) * item.getEffectLevel(itemStack, ItemEffect.abilityOvercharge)) / 100.0;
            int comboPoints = ComboPoints.get(attacker);
            boolean isSatiated = !attacker.getFoodData().needsFood();
            AbilityUseResult result = PryEffect.performRegular(attacker, item, itemStack, damageMultiplier, amplifier, target, isSatiated, comboPoints);
            item.tickProgression(attacker, itemStack, result == AbilityUseResult.fail ? 1 : 2);
            int echoLevel = item.getEffectLevel(itemStack, ItemEffect.abilityEcho);
            if (echoLevel > 0) {
                PryEffect.performEcho(attacker, item, itemStack, damageMultiplier, amplifier, target, isSatiated, comboPoints);
            }
        }
        attacker.causeFoodExhaustion(1.0F);
        attacker.m_21011_(hand, false);
        attacker.getCooldowns().addCooldown(item, this.getCooldown(item, itemStack));
        if (ComboPoints.canSpend(item, itemStack)) {
            ComboPoints.reset(attacker);
        }
        int revengeLevel = item.getEffectLevel(itemStack, ItemEffect.abilityRevenge);
        if (revengeLevel > 0) {
            RevengeTracker.removeEnemy(attacker, target);
        }
        item.applyDamage(2, itemStack, attacker);
    }
}