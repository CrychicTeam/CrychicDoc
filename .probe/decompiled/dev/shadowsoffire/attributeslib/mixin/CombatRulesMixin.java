package dev.shadowsoffire.attributeslib.mixin;

import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.ALCombatRules;
import net.minecraft.world.damagesource.CombatRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ CombatRules.class })
public class CombatRulesMixin {

    @Overwrite
    public static float getDamageAfterMagicAbsorb(float damage, float protPoints) {
        return damage * ALCombatRules.getProtDamageReduction(protPoints);
    }

    @Overwrite
    public static float getDamageAfterAbsorb(float damage, float armor, float toughness) {
        AttributesLib.LOGGER.trace("Invocation of CombatRules#getDamageAfterAbsorb is bypassing armor pen.");
        return damage * ALCombatRules.getArmorDamageReduction(damage, armor);
    }
}