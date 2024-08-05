package dev.xkmc.l2weaponry.content.item.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.ArrayList;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class TooltipProcessor {

    public static HashMultimap<Attribute, AttributeModifier> processTooltip(Multimap<Attribute, AttributeModifier> ans) {
        HashMultimap<Attribute, AttributeModifier> copy = HashMultimap.create();
        copy.putAll(ans);
        sumOp(copy, ForgeMod.ENTITY_REACH.get(), AttributeModifier.Operation.ADDITION);
        sumOp(copy, ForgeMod.BLOCK_REACH.get(), AttributeModifier.Operation.ADDITION);
        sumOp(copy, Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION);
        sumOp(copy, Attributes.ATTACK_SPEED, AttributeModifier.Operation.ADDITION);
        sumOp(copy, (Attribute) L2DamageTracker.CRIT_DMG.get(), AttributeModifier.Operation.ADDITION);
        sumOp(copy, (Attribute) LWItems.SHIELD_DEFENSE.get(), AttributeModifier.Operation.MULTIPLY_BASE);
        wrapMultiplier(copy, (Attribute) L2DamageTracker.CRIT_RATE.get());
        wrapMultiplier(copy, (Attribute) L2DamageTracker.CRIT_DMG.get());
        wrapMultiplier(copy, (Attribute) L2DamageTracker.BOW_STRENGTH.get());
        return copy;
    }

    private static void sumOp(Multimap<Attribute, AttributeModifier> ans, Attribute attr, AttributeModifier.Operation op) {
        ArrayList<AttributeModifier> list = new ArrayList(ans.get(attr));
        AttributeModifier mod = null;
        double add = 0.0;
        for (AttributeModifier e : list) {
            if (e.getOperation() == op) {
                if (mod == null) {
                    mod = e;
                } else {
                    add += e.getAmount();
                    ans.remove(attr, e);
                }
            }
        }
        if (add != 0.0) {
            ans.remove(attr, mod);
            add += mod.getAmount();
            ans.put(attr, new AttributeModifier(mod.getId(), mod.getName(), add, op));
        }
    }

    private static void wrapMultiplier(Multimap<Attribute, AttributeModifier> ans, Attribute attr) {
        for (AttributeModifier e : new ArrayList(ans.get(attr))) {
            if (e.getOperation() == AttributeModifier.Operation.ADDITION) {
                ans.remove(attr, e);
                ans.put(attr, new AttributeModifier(e.getId(), e.getName(), e.getAmount(), AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }
}