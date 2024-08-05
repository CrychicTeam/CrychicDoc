package dev.shadowsoffire.attributeslib.api;

import dev.shadowsoffire.attributeslib.ALConfig;
import java.math.BigDecimal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import repack.evalex.Expression;

public class ALCombatRules {

    public static float getDamageAfterProtection(LivingEntity target, DamageSource src, float amount, float protPoints) {
        if (src.getEntity() instanceof LivingEntity attacker) {
            float shred = (float) attacker.getAttributeValue(ALObjects.Attributes.PROT_SHRED.get());
            if (shred > 0.001F) {
                protPoints *= 1.0F - shred;
            }
            float pierce = (float) attacker.getAttributeValue(ALObjects.Attributes.PROT_PIERCE.get());
            if (pierce > 0.001F) {
                protPoints -= pierce;
            }
        }
        return protPoints <= 0.0F ? amount : amount * getProtDamageReduction(protPoints);
    }

    public static float getProtDamageReduction(float protPoints) {
        return ALConfig.getProtExpr().isPresent() ? ((Expression) ALConfig.getProtExpr().get()).setVariable("protPoints", new BigDecimal((double) protPoints)).eval().floatValue() : 1.0F - Math.min(0.025F * protPoints, 0.85F);
    }

    public static float getDamageAfterArmor(LivingEntity target, DamageSource src, float amount, float armor, float toughness) {
        if (src.getEntity() instanceof LivingEntity attacker) {
            float shred = (float) attacker.getAttributeValue(ALObjects.Attributes.ARMOR_SHRED.get());
            float bypassResist = Math.min(toughness * 0.02F, 0.6F);
            if (shred > 0.001F) {
                shred *= 1.0F - bypassResist;
                armor *= 1.0F - shred;
            }
            float pierce = (float) attacker.getAttributeValue(ALObjects.Attributes.ARMOR_PIERCE.get());
            if (pierce > 0.001F) {
                pierce *= 1.0F - bypassResist;
                armor -= pierce;
            }
        }
        return armor <= 0.0F ? amount : amount * getArmorDamageReduction(amount, armor);
    }

    public static float getAValue(float damage) {
        if (ALConfig.getAValueExpr().isPresent()) {
            return ((Expression) ALConfig.getAValueExpr().get()).setVariable("damage", new BigDecimal((double) damage)).eval().floatValue();
        } else {
            return damage < 20.0F ? 10.0F : 10.0F + (damage - 20.0F) / 2.0F;
        }
    }

    public static float getArmorDamageReduction(float damage, float armor) {
        float a = getAValue(damage);
        return ALConfig.getArmorExpr().isPresent() ? ((Expression) ALConfig.getArmorExpr().get()).setVariable("a", new BigDecimal((double) a)).setVariable("damage", new BigDecimal((double) damage)).setVariable("armor", new BigDecimal((double) armor)).eval().floatValue() : a / (a + armor);
    }
}