package dev.xkmc.l2complements.content.item.equipments;

import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraArmorConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EterniumArmor extends ExtraArmorConfig {

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (stack.isDamaged()) {
            stack.setDamageValue(0);
        }
    }
}